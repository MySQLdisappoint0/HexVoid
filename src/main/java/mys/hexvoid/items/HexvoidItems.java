package mys.hexvoid.items;

import mys.hexvoid.Hexvoid;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class HexvoidItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Hexvoid.MODID);
    public static final RegistryObject<Item> test_item = wrap("test_item", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> enlightening_apple = wrap("enlightening_apple", () ->
            new EnlighteningAppleItem(
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
                            .food(
                                    new FoodProperties.Builder()
                                            .nutrition(1)
                                            .saturationMod(0.0F)
                                            .alwaysEat()
                                            .build()
                            )
            )
    );
    public static final RegistryObject<Item> lore_fragment = wrap("lore_fragment", () ->
            new LoreFragmentItem(
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.RARE)
            )
    );

    public static final RegistryObject<Item> mind_staff = wrap("mind_staff", () ->
            new MindStaffItem(
                    new Item.Properties()
                            .stacksTo(1)
                            .defaultDurability(1)
            )
    );

    private static RegistryObject<Item> wrap(String regName, Supplier<Item> supplier) {
        return ITEMS.register(regName, supplier);
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
