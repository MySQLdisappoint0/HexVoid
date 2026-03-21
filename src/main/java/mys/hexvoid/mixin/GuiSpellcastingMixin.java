package mys.hexvoid.mixin;

import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import at.petrak.hexcasting.common.lib.HexAttributes;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSpellcasting.class)
public abstract class GuiSpellcastingMixin {

    @Shadow(remap = false)
    public abstract void closeForReal();

    @Inject(method = "tick", at = @At("HEAD"), remap = false, cancellable = true)
    private void tick(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            if (mc.player.getAttributeValue(HexAttributes.FEEBLE_MIND) > 0) {
                this.closeForReal();
            }
        }
        ci.cancel();
    }
}
