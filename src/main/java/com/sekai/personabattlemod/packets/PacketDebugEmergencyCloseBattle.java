package com.sekai.personabattlemod.packets;

import com.sekai.personabattlemod.battle.BattleManager;
import com.sekai.personabattlemod.client.gui.MainBattleGui;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketDebugEmergencyCloseBattle {
    public UUID uniqueKey;

    public PacketDebugEmergencyCloseBattle(UUID uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public static PacketDebugEmergencyCloseBattle decode(PacketBuffer buf) {
        return new PacketDebugEmergencyCloseBattle(buf.readUniqueId());
    }

    public static void encode(PacketDebugEmergencyCloseBattle msg, PacketBuffer buf) {
        buf.writeUniqueId(msg.uniqueKey);
    }

    public static void handle(final PacketDebugEmergencyCloseBattle pkt, Supplier<NetworkEvent.Context> ctx) {
        if(ctx.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
            ctx.get().enqueueWork(() -> {
                BattleManager.instance.endBattle(pkt.uniqueKey);
                //Minecraft.getInstance().displayGuiScreen(new MainBattleGui(BattleManager.instance.getBattle(pkt.uniqueKey)));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
