package mys.hexvoid.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class GlobalLootModifierGenerator extends GlobalLootModifierProvider {
    public GlobalLootModifierGenerator(PackOutput output, String modid) {
        super(output, modid);
    }

    @Override
    protected void start() {
        this.add(
                "lootable_woodland_mansion",
                new LootTableModifier(
                        new LootItemCondition[]{
                                LootTableIdCondition.builder(ResourceLocation.fromNamespaceAndPath("minecraft", "chests/woodland_mansion")).build(),
                                LootItemRandomChanceCondition.randomChance(0.10f).build() // 10% 概率
                        }
                )
        );
        this.add(
                "lootable_nether_bridge",
                new LootTableModifier(
                        new LootItemCondition[]{
                                LootTableIdCondition.builder(ResourceLocation.fromNamespaceAndPath("minecraft", "chests/nether_bridge")).build(),
                                LootItemRandomChanceCondition.randomChance(0.15f).build() // 15% 概率
                        }
                )
        );
        this.add(
                "lootable_desert_pyramid",
                new LootTableModifier(
                        new LootItemCondition[]{
                                LootTableIdCondition.builder(ResourceLocation.fromNamespaceAndPath("minecraft", "chests/desert_pyramid")).build(),
                                LootItemRandomChanceCondition.randomChance(0.01f).build() // 1% 概率
                        }
                )
        );
        this.add(
                "lootable_end_city_treasure",
                new LootTableModifier(
                        new LootItemCondition[]{
                                LootTableIdCondition.builder(ResourceLocation.fromNamespaceAndPath("minecraft", "chests/end_city_treasure")).build(),
                                LootItemRandomChanceCondition.randomChance(0.25f).build() // 25% 概率
                        }
                )
        );
        this.add(
                "lootable_stronghold_library",
                new LootTableModifier(
                        new LootItemCondition[]{
                                LootTableIdCondition.builder(ResourceLocation.fromNamespaceAndPath("minecraft", "chests/stronghold_library")).build(),
                                LootItemRandomChanceCondition.randomChance(0.05f).build() // 5% 概率
                        }
                )
        );
        this.add(
                "lootable_buried_treasure",
                new LootTableModifier(
                        new LootItemCondition[]{
                                LootTableIdCondition.builder(ResourceLocation.fromNamespaceAndPath("minecraft", "chests/buried_treasure")).build(),
                                LootItemRandomChanceCondition.randomChance(0.001f).build() // 0.1% 概率
                        }
                )
        );
        this.add(
                "lootable_igloo_chest",
                new LootTableModifier(
                        new LootItemCondition[]{
                                LootTableIdCondition.builder(ResourceLocation.fromNamespaceAndPath("minecraft", "chests/igloo_chest")).build(),
                                LootItemRandomChanceCondition.randomChance(0.001f).build() // 0.1% 概率
                        }
                )
        );
        this.add(
                "lootable_ancient_city",
                new LootTableModifier(
                        new LootItemCondition[]{
                                LootTableIdCondition.builder(ResourceLocation.fromNamespaceAndPath("minecraft", "chests/ancient_city")).build(),
                                LootItemRandomChanceCondition.randomChance(0.10f).build() // 10% 概率
                        }
                )
        );
        this.add(
                "lootable_ancient_city_ice_box",
                new LootTableModifier(
                        new LootItemCondition[]{
                                LootTableIdCondition.builder(ResourceLocation.fromNamespaceAndPath("minecraft", "chests/ancient_city_ice_box")).build(),
                                LootItemRandomChanceCondition.randomChance(0.05f).build() // 5% 概率
                        }
                )
        );
    }

}
