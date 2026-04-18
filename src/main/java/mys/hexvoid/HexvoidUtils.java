package mys.hexvoid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class HexvoidUtils {

    /**
     *
     * @param player       a player that need award advancement
     * @param level        the ServerLevel that player stayed
     * @param advancement  target advancement
     * @param advCriterion the criterion of target advancement
     * @return status code:
     * <table>
     *     <tr><td><code>-1</code></td><td><code>not found</code></td></tr>
     *     <tr><td><code>0</code></td><td><code>no error</code></td></tr>
     *     <tr><td><code>1</code></td><td><code>has awarded</code></td></tr>
     * </table>
     */
    public static int awardAdvancement(@NotNull ServerPlayer player, @NotNull ServerLevel level, ResourceLocation advancement, String advCriterion) {
        var adv = level.getServer().getAdvancements().getAdvancement(advancement);
        if (adv != null) return player.getAdvancements().award(adv, advCriterion) ? 0 : 1;
        else Hexvoid.LOGGER.error("couldn't get advancement", new NullPointerException());
        return -1;
    }
}
