package mys.hexvoid.casting.vm;

import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv;
import mys.hexvoid.items.HexvoidItems;
import mys.hexvoid.items.MindStaffItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;

public class MindStaffEnv extends StaffCastEnv {
    public MindStaffEnv(ServerPlayer caster, InteractionHand castingHand) {
        super(caster, castingHand);
    }

    @Override
    public long extractMediaEnvironment(long cost, boolean simulate) {
        if (caster.isCreative()) return 0L;

        var staff = caster.getItemInHand(getCastingHand());

        if (staff.is(HexvoidItems.mind_staff.get()))
            return MindStaffItem.extractStoredMedia(staff, cost, simulate);
        else
            return cost;
    }
}
