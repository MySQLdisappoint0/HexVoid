package mys.hexvoid.mixin;

import at.petrak.hexcasting.api.casting.eval.ResolvedPattern;
import at.petrak.hexcasting.forge.xplat.ForgeXplatImpl;
import mys.hexvoid.item.tags.HexvoidTags;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ForgeXplatImpl.class)
public class ForgeXplatImplMixin {

    @Shadow(remap = false)
    @Final
    public static String TAG_PATTERNS;

    @Inject(method = "getPatternsSavedInUi", at = @At("HEAD"), remap = false, cancellable = true)
    private void getPatternsSavedInUi(ServerPlayer serverPlayer, CallbackInfoReturnable<List<ResolvedPattern>> cir) {
        if (serverPlayer.getPersistentData().contains(HexvoidTags.TAG_PATTERN_CLEARED)) {
            if (serverPlayer.getPersistentData().getList(TAG_PATTERNS, Tag.TAG_COMPOUND).isEmpty()) {
                serverPlayer.getPersistentData().remove(HexvoidTags.TAG_PATTERN_CLEARED);
            } else {
                serverPlayer.getPersistentData().remove(TAG_PATTERNS);
                serverPlayer.getPersistentData().remove(HexvoidTags.TAG_PATTERN_CLEARED);
            }
            cir.setReturnValue(new ArrayList<>(serverPlayer.getPersistentData().getList(TAG_PATTERNS, Tag.TAG_COMPOUND).size()));
        }
    }
}
