package mys.hexvoid.datagen;

import at.petrak.hexcasting.common.lib.HexItems;
import mys.hexvoid.item.tags.HexvoidTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {

    public static ArrayList<Advancement> advancements = new ArrayList<>();

    @Override
    public void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<Advancement> saver, @NotNull ExistingFileHelper existingFileHelper) {
        // lore/research (root)
        var research = Advancement.Builder.advancement()
                .display(new DisplayInfo(HexItems.LORE_FRAGMENT.getDefaultInstance(),
                                Component.translatable("advancement.hexvoid:lore/research"),
                                Component.translatable("advancement.hexvoid:lore/research.desc"),
                                ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/brain_coral_block.png"),
                                FrameType.TASK,
                                true,
                                true,
                                false
                        )
                )
                .addCriterion(HexvoidTags.ADV_CRITERION_GRANT, new Criterion(new ImpossibleTrigger.TriggerInstance()))
                .save(saver, HexvoidTags.ADV_LORE_RESEARCH, existingFileHelper);
        advancements.add(research);

        // channel_to_void
        var channel_to_world = Advancement.Builder.advancement()
                .parent(research)
                .display(new DisplayInfo(
                                Items.BEDROCK.getDefaultInstance(),
                                Component.translatable("advancement.hexvoid:root"),
                                Component.translatable("advancement.hexvoid:root.desc"),
                                null,
                                FrameType.TASK,
                                true,
                                true,
                                true
                        )
                )
                .addCriterion(HexvoidTags.ADV_CRITERION_TRY_CHANNEL, new Criterion(new EntityHurtPlayerTrigger.TriggerInstance(
                        ContextAwarePredicate.ANY,
                        new DamagePredicate(
                                MinMaxBounds.Doubles.ANY,
                                MinMaxBounds.Doubles.ANY,
                                EntityPredicate.ANY,
                                false,
                                new DamageSourcePredicate(
                                        List.of(TagPredicate.is(new TagKey<>(
                                                Registries.DAMAGE_TYPE,
                                                DamageTypes.CHANNEL_TO_VOID.location()
                                        ))),
                                        EntityPredicate.ANY,
                                        EntityPredicate.ANY
                                )
                        )
                )))
                .save(saver, HexvoidTags.ADV_DEEPWORLD, existingFileHelper);
        advancements.add(channel_to_world);

        // find broken
        var find_broken = Advancement.Builder.advancement()
                .parent(research)
                .display(new DisplayInfo(
                        Items.COBWEB.getDefaultInstance(),
                        Component.translatable("advancement.hexvoid:findbroken"),
                        Component.translatable("advancement.hexvoid:findbroken.desc"),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        true
                ))
                .addCriterion(HexvoidTags.ADV_CRITERION_FIND_BROKEN, new Criterion(new ImpossibleTrigger.TriggerInstance()))
                .save(saver, HexvoidTags.ADV_FIND_BROKEN, existingFileHelper);
        advancements.add(find_broken);

        // exchange life
        var exchange_life = Advancement.Builder.advancement()
                .parent(research)
                .display(new DisplayInfo(
                        Items.BRAIN_CORAL_BLOCK.getDefaultInstance(),
                        Component.translatable("advancement.hexvoid:exchangelife"),
                        Component.translatable("advancement.hexvoid:exchangelife.desc"),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        true
                ))
                .addCriterion(HexvoidTags.ADV_CRITERION_EXCHANGE_LIFE, new Criterion(new ImpossibleTrigger.TriggerInstance()))
                .save(saver, HexvoidTags.ADV_EXCHANGE_LIFE, existingFileHelper);
        advancements.add(exchange_life);
    }
}
