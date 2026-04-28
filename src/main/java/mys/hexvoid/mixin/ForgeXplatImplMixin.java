package mys.hexvoid.mixin;

import at.petrak.hexcasting.api.casting.eval.ResolvedPattern;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.forge.xplat.ForgeXplatImpl;
import mys.hexvoid.casting.vm.MindStaffEnv;
import mys.hexvoid.helper.ForgeXplatImplMixinHelper;
import mys.hexvoid.item.tags.HexvoidTags;
import mys.hexvoid.items.HexvoidItems;
import mys.hexvoid.items.MindStaffItem;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static at.petrak.hexcasting.forge.xplat.ForgeXplatImpl.TAG_VM;

@Mixin(ForgeXplatImpl.class)
public class ForgeXplatImplMixin implements ForgeXplatImplMixinHelper {

    @Shadow(remap = false)
    @Final
    public static String TAG_PATTERNS;
    @Unique
    private boolean showCost = false;

    @Unique
    public void setShowCost(boolean showCost) {
        this.showCost = showCost;
    }

    @Inject(method = "getPatternsSavedInUi", at = @At("HEAD"), remap = false, cancellable = true)
    private void getPatternsSavedInUi(ServerPlayer player, CallbackInfoReturnable<List<ResolvedPattern>> cir) {
        if (player.getPersistentData().contains(HexvoidTags.TAG_PATTERN_CLEARED)) {
            if (player.getPersistentData().getList(TAG_PATTERNS, Tag.TAG_COMPOUND).isEmpty()) {
                player.getPersistentData().remove(HexvoidTags.TAG_PATTERN_CLEARED);
            } else {
                player.getPersistentData().remove(TAG_PATTERNS);
                player.getPersistentData().remove(HexvoidTags.TAG_PATTERN_CLEARED);
            }
            cir.setReturnValue(new ArrayList<>(player.getPersistentData().getList(TAG_PATTERNS, Tag.TAG_COMPOUND).size()));
        }
    }

    @Inject(method = "getStaffcastVM", at = @At("HEAD"), remap = false, cancellable = true)
    private void getStaffcastVM(ServerPlayer player, InteractionHand hand, CallbackInfoReturnable<CastingVM> cir) {
        var staff = player.getItemInHand(hand);
        if (staff.is(HexvoidItems.mind_staff.get()) && MindStaffItem.canStore(staff)) {
            cir.setReturnValue(new CastingVM(
                    CastingImage.loadFromNbt(
                            player.getPersistentData().getCompound(TAG_VM),
                            player.serverLevel()
                    ),
                    new MindStaffEnv(player, hand, showCost)));
        }
    }
}
