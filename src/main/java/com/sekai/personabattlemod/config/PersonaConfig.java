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

    //public static boolean aBoolean;
    //public static int anInt;
    public static boolean musicBattle;
    public static boolean soundThirdEye;

    public static int UIColorRed;
    public static int UIColorGreen;
    public static int UIColorBlue;

    public static void bakeConfig() {
        //aBoolean = CLIENT.aBoolean.get();
        //anInt = CLIENT.anInt.get();
        musicBattle = CLIENT.musicBattle.get();
        soundThirdEye = CLIENT.soundThirdEye.get();

        UIColorRed = CLIENT.UIColorRed.get();
        UIColorGreen = CLIENT.UIColorGreen.get();
        UIColorBlue = CLIENT.UIColorBlue.get();
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

        /*public final ForgeConfigSpec.BooleanValue aBoolean;
        public final ForgeConfigSpec.IntValue anInt;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            aBoolean = builder
                    .comment("aBoolean usage description")
                    .translation(PersonaBattle.MOD_ID + ".config." + "aBoolean")
                    .define("aBoolean", false);

            builder.push("category");
            anInt = builder
                    .comment("anInt usage description")
                    .translation(PersonaBattle.MOD_ID + ".config." + "anInt")
                    .defineInRange("anInt", 10, 0, 100);
            builder.pop();
        }*/
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == PersonaConfig.CLIENT_SPEC) {
            bakeConfig();
        }
    }

}