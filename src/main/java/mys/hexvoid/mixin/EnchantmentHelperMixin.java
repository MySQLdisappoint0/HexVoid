package mys.hexvoid.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "storeEnchantment", at = @At("HEAD"), cancellable = true)
    private static void storeBigLimit(ResourceLocation id, int lvl, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", id.toString());
        tag.putInt("lvl", Math.max(0, Math.min(65536, lvl))); // 只保留 0~32767
        cir.setReturnValue(tag);
    }

    @Inject(method = "setEnchantmentLevel", at = @At("HEAD"), cancellable = true)
    private static void setBigLimit(CompoundTag tag, int lvl, CallbackInfo ci) {
        tag.putInt("lvl", Math.max(0, Math.min(65536, lvl)));
        ci.cancel();
    }

    @Inject(method = "getEnchantmentLevel(Lnet/minecraft/nbt/CompoundTag;)I", at = @At("HEAD"), cancellable = true)
    private static void readBigLimit(CompoundTag tag, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Math.max(0, Math.min(65536, tag.getInt("lvl"))));
    }
}