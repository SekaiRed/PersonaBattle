package com.sekai.personabattlemod;

import com.sekai.personabattlemod.capabilities.WildCardStorage;
import com.sekai.personabattlemod.client.entity.render.ShadowEntityRender;
import com.sekai.personabattlemod.client.gui.BetaProfileGui;
import com.sekai.personabattlemod.client.gui.BetaSinglePersonaGui;
import com.sekai.personabattlemod.client.gui.GuiOverlay;
import com.sekai.personabattlemod.config.PersonaConfig;
import com.sekai.personabattlemod.events.ThirdEyeEvent;
import com.sekai.personabattlemod.items.ItemLogo;
import com.sekai.personabattlemod.move.MoveDatabase;
import com.sekai.personabattlemod.persona.impl.WildCard;
import com.sekai.personabattlemod.util.CapabilityHandler;
import com.sekai.personabattlemod.util.EventHandler;
import com.sekai.personabattlemod.util.PacketHandler;
import com.sekai.personabattlemod.util.RegistryHandler;
import javafx.scene.input.KeyCode;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("p5battle")
public class PersonaBattle
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "p5battle";
    public static final int MAX_SKILL = 8;
    public static KeyBinding keyProfile;
    public static KeyBinding keyThirdEye;

    public PersonaBattle() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PersonaConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PersonaConfig.SERVER_SPEC);

        RegistryHandler.init();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        PacketHandler.register();
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
        CapabilityManager.INSTANCE.register(WildCard.class, new WildCardStorage(), () -> new WildCard());
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        keyProfile = new KeyBinding("Player Profile", KeyCode.I.impl_getCode(), "Persona 5 Battle Mod");
        ClientRegistry.registerKeyBinding(keyProfile);
        keyThirdEye = new KeyBinding("Third Eye", KeyCode.O.impl_getCode(), "Persona 5 Battle Mod");
        ClientRegistry.registerKeyBinding(keyThirdEye);
        MoveDatabase.init();
        //Events
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(new ThirdEyeEvent());
        //Guis
        MinecraftForge.EVENT_BUS.register(new BetaProfileGui());
        MinecraftForge.EVENT_BUS.register(new BetaSinglePersonaGui());
        RenderingRegistry.registerEntityRenderingHandler(RegistryHandler.SHADOW.get(), ShadowEntityRender::new);
        MinecraftForge.EVENT_BUS.register(new GuiOverlay());
    }

    /*private void serverLoad(final FMLServerStartingEvent event) {
        CommandDispatcher<CommandSource> commandDispatcher = event.getCommandDispatcher();
        CommandGiveSkill.register(commandDispatcher);
        //MBEquoteCommand.register(commandDispatcher);
    }*/

    public static final ItemGroup TAB = new ItemGroup("p5battleTab") {
        @Override
        public ItemStack createIcon() {
            //return new ItemStack(ItemLogo::new);
            return new ItemStack(RegistryHandler.LOGO.get());
            //return new ItemStack(RegistryHandler.RUBY.get());
        }
    };
}
