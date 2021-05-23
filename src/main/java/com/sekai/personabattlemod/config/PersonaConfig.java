package com.sekai.personabattlemod.config;

import com.sekai.personabattlemod.PersonaBattle;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = PersonaBattle.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PersonaConfig {
    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    //Client
    public static boolean musicBattle;
    public static boolean soundThirdEye;

    public static int UIColorRed;
    public static int UIColorGreen;
    public static int UIColorBlue;

    //Server
    public static int battleRadius;

    public static void bakeConfig() {
        //Client
        musicBattle = CLIENT.musicBattle.get();
        soundThirdEye = CLIENT.soundThirdEye.get();

        UIColorRed = CLIENT.UIColorRed.get();
        UIColorGreen = CLIENT.UIColorGreen.get();
        UIColorBlue = CLIENT.UIColorBlue.get();

        //Common
        battleRadius = COMMON.battleRadius.get();
    }

    // Doesn't need to be an inner class
    public static class ClientConfig {
        public final ForgeConfigSpec.BooleanValue musicBattle;
        public final ForgeConfigSpec.BooleanValue soundThirdEye;

        public final ForgeConfigSpec.IntValue UIColorRed;
        public final ForgeConfigSpec.IntValue UIColorGreen;
        public final ForgeConfigSpec.IntValue UIColorBlue;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Client Config");

            builder.push("Sounds");
            musicBattle = builder
                    .comment("Whether or not to play battle music.")
                    .translation(PersonaBattle.MOD_ID + ".config." + "musicBattle")
                    .define("musicBattle", true);
            soundThirdEye = builder
                    .comment("Whether or not to play sounds when using the third eye ability.")
                    .translation(PersonaBattle.MOD_ID + ".config." + "soundThirdEye")
                    .define("soundThirdEye", true);
            builder.pop();

            builder.push("UI Color");
            UIColorRed = builder
                    .comment("Custom UI color : Red")
                    .translation(PersonaBattle.MOD_ID + ".config." + "UIColorRed")
                    .defineInRange("UIColorRed", 255, 0, 255);
            UIColorGreen = builder
                    .comment("Custom UI color : Green")
                    .translation(PersonaBattle.MOD_ID + ".config." + "UIColorGreen")
                    .defineInRange("UIColorGreen", 0, 0, 255);
            UIColorBlue = builder
                    .comment("Custom UI color : Blue")
                    .translation(PersonaBattle.MOD_ID + ".config." + "UIColorBlue")
                    .defineInRange("UIColorBlue", 0, 0, 255);
            builder.pop();
        }
    }

    public static class CommonConfig {
        public final ForgeConfigSpec.IntValue battleRadius;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Common Config");

            builder.push("Battle");
            battleRadius = builder
                    .comment("When a battle start, nearby enemies will get dragged into the battle as well if they're within the specified block radius.")
                    .translation(PersonaBattle.MOD_ID + ".config." + "battleRadius")
                    .defineInRange("battleRadius", 10, 0, 64);
            builder.pop();
        }
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == PersonaConfig.CLIENT_SPEC) {
            bakeConfig();
        }

        if (configEvent.getConfig().getSpec() == PersonaConfig.COMMON_SPEC) {
            bakeConfig();
        }
    }

}