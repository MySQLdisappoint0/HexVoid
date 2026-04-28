package mys.hexvoid.mixin;

import at.petrak.hexcasting.api.mod.HexTags;
import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import at.petrak.hexcasting.common.lib.HexAttributes;
import mys.hexvoid.items.HexvoidItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSpellcasting.class)
public abstract class GuiSpellcastingMixin {

    @Final
    @Shadow(remap = false)
    private InteractionHand handOpenedWith;

    @Shadow(remap = false)
    public abstract void closeForReal();

    @Inject(method = "tick", at = @At("HEAD"), remap = false, cancellable = true)
    private void tick(CallbackInfo ci) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.getAttributeValue(HexAttributes.FEEBLE_MIND) > 0)
                this.closeForReal();
            else if (
                    !player.getItemInHand(this.handOpenedWith).is(HexvoidItems.mind_staff.get()) &
                            !player.getItemInHand(handOpenedWith).is(Items.STICK) &
                            !player.getItemInHand(handOpenedWith).is(HexTags.Items.STAVES)
            )
                this.closeForReal();
        }
        ci.cancel();
    }
}
