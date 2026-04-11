package mys.hexvoid.forge;

import mys.hexvoid.Hexvoid;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Hexvoid.MOD_ID)
public final class HexvoidForge {
    public HexvoidForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(Hexvoid.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        Hexvoid.init();
    }
}
