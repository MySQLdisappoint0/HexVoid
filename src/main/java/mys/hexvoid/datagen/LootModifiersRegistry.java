package mys.hexvoid.datagen;

import com.mojang.serialization.Codec;
import mys.hexvoid.Hexvoid;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("SpellCheckingInspection")
public class LootModifiersRegistry {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Hexvoid.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADDITEM =
            LOOT_MODIFIER_SERIALIZERS.register(
                    "additem",
                    LootTableModifier.CODEC
            );

    public static void register(IEventBus bus) {
        LOOT_MODIFIER_SERIALIZERS.register(bus);
    }
}