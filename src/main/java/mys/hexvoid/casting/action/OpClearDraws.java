package mys.hexvoid.casting.action;

import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import at.petrak.hexcasting.common.msgs.MsgOpenSpellGuiS2C;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import kotlin.Pair;
import mys.hexvoid.item.tags.HexvoidTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static at.petrak.hexcasting.forge.xplat.ForgeXplatImpl.TAG_PATTERNS;

public class OpClearDraws implements SpellAction {

    @Override
    public int getArgc() {
        return 0;
    }

    @Override
    public boolean hasCastingSound(@NotNull CastingEnvironment castingEnvironment) {
        return true;
    }

    @Override
    public boolean awardsCastingStat(@NotNull CastingEnvironment castingEnvironment) {
        return DefaultImpls.awardsCastingStat(this, castingEnvironment);
    }

    @Override
    public @NotNull Result execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) {
        return new Result(new Spell(), 0, List.of(), 0);
    }

    @Override
    public @NotNull Result executeWithUserdata(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment, @NotNull CompoundTag compoundTag) {
        return DefaultImpls.executeWithUserdata(this, list, castingEnvironment, compoundTag);
    }

    @Override
    public @NotNull OperationResult operate(@NotNull CastingEnvironment castingEnvironment, @NotNull CastingImage castingImage, @NotNull SpellContinuation spellContinuation) {
        if (!(castingEnvironment instanceof StaffCastEnv)) throw new MishapBadCaster();
        return DefaultImpls.operate(this, castingEnvironment, castingImage, spellContinuation);
    }

    private static class Spell implements RenderedSpell {
        @Override
        public void cast(@NotNull CastingEnvironment castingEnvironment) {
            if (castingEnvironment.getCastingEntity() instanceof ServerPlayer serverPlayer) {
                var hand = castingEnvironment.getCastingHand();
                CastingVM vm = IXplatAbstractions.INSTANCE.getStaffcastVM(serverPlayer, hand);
                Pair<List<CompoundTag>, CompoundTag> descs = vm.generateDescs();
                IXplatAbstractions.INSTANCE.sendPacketToPlayer(serverPlayer, new MsgOpenSpellGuiS2C(hand, new ArrayList<>(serverPlayer.getPersistentData().getList(TAG_PATTERNS, Tag.TAG_COMPOUND).size()), descs.getFirst(), descs.getSecond(), 0));
                serverPlayer.getPersistentData().put(HexvoidTags.TAG_PATTERN_CLEARED, new CompoundTag());
            }
        }

        @Override
        public @Nullable CastingImage cast(@NotNull CastingEnvironment castingEnvironment, @NotNull CastingImage castingImage) {
            return DefaultImpls.cast(this, castingEnvironment, castingImage);
        }
    }
}
