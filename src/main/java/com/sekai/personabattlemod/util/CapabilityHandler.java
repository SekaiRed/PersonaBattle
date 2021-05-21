package com.sekai.personabattlemod.util;

import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.capabilities.WildCardProvider;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.entity.player.PlayerEntity;

public class CapabilityHandler {
    public static final ResourceLocation WC_CAP = new ResourceLocation(PersonaBattle.MOD_ID, "wc");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if ((event.getObject() instanceof PlayerEntity))
        {
            event.addCapability(WC_CAP, new WildCardProvider());
            return;
        }

		/*if ((event.getObject() instanceof EntityLivingBase))
		{
			event.addCapability(STAT_CAP, new StatsProvider());
		}*/
    }
}
