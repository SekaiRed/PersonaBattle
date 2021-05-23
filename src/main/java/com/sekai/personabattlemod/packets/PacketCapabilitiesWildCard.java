package com.sekai.personabattlemod.packets;

import com.sekai.personabattlemod.capabilities.WildCardProvider;
import com.sekai.personabattlemod.battle.persona.impl.Persona;
import com.sekai.personabattlemod.battle.persona.impl.WildCard;
import com.sekai.personabattlemod.battle.persona.property.PersonaArcana;
import com.sekai.personabattlemod.battle.persona.property.PersonaStat;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCapabilitiesWildCard {
    public WildCard data;

    public PacketCapabilitiesWildCard(WildCard data) {
        this.data = data;
    }

    public static PacketCapabilitiesWildCard decode(PacketBuffer buf) {
        WildCard data = new WildCard();

        data.setLevel(buf.readInt());

        data.setMaxHP(buf.readInt());
        data.setMaxSP(buf.readInt());

        data.setHP(buf.readInt());
        data.setMaxSP(buf.readInt());

        data.setStockSize(buf.readInt());

        int pCount = buf.readInt();
        for(int i = 0; i < pCount; i++) {
            String name = buf.readString();
            String modelID = buf.readString();
            Persona p = new Persona(name, modelID);

            p.setLevel(buf.readInt());

            p.setArcana(PersonaArcana.values()[buf.readInt()]);

            int sCount = buf.readInt();
            for(int j = 0; j < sCount; j++)
                p.addSkill(buf.readString());

            for(int j = 0; j < PersonaStat.values().length; j++)
                p.addStat(PersonaStat.values()[i], buf.readInt());

            data.addPersona(p);
        }

        data.equipPersona(buf.readInt());

        return new PacketCapabilitiesWildCard(data);
    }

    public static void encode(PacketCapabilitiesWildCard msg, PacketBuffer buf) {
        buf.writeInt(msg.data.getLevel());

        buf.writeInt(msg.data.getMaxHP());
        buf.writeInt(msg.data.getMaxSP());

        buf.writeInt(msg.data.getHP());
        buf.writeInt(msg.data.getSP());

        buf.writeInt(msg.data.getStockSize());

        int pCount = msg.data.getPersonaCount();
        buf.writeInt(pCount);
        for(int i = 0; i < pCount; i++) {
            Persona p = msg.data.getPersona(i);

            buf.writeString(p.getName());
            buf.writeString(p.getModelID());
            buf.writeInt(p.getLevel());

            buf.writeInt(p.getArcana().ordinal());

            int sCount = p.getSkillCount();
            buf.writeInt(sCount);
            for(int j = 0; j < sCount; j++)
                buf.writeString(p.getSkill(j));

            for(int j = 0; j < PersonaStat.values().length; j++)
                buf.writeInt(p.getStat(PersonaStat.values()[i]));
        }

        buf.writeInt(msg.data.getPersonaIndex());
    }

    public static void handle(final PacketCapabilitiesWildCard pkt, Supplier<NetworkEvent.Context> ctx) {
        if(ctx.get().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
            Minecraft mc = Minecraft.getInstance();

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
            });
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

    //channel.registerMessage(0, MyPacket.class, MyPacket::encode, MyPacket::new, MyPacket::handle);
}