package mys.hexvoid.fabric;

import mys.hexvoid.Hexvoid;
import net.fabricmc.api.ModInitializer;

public final class HexvoidFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run our common setup.
        Hexvoid.init();
    }
}
