package com.sekai.personabattlemod.util;

import com.mojang.brigadier.CommandDispatcher;
import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.capabilities.WildCardProvider;
import com.sekai.personabattlemod.client.gui.BetaProfileGui;
import com.sekai.personabattlemod.commands.CommandSkill;
import com.sekai.personabattlemod.packets.PacketCapabilitiesWildCard;
import com.sekai.personabattlemod.battle.persona.impl.Persona;
import com.sekai.personabattlemod.battle.persona.impl.WildCard;
import com.sekai.personabattlemod.battle.persona.property.PersonaArcana;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Random;

public class EventHandler {
    /*@SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {

    }*/
    @SubscribeEvent
    public void serverLoad(FMLServerStartingEvent event) {
        CommandDispatcher<CommandSource> commandDispatcher = event.getCommandDispatcher();
        CommandSkill.register(commandDispatcher);
        //MBEquoteCommand.register(commandDispatcher);
    }

    @SubscribeEvent
    public void clientTickEvent(TickEvent.ClientTickEvent event) {
        if (PersonaBattle.keyProfile.isKeyDown())
        {
            Minecraft.getInstance().displayGuiScreen(new BetaProfileGui());
            //Minecraft.getInstance().addScheduledTask(() -> {Minecraft.getInstance().displayGuiScreen(new BetaProfileGui());});
            //event.
            //Minecraft.getInstance().
        }
        if (PersonaBattle.keyThirdEye.isKeyDown())
        {

        }
    }

    @SubscribeEvent
    public void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
        //PacketHandler.NET.send(PacketDistributor.PLAYER.noArg(), new PacketDebug(4));

        System.out.println("fuck you" + event.getPlayer().world.isRemote);

        if(event.getPlayer().world.isRemote())
            return;

        PlayerEntity player = event.getPlayer();

        WildCard wc = player.getCapability(WildCardProvider.WC_CAP, null).orElse(null);

