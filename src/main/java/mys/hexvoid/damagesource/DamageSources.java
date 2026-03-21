package mys.hexvoid.damagesource;

import mys.hexvoid.Hexvoid;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

import java.io.IOException;

public class DamageSources {
    public static DamageSource buildDamage(LivingEntity entity, ResourceKey<DamageType> damageTypeResourceKey) {
        try (var level = entity.level()) {
            Registry<DamageType> damageTypeRegistry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
            Holder.Reference<DamageType> damageTypeReference = damageTypeRegistry.getHolderOrThrow(damageTypeResourceKey);
            return new DamageSource(damageTypeReference);
        } catch (IOException e) {
            Hexvoid.LOGGER.error("cannot get level", e);
            throw new RuntimeException();
        }
    }
}
