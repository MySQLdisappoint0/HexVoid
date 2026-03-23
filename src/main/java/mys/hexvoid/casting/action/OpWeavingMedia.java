package mys.hexvoid.casting.action;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity;
import at.petrak.hexcasting.api.misc.MediaConstants;
import mys.hexvoid.items.HexvoidItems;
import mys.hexvoid.items.MindStaffItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OpWeavingMedia implements SpellAction {
    @Override
    public int getArgc() {
        return 1;
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
    public @NotNull Result execute(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        Entity entity = OperatorUtils.getEntity(args, 0, getArgc());
        if (entity instanceof ItemEntity item && item.getItem().is(HexvoidItems.mind_staff.get())) {
            var stack = item.getItem();
            MindStaffItem.setCanStore(stack);
            item.setItem(stack);
        } else throw new MishapBadEntity(entity, Component.translatable("item.hexvoid.mind_staff"));
        return new Result(new Spell(), MediaConstants.CRYSTAL_UNIT * 10, List.of(), 0);
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
        public void cast(@NotNull CastingEnvironment castingEnvironment) {

        }

        @Override
        public @Nullable CastingImage cast(@NotNull CastingEnvironment env, @NotNull CastingImage image) {
            //Hexvoid.LOGGER.debug(image.toString());
            return DefaultImpls.cast(this, env, image);
        }
    }
}
