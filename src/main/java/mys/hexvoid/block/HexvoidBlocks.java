package mys.hexvoid.block;

import mys.hexvoid.Hexvoid;
import mys.hexvoid.items.HexvoidItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class HexvoidBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Hexvoid.MODID);

    public static ArrayList<RegistryObject<Block>> RegisteredBlocks = new ArrayList<>();

    public static final RegistryObject<Block> test_block = registerBlock("test_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));


    public static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
        RegistryObject<Block> toReturn = BLOCKS.register(name, block);
        RegisteredBlocks.add(toReturn);
        HexvoidItems.RegisteredItems.add(registerBlockItem(name, toReturn));
        return toReturn;
    }

    public static RegistryObject<Item> registerBlockItem(String name, RegistryObject<Block> block) {
        return HexvoidItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
