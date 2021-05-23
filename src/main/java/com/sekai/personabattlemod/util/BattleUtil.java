package com.sekai.personabattlemod.util;

import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.battle.BattleArena;
import com.sekai.personabattlemod.battle.persona.IPersona;
import com.sekai.personabattlemod.battle.persona.impl.Persona;
import com.sekai.personabattlemod.battle.persona.property.PersonaArcana;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BattleUtil {
    public static BattleArena getArena(ServerPlayerEntity source, LivingEntity target) {
        return new BattleArena();
    }

    public static List<LivingEntity> getEnemies(LivingEntity target, int battleRadius) {
        World world = target.getEntityWorld();
        return world.getEntitiesWithinAABB(MonsterEntity.class, new AxisAlignedBB(target.getPosition()).grow(battleRadius));
    }

    public static IPersona getStats(LivingEntity entity) {
        Persona p = new Persona(entity.getName().getString(), entity.getEntityString());
        p.setLevel(5 + (new Random()).nextInt(5));
        p.setArcana(PersonaArcana.FOOL);
        p.automaticStatDistribution();

        String type = entity.getType().getRegistryName().toString();

        switch(type) {
            case "minecraft:zombie":
                p.addSkill(PersonaBattle.MOD_ID + ":lunge");
                if(Math.random() < 0.4F)
                    p.addSkill(PersonaBattle.MOD_ID + ":makajama");
                break;
            case "minecraft:skeleton":
                p.addSkill(PersonaBattle.MOD_ID + ":sprayshot");
                if(Math.random() < 0.4F)
                    p.addSkill(PersonaBattle.MOD_ID + ":tarukaja");
                break;
            case "minecraft:enderman":
                p.addSkill(PersonaBattle.MOD_ID + ":void");
                if(Math.random() < 0.4F)
                    p.addSkill(PersonaBattle.MOD_ID + ":megido");
                break;
            case "minecraft:creeper":
                p.addSkill(PersonaBattle.MOD_ID + ":selfdestruct");
                break;
            default :
                p.addSkill(PersonaBattle.MOD_ID + ":lunge");
                break;
        }

        return p;
    }
}
