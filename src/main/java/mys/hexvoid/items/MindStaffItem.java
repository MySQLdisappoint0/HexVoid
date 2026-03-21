package mys.hexvoid.items;


import at.petrak.hexcasting.api.casting.eval.ResolvedPattern;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.common.lib.HexAttributes;
import at.petrak.hexcasting.common.lib.HexSounds;
import at.petrak.hexcasting.common.msgs.MsgClearSpiralPatternsS2C;
import at.petrak.hexcasting.common.msgs.MsgOpenSpellGuiS2C;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import kotlin.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static at.petrak.hexcasting.forge.xplat.ForgeXplatImpl.TAG_PATTERNS;

public class MindStaffItem extends Item {
    public MindStaffItem(Properties pProperties) {
        super(pProperties);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        if (player.getAttributeValue(HexAttributes.FEEBLE_MIND) > (double)0.0F) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        } else {
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
                CastingVM vm = IXplatAbstractions.INSTANCE.getStaffcastVM(serverPlayer, hand);

                ListTag patternsTag = player.getPersistentData().getList(TAG_PATTERNS, Tag.TAG_COMPOUND);
                List<ResolvedPattern> patterns = new ArrayList<>(patternsTag.size());

                Pair<List<CompoundTag>, CompoundTag> descs = vm.generateDescs();
                IXplatAbstractions.INSTANCE.sendPacketToPlayer(serverPlayer, new MsgOpenSpellGuiS2C(hand, patterns, descs.getFirst(), descs.getSecond(), 0));
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            player.getItemInHand(hand).hurtAndBreak(1, player, (T) -> {});
            player.setItemInHand(hand, new ItemStack(Items.STICK, 1));
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        var stack = pStack.copy();
        stack.shrink(1);
        return super.finishUsingItem(stack, pLevel, pLivingEntity);
    }
}
