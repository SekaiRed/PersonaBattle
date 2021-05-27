package com.sekai.personabattlemod.packets;

import com.sekai.personabattlemod.battle.BattleManager;
import com.sekai.personabattlemod.battle.persona.impl.Persona;
import com.sekai.personabattlemod.battle.persona.impl.WildCard;
import com.sekai.personabattlemod.battle.persona.property.PersonaArcana;
import com.sekai.personabattlemod.battle.persona.property.PersonaStat;
import com.sekai.personabattlemod.capabilities.WildCardProvider;
import com.sekai.personabattlemod.client.gui.BetaProfileGui;
import com.sekai.personabattlemod.client.gui.MainBattleGui;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketClientInitBattle {
    //public BattleManager.BattleInstance battle;
    public UUID uniqueKey;

    public PacketClientInitBattle(BattleManager.BattleInstance battle) {
        //this.battle = battle;
        uniqueKey = battle.getUniqueKey();
    }

    public PacketClientInitBattle(UUID uniqueKey) {
        //this.battle = battle;
        this.uniqueKey = uniqueKey;
    }

    public static PacketClientInitBattle decode(PacketBuffer buf) {
        /*WildCard data = new WildCard();

        data.setLevel(buf.readInt());

        return new PacketCapabilitiesWildCard(data);*/
        return new PacketClientInitBattle(buf.readUniqueId());
    }

    public static void encode(PacketClientInitBattle msg, PacketBuffer buf) {
        buf.writeUniqueId(msg.uniqueKey);
    }

    public static void handle(final PacketClientInitBattle pkt, Supplier<NetworkEvent.Context> ctx) {
        if(ctx.get().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
            ctx.get().enqueueWork(() -> {
                /*System.out.println(ctx.get().getDirection().toString());
                PlayerEntity player = Minecraft.getInstance().player;
                //PlayerEntity player = ctx.get().getSender();
                WildCard wc = player.getCapability(WildCardProvider.WC_CAP, null).orElse(null);

                if (wc != null) {
                    wc.setLevel(pkt.data.getLevel());
                    wc.setMaxHP(pkt.data.getMaxHP());
                    wc.setMaxSP(pkt.data.getMaxSP());
                    wc.setHP(pkt.data.getHP());
                    wc.setSP(pkt.data.getSP());

                    wc.setStockSize(pkt.data.getStockSize());

                    int pCount = pkt.data.getPersonaCount();
                    for (int i = 0; i < pCount; i++) {
                        wc.addPersona(pkt.data.getPersona(i));
                    }

                    wc.equipPersona(pkt.data.getPersonaIndex());
                }*/

                //Minecraft.getInstance().ingameGUI.displayTitle("New battle started!", null, 50, 300, 50);
                //Minecraft.getInstance().ingameGUI.displayTitle(null, "With UUID\n" + pkt.uniqueKey.toString(), 50, 300, 50);
                Minecraft.getInstance().displayGuiScreen(new MainBattleGui());
            });
            ctx.get().setPacketHandled(true);
            /*Minecraft mc = Minecraft.getInstance();

            if (mc.world == null)
                return;

            if (!mc.world.isRemote())
                return;

            mc.player.getCapability(WildCardProvider.WC_CAP).ifPresent(wc -> {
                wc.setLevel(pkt.data.getLevel());
                wc.setMaxHP(pkt.data.getMaxHP());
                wc.setMaxSP(pkt.data.getMaxSP());
                wc.setHP(pkt.data.getHP());
                wc.setSP(pkt.data.getSP());

                wc.setStockSize(pkt.data.getStockSize());

                wc.clearPersonas();
                int pCount = pkt.data.getPersonaCount();
                for (int i = 0; i < pCount; i++) {
                    wc.addPersona(pkt.data.getPersona(i));
                }

                wc.equipPersona(pkt.data.getPersonaIndex());
            });*/
        }
        /*if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ctx.get().enqueueWork(() -> {
                System.out.println(ctx.get().getDirection().toString());
                PlayerEntity player = Minecraft.getInstance().player;
                //PlayerEntity player = ctx.get().getSender();
                WildCard wc = player.getCapability(WildCardProvider.WC_CAP, null).orElse(null);

                if (wc != null) {
                    wc.setLevel(pkt.data.getLevel());
                    wc.setMaxHP(pkt.data.getMaxHP());
                    wc.setMaxSP(pkt.data.getMaxSP());
                    wc.setHP(pkt.data.getHP());
                    wc.setSP(pkt.data.getSP());

                    wc.setStockSize(pkt.data.getStockSize());

                    int pCount = pkt.data.getPersonaCount();
                    for (int i = 0; i < pCount; i++) {
                        wc.addPersona(pkt.data.getPersona(i));
                    }

                    wc.equipPersona(pkt.data.getPersonaIndex());
                }
            });
            ctx.get().setPacketHandled(true);
        } else if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ctx.get().enqueueWork(() -> {
                System.out.println(ctx.get().getDirection().toString());
                PlayerEntity player = Minecraft.getInstance().player;
                WildCard wc = player.getCapability(WildCardProvider.WC_CAP, null).orElse(null);

                if (wc != null) {
                    wc.setLevel(pkt.data.getLevel());
                    wc.setMaxHP(pkt.data.getMaxHP());
                    wc.setMaxSP(pkt.data.getMaxSP());
                    wc.setHP(pkt.data.getHP());
                    wc.setSP(pkt.data.getSP());

                    wc.setStockSize(pkt.data.getStockSize());

                    int pCount = pkt.data.getPersonaCount();
                    for (int i = 0; i < pCount; i++) {
                        wc.addPersona(pkt.data.getPersona(i));
                    }

                    wc.equipPersona(pkt.data.getPersonaIndex());
                }
            });
            ctx.get().setPacketHandled(true);
        }*/
    }
}
