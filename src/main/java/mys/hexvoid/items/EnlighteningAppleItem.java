package mys.hexvoid.items;

import at.petrak.hexcasting.api.HexAPI;
import mys.hexvoid.damagesource.DamageSources;
import mys.hexvoid.datagen.DamageTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EnlighteningAppleItem extends Item {
    public EnlighteningAppleItem(Properties properties) {
        super(properties);
    }

    private static @NotNull Advancement getAdvancement(ServerLevel serverlevel, ResourceLocation location) {
        return Objects.requireNonNull(serverlevel.getServer().getAdvancements().getAdvancement(location));
    }

    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity consumer) {
        if (level instanceof ServerLevel serverlevel) {
            if (consumer instanceof ServerPlayer serverPlayer) {
                var playerAdvs = serverPlayer.getAdvancements();
                playerAdvs.award(getAdvancement(serverlevel, HexAPI.modLoc("root")), "has_charged_amethyst");
                playerAdvs.award(getAdvancement(serverlevel, HexAPI.modLoc("y_u_no_cast_angy")), "did_the_thing");
                playerAdvs.award(getAdvancement(serverlevel, HexAPI.modLoc("opened_eyes")), "health_used");
                playerAdvs.award(getAdvancement(serverlevel, HexAPI.modLoc("enlightenment")), "health_used");
            }
        }

        var mobEffect = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 140, 254, false, false, false);
        consumer.addEffect(mobEffect);

        mobEffect = new MobEffectInstance(MobEffects.JUMP, 140, 249, false, false, false);
        consumer.addEffect(mobEffect);

        mobEffect = new MobEffectInstance(MobEffects.BLINDNESS, 140, 254, false, false, false);
        consumer.addEffect(mobEffect);

        consumer.hurt(DamageSources.buildDamage(consumer, DamageTypes.OVER_KNOWLEDGE), 4.0f);

        var stackReturn = stack.copy();
        stackReturn.shrink(1);
        super.finishUsingItem(stack, level, consumer);
        return stackReturn;
    }
}
