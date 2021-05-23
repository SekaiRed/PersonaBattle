package com.sekai.personabattlemod.util;

import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.packets.PacketCapabilitiesWildCard;
import com.sekai.personabattlemod.packets.PacketClientInitBattle;
import com.sekai.personabattlemod.packets.PacketDebug;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel NET = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PersonaBattle.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register()
    {
        //NET_CHANNEL.registerMessage();
        NET.registerMessage(0, PacketCapabilitiesWildCard.class, PacketCapabilitiesWildCard::encode, PacketCapabilitiesWildCard::decode, PacketCapabilitiesWildCard::handle);
        NET.registerMessage(1, PacketDebug.class, PacketDebug::encode, PacketDebug::decode, PacketDebug::handle);
        NET.registerMessage(2, PacketClientInitBattle.class, PacketClientInitBattle::encode, PacketClientInitBattle::decode, PacketClientInitBattle::handle);
    }

    //SimpleChannel.registerMessage(PacketCapabilitiesSp.Handler.class, PacketCapabilitiesSp.class, 0, Side.CLIENT);
}
