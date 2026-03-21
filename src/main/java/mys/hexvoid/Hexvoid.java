package mys.hexvoid;

import at.petrak.hexcasting.common.lib.HexCreativeTabs;
import at.petrak.hexcasting.common.lib.HexRegistries;
import com.mojang.logging.LogUtils;
import mys.hexvoid.block.HexvoidBlocks;
import mys.hexvoid.items.HexvoidItems;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(Hexvoid.MODID)
public class Hexvoid {
    public static final String MODID = "hexvoid";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Hexvoid(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        HexvoidItems.register(modEventBus);
        HexvoidBlocks.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::onRegister);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, (LivingDamageEvent event) -> {

        });
    }

    public static ResourceLocation ModLoc(String path) {
        var patchedPath = path.toLowerCase();
        if (ResourceLocation.isValidPath(patchedPath)) {
            return ResourceLocation.fromNamespaceAndPath(MODID, patchedPath);
        } else {
            throw new IllegalArgumentException("path is not valid: " + patchedPath);
        }
    }

    public void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(HexRegistries.ACTION)) {
            // We register our spells
            HexvoidSpellsRegistry.registerActions();
            LOGGER.info("Hexcasting is in the voooooooid!");
            LOGGER.info("hexvoid has registered patterns.");
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == HexCreativeTabs.HEX) {
            // items
            event.accept(HexvoidItems.test_item);
            event.accept(HexvoidItems.enlightening_apple);
            event.accept(HexvoidItems.lore_fragment);
            event.accept(HexvoidItems.mind_staff);

            // block items
            event.accept(HexvoidBlocks.test_block.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // we do nothing when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // we have no client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    public static class RuntimeData {
        private static boolean FROZEN = false;

        public static boolean isFrozen() {
            return FROZEN;
        }

        public static void setFrozen(boolean isFrozen) {
            FROZEN = isFrozen;
        }
    }
}
