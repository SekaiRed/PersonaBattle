package com.sekai.personabattlemod.util;

import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.dimensions.MementosModDimension;
import com.sekai.personabattlemod.entities.ShadowEntity;
import com.sekai.personabattlemod.items.ItemBase;
import com.sekai.personabattlemod.items.ItemLogo;
import com.sekai.personabattlemod.items.ItemSkillcard;
import com.sekai.personabattlemod.sounds.SoundBase;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, PersonaBattle.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, PersonaBattle.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, PersonaBattle.MOD_ID);
    public static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, PersonaBattle.MOD_ID);

    //public static final DeferredRegister<Regist>

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Items
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", ItemBase::new);
    public static final RegistryObject<Item> LOGO = ITEMS.register("modlogo", ItemLogo::new);
    public static final RegistryObject<Item> SKILLCARD = ITEMS.register("skillcard", ItemSkillcard::new);

    //Entities
    public static final RegistryObject<EntityType<ShadowEntity>> SHADOW = ENTITIES.register("shadow", () -> EntityType.Builder.create(ShadowEntity::new, EntityClassification.MONSTER)
            .size(0.8f, 0.6f)
            .setShouldReceiveVelocityUpdates(true)
            .build(new ResourceLocation(PersonaBattle.MOD_ID, "shadow").toString()));

    //Sounds
    public static final RegistryObject<SoundEvent> PHONE_OPEN = SOUNDS.register("phone_open",
            () -> new SoundBase(new ResourceLocation(PersonaBattle.MOD_ID,"ui.phone.open")));
    public static final RegistryObject<SoundEvent> PHONE_CHANGE = SOUNDS.register("phone_change",
            () -> new SoundBase(new ResourceLocation(PersonaBattle.MOD_ID,"ui.phone.change")));

    //Dimensions
    public static final RegistryObject<ModDimension> MEMENTOS = DIMENSIONS.register("mementos", MementosModDimension::new);
}
