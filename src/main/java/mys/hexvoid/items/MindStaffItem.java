package mys.hexvoid.items;


import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.utils.MediaHelper;
import at.petrak.hexcasting.common.lib.HexAttributes;
import at.petrak.hexcasting.common.lib.HexSounds;
import at.petrak.hexcasting.common.msgs.MsgClearSpiralPatternsS2C;
import at.petrak.hexcasting.common.msgs.MsgOpenSpellGuiS2C;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import kotlin.Pair;
import mys.hexvoid.casting.vm.MindStaffEnv;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static at.petrak.hexcasting.forge.xplat.ForgeXplatImpl.TAG_PATTERNS;
import static at.petrak.hexcasting.forge.xplat.ForgeXplatImpl.TAG_VM;

public class MindStaffItem extends Item {
    public static final String TAG_MEDIA = "storedMedia";
    public static final String TAG_LAST_MAX_MEDIA = "lastMaxMedia";
    public static final String TAG_CAN_STORE = "canStoreMedia";

    public static final TextColor HEX_COLOR = TextColor.fromRgb(11767539);
    private static final DecimalFormat PERCENTAGE = new DecimalFormat("####");
    private static final DecimalFormat DUST_AMOUNT;

    static {
        PERCENTAGE.setRoundingMode(RoundingMode.DOWN);
        DUST_AMOUNT = new DecimalFormat("###,###.##");
    }

    public MindStaffItem(Properties pProperties) {
        super(pProperties);
    }

    public static boolean canStore(@NotNull ItemStack stack) {
        var tag = stack.getTag();
        if (tag != null) {
            return tag.contains(TAG_CAN_STORE);
        } else {
            return false;
        }
    }

    public static void setCanStore(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(TAG_CAN_STORE, true);
    }

    public static long getStoredMedia(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getLong(TAG_MEDIA);
    }

    public static long getMaxMedia(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        var max_last = tag.getLong(TAG_LAST_MAX_MEDIA);
        if (max_last <= 0) max_last = 1;
        return max_last;
    }

    public static void setStoredMedia(ItemStack stack, long media) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putLong(TAG_MEDIA, media);
    }

    public static void setMaxMedia(ItemStack stack, long media) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putLong(TAG_LAST_MAX_MEDIA, media);
    }

    public static long extractStoredMedia(ItemStack stack, long requested, boolean simulate) {
        long stored = getStoredMedia(stack);
        long paid = Math.min(requested, stored);

        if (!simulate) {
            setStoredMedia(stack, stored - paid);
        }

        return requested - paid;
    }

    @SuppressWarnings("unused")
    public static long insertStoredMedia(ItemStack stack, long amount, boolean simulate) {
        long stored = getStoredMedia(stack);
        long last_max = getMaxMedia(stack);
        long media_current = stored + amount;

        if (!simulate) {
            setStoredMedia(stack, media_current);
            if (media_current > last_max) setMaxMedia(stack, media_current);
        }

        return amount;
    }

    private static InteractionHand getAnotherHand(InteractionHand hand) {
        if (hand.equals(InteractionHand.MAIN_HAND)) return InteractionHand.OFF_HAND;
        else return hand;
    }

    private static void tryStore(@NotNull ItemStack source, @NotNull ItemStack staff) {
        if (source.isEmpty()) return;

        ADMediaHolder holder = IXplatAbstractions.INSTANCE.findMediaHolder(source);
        if (holder == null || !holder.canProvide()) return;

        long extracted = MediaHelper.extractMedia(source);
        if (extracted <= 0) return;

        setStoredMedia(staff, getStoredMedia(staff) + extracted);
        if (getStoredMedia(staff) + extracted > getMaxMedia(staff))
            setMaxMedia(staff, getStoredMedia(staff) + extracted);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        if (player.getAttributeValue(HexAttributes.FEEBLE_MIND) > (double) 0.0F) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        } else {

            if (canStore(player.getItemInHand(hand))) {
                var staff = player.getItemInHand(hand);
                var tag = staff.getOrCreateTag();
                if (tag.contains(TAG_MEDIA)) {
                    tag.putInt("Damage", (int) (getStoredMedia(staff) / getMaxMedia(staff)));
                }
                var stackAnother = player.getItemInHand(getAnotherHand(hand));
                tryStore(stackAnother, staff);
            }

            if (player.isShiftKeyDown()) {
                if (world.isClientSide()) {
                    player.playSound(HexSounds.STAFF_RESET, 1.0F, 1.0F);
                } else if (player instanceof ServerPlayer serverPlayer) {
                    IXplatAbstractions.INSTANCE.clearCastingData(serverPlayer);
                    MsgClearSpiralPatternsS2C packet = new MsgClearSpiralPatternsS2C(player.getUUID());
                    IXplatAbstractions.INSTANCE.sendPacketToPlayer(serverPlayer, packet);
                    IXplatAbstractions.INSTANCE.sendPacketTracking(serverPlayer, packet);
                }
            }

            if (!world.isClientSide() && player instanceof ServerPlayer serverPlayer) {
                CastingVM vm = new CastingVM(
                        CastingImage.loadFromNbt(
                                player.getPersistentData().getCompound(TAG_VM),
                                serverPlayer.serverLevel()
                        ),
                        new MindStaffEnv(serverPlayer, hand)
                );

                ListTag patternsTag = player.getPersistentData().getList(TAG_PATTERNS, Tag.TAG_COMPOUND);

                Pair<List<CompoundTag>, CompoundTag> descs = vm.generateDescs();
                IXplatAbstractions.INSTANCE.sendPacketToPlayer(serverPlayer, new MsgOpenSpellGuiS2C(hand, new ArrayList<>(patternsTag.size()), descs.getFirst(), descs.getSecond(), 0));
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            if (!canStore(player.getItemInHand(hand))) {
                player.getItemInHand(hand).hurtAndBreak(1, player, (T) -> {
                });
                player.setItemInHand(hand, new ItemStack(Items.STICK, 1));
            }
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        if (canStore(stack)) {
            long maxMedia = getMaxMedia(stack);
            if (maxMedia > 0L) {
                long media = getStoredMedia(stack);
                float fullness = this.getMediaFullness(stack);
                TextColor color = TextColor.fromRgb(MediaHelper.mediaBarColor(media, maxMedia));
                MutableComponent mediamount = Component.literal(DUST_AMOUNT.format((float) media / 10000.0F));
                MutableComponent percentFull = Component.literal(PERCENTAGE.format(100.0F * fullness) + "%");
                MutableComponent maxCapacity = Component.translatable("hexcasting.tooltip.media", DUST_AMOUNT.format((float) maxMedia / 10000.0F));
                mediamount.withStyle((style) -> style.withColor(HEX_COLOR));
                maxCapacity.withStyle((style) -> style.withColor(HEX_COLOR));
                percentFull.withStyle((style) -> style.withColor(color));
                pTooltipComponents.add(Component.translatable("hexvoid.tooltip.media_amount", mediamount, maxCapacity, percentFull));
            }
        }
        super.appendHoverText(stack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private float getMediaFullness(ItemStack stack) {
        return getMaxMedia(stack) == 0L ? 0.0F : (float) getStoredMedia(stack) / getMaxMedia(stack);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return canStore(stack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return MediaHelper.mediaBarWidth(getStoredMedia(stack), getMaxMedia(stack));
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return MediaHelper.mediaBarColor(getStoredMedia(stack), getMaxMedia(stack));
    }
}
