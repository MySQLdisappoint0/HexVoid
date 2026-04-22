package mys.hexvoid.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract CompoundTag getOrCreateTag();

    @Inject(method = "enchant", at = @At("HEAD"), cancellable = true)
    private void enchantBigLimit(Enchantment enchantment, int level, CallbackInfo ci) {
        CompoundTag tag = this.getOrCreateTag();

        if (!tag.contains("Enchantments", Tag.TAG_LIST)) {
            tag.put("Enchantments", new ListTag());
        }

        ListTag list = tag.getList("Enchantments", Tag.TAG_COMPOUND);
        list.add(EnchantmentHelper.storeEnchantment(
                EnchantmentHelper.getEnchantmentId(enchantment),
                Math.max(0, Math.min(65536, level))
        ));
        ci.cancel();
    }
}