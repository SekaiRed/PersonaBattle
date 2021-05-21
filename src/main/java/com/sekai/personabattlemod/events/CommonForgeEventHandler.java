package com.sekai.personabattlemod.events;

import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.util.RegistryHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= PersonaBattle.MOD_ID, bus= Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEventHandler
{
    public static final ResourceLocation DIMENSION_TYPE_M = new ResourceLocation(PersonaBattle.MOD_ID, RegistryHandler.MEMENTOS.getId().getPath());

    @SubscribeEvent
    public static void onRegisterDimensionsEvent(RegisterDimensionsEvent event)
    {
        // the first argument here is a resource location for your dimension type
        // the second argument here is the ModDimension that your DimensionType uses
        // the third argument here is an optional PacketBuffer with extra data you want to pass
        // to your DimensionType, which is in turn passed to your Dimension
        // which allows you to define properties of your Dimension when you register this
        // the fourth argument determines skylight for some reason
        // we'll also need to add a check to prevent the dimension from being registered more than once
        if (DimensionType.byName(DIMENSION_TYPE_M) == null)
        {
            DimensionManager.registerDimension(DIMENSION_TYPE_M, RegistryHandler.MEMENTOS.get(), null, true);
        }

        // this returns a DimensionType for your ResourceLocation; alternatively you can also retrieve the dimensiontype with
        // DimensionType.byName(ResourceLocation)
    }
}