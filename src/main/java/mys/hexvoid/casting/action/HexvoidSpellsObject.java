package mys.hexvoid.casting.action;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import mys.hexvoid.HexvoidSpellsRegistry;

public record HexvoidSpellsObject(String name, String signature, HexDir dir, Action spellAction) {
    public static ActionRegistryEntry register(String name, String signature, HexDir dir, Action spellAction) {
        return HexvoidSpellsRegistry.wrap(name, HexPattern.fromAngles(signature, dir), spellAction);
    }
}