        if(wc != null) {
            wc.setLevel(1 + (new Random()).nextInt(9));

            wc.setMaxHP(80);
            wc.setMaxSP(20);
            wc.setHP(80);
            wc.setSP(20);

            wc.clearPersonas();

            Persona p = new Persona("Astolfo", "p5battle:shadow");
            p.setLevel(wc.getLevel() + (new Random()).nextInt(2) - (new Random()).nextInt(2));
            p.automaticStatDistribution();
            p.addSkill("p5battle:agi");
            p.addSkill("p5battle:lunge");
            p.addSkill("p5battle:tarukaja");
            p.setArcana(PersonaArcana.MAGICIAN);
            wc.addPersona(p);

            Persona p2 = new Persona("Arsene", "minecraft:skeleton");
            p2.setLevel(wc.getLevel() + (new Random()).nextInt(8) - (new Random()).nextInt(3));
            p2.automaticStatDistribution();
            p2.addSkill("p5battle:eiha");
            p2.addSkill("p5battle:void");
            p2.setArcana(PersonaArcana.FOOL);
            wc.addPersona(p2);

            Persona p3 = new Persona("Wither", "minecraft:wither");
            p3.setLevel(wc.getLevel() + (new Random()).nextInt(20) - (new Random()).nextInt(5));
            p3.automaticStatDistribution();
            p3.addSkill("p5battle:decadyne");
            p3.addSkill("p5battle:witherblast");
            p3.addSkill("p5battle:sprayshot");
            p3.addSkill("p5battle:makajama");
            p3.setArcana(PersonaArcana.DEATH);
            wc.addPersona(p3);

            Persona p4 = new Persona("Spider", "minecraft:spider");
            p4.setLevel(wc.getLevel() + (new Random()).nextInt(2));
            p4.automaticStatDistribution();
            p4.addSkill("p5battle:lunge");
            p4.addSkill("p5battle:tarukaja");
            p4.setArcana(PersonaArcana.CHARIOT);
            wc.addPersona(p4);

            Persona p5 = new Persona("Enderman", "minecraft:enderman");
            p5.setLevel(wc.getLevel() + (new Random()).nextInt(5));
            p5.automaticStatDistribution();
            p5.addSkill("p5battle:void");
            p5.addSkill("p5battle:megido");
            p5.setArcana(PersonaArcana.MOON);
            wc.addPersona(p5);

            wc.equipPersona(0);

            player.sendMessage(new StringTextComponent("level : " + wc.getLevel()));

            PacketHandler.NET.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new PacketCapabilitiesWildCard(wc));
        }

        //PacketHandler.NET.sendTo(new PacketDebug(4), event.getPlayer(), NetworkDirection.PLAY_TO_CLIENT);
        //PacketHandler.NET.send(PacketDistributor.PLAYER.with((Supplier<ServerPlayerEntity>(event.getPlayer()))), new PacketDebug(4));

        /*WildCard wc = event.getPlayer().getCapability(WildCardProvider.WC_CAP, null).orElse(null);

        Persona p = new Persona("Arsene");
        p.addSkill("p5battle:agi");
        wc.addPersona(p);

        if(wc != null)
        {
            wc.setLevel(1 + (new Random()).nextInt(9));

            //PacketHandler.NET.send(PacketDistributor.ALL.noArg(), new PacketCapabilitiesWildCard(wc));

            //event.getPlayer().sendMessage(new TextComponentString("You're level " + stat.getLevel() + "."));
            //Main.Network.sendTo(new PacketCapabilitiesStats((Stats) stat), (EntityPlayerMP) event.player);
        }*/
    }

    @SubscribeEvent
    public void onPlayerFall(LivingFallEvent event)
    {
        Entity entity = event.getEntity();

        if (entity.world.isRemote || !(entity instanceof PlayerEntity) || event.getDistance() < 3)
            return;

        //PacketHandler.NET.send(PacketDistributor.ALL.noArg(), new PacketDebug(4));

        PlayerEntity player = (PlayerEntity) entity;

        WildCard wc = player.getCapability(WildCardProvider.WC_CAP, null).orElse(null);

        wc.setLevel(wc.getLevel() + 1);

        //PacketHandler.NET.send(PacketDistributor.PLAYER.noArg(), new PacketCapabilitiesWildCard(wc));
        //PacketHandler.NET.sendTo(new PacketCapabilitiesWildCard(wc), ((ServerPlayerEntity) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        PacketHandler.NET.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new PacketCapabilitiesWildCard(wc));

        player.sendMessage(new StringTextComponent("level : " + wc.getLevel()));

        //ARPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), 64, world.getDimension().getType())), new ItemMessage(inventory.getStackInSlot(0), pos));
    }

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        //this is only for copying capabilities
        if (event.getPlayer().getEntityWorld().isRemote) {
            return;
        }

        WildCard wcNew = event.getPlayer().getCapability(WildCardProvider.WC_CAP, null).orElse(null);
        WildCard wcOld = event.getOriginal().getCapability(WildCardProvider.WC_CAP, null).orElse(null);

        if (wcOld == null) {
            return;
        }

        if (wcNew == null) {
            throw new NullPointerException("missing player capability during clone");
        }

        wcNew.clone(wcOld);
    }

    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        WildCard wc = event.getPlayer().getCapability(WildCardProvider.WC_CAP, null).orElse(null);
        PacketHandler.NET.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new PacketCapabilitiesWildCard(wc));
    }

    @SubscribeEvent
    public void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        //todo need more checks in here, can't just send a null wc
        WildCard wc = event.getPlayer().getCapability(WildCardProvider.WC_CAP, null).orElse(null);
        PacketHandler.NET.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new PacketCapabilitiesWildCard(wc));
    }

    @SubscribeEvent
    public void onFogDensityEvent(EntityViewRenderEvent.FogDensity event) {
        /*if (event.get.isInsideOfMaterial(ModMaterials.MOLTEN_GLASS)) {
            GlStateManager.setFog(EXP);
            event.setDensity(2F);
        }
        else {
            GlStateManager.setFog(LINEAR);
            event.setDensity(0.01F);
        }
        event.setCanceled(true);*/
        /*if (PersonaBattle.keyThirdEye.isKeyDown())
        {
            //GlStateManager.
            GL11.glFogi(GL11.GL_FOG_MODE, GlStateManager.FogMode.LINEAR.field_187351_d);
            GL11.glFogf(GL11.GL_FOG_START, -20f);
            GL11.glFogf(GL11.GL_FOG_END, 20f);
            event.setDensity(0.2F);
            event.setCanceled(true);
        }*/
    }

    @SubscribeEvent
    public void onFogColorsEvent(EntityViewRenderEvent.FogColors event) {
        /*if (PersonaBattle.keyThirdEye.isKeyDown()) {
            event.setRed(event.getRed()/4f);
            event.setGreen(event.getGreen()/4f);
            event.setBlue(event.getBlue()/4f);
            //event.setCanceled(true);
        }*/
    }
}