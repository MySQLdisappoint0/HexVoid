package mys.hexvoid.casting.action;

import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import at.petrak.hexcasting.api.misc.MediaConstants;
import mys.hexvoid.HexvoidUtils;
import mys.hexvoid.damagesource.DamageSources;
import mys.hexvoid.datagen.DamageTypes;
import mys.hexvoid.item.tags.HexvoidTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OpChannelToVoid implements SpellAction {

    @Override
    public int getArgc() {
        return 0;
    }

    @Override
    public boolean hasCastingSound(@NotNull CastingEnvironment castingEnvironment) {
        return DefaultImpls.hasCastingSound(this, castingEnvironment);
    }

    @Override
    public boolean awardsCastingStat(@NotNull CastingEnvironment castingEnvironment) {
        return DefaultImpls.awardsCastingStat(this, castingEnvironment);
    }

    @Override
    public @NotNull Result execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        return new Result(new Spell(), MediaConstants.CRYSTAL_UNIT * 2, List.of(), 0);
    }

    @Override
    public @NotNull Result executeWithUserdata(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env, @NotNull CompoundTag userData) throws Mishap {
        return DefaultImpls.executeWithUserdata(this, args, env, userData);
    }

    @Override
    public @NotNull OperationResult operate(@NotNull CastingEnvironment env, @NotNull CastingImage image, @NotNull SpellContinuation continuation) {
        return DefaultImpls.operate(this, env, image, continuation);
    }

    private static class Spell implements RenderedSpell {

        @Override
        public void cast(@NotNull CastingEnvironment env) {
            if (env.getCastingEntity() instanceof ServerPlayer player) {
                if (player.getBlockY() - 1 == -64) {
                    if (player.level() instanceof ServerLevel serverLevel) {
                        if (serverLevel.getBlockState(player.getOnPos()).is(Blocks.BEDROCK)) {
                            serverLevel.destroyBlock(player.getOnPos(), false);
                            HexvoidUtils.awardAdvancement(player, serverLevel, HexvoidTags.ADV_DEEPWORLD, HexvoidTags.ADV_CRITERION_TRY_CHANNEL);
                        } else
                            player.displayClientMessage(Component.translatable("hexvoid.msg.spell.ctv.block"), true);
                    }
                } else player.displayClientMessage(Component.translatable("hexvoid.msg.spell.ctv.pos"), true);
            } else if (env.getCastingEntity() == null) {
                throw new MishapBadCaster();
            } else {
                env.getCastingEntity().die(DamageSources.buildDamage(env.getCastingEntity(), DamageTypes.CHANNEL_TO_VOID));
            }
        }

        @Override
        public @Nullable CastingImage cast(@NotNull CastingEnvironment env, @NotNull CastingImage image) {
            //Hexvoid.LOGGER.debug(image.toString());
            return DefaultImpls.cast(this, env, image);
        }
    }
}
