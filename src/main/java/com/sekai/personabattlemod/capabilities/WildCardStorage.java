package com.sekai.personabattlemod.capabilities;

import com.sekai.personabattlemod.persona.impl.Persona;
import com.sekai.personabattlemod.persona.impl.WildCard;
import com.sekai.personabattlemod.persona.property.PersonaArcana;
import com.sekai.personabattlemod.persona.property.PersonaStat;
import net.minecraft.command.arguments.NBTCompoundTagArgument;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class WildCardStorage implements Capability.IStorage<WildCard> {
    @Override
    public INBT writeNBT(Capability<WildCard> capability, WildCard instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();

        tag.putInt("level", instance.getLevel());

        tag.putInt("maxHP", instance.getMaxHP());
        tag.putInt("maxSP", instance.getMaxSP());

        tag.putInt("HP", instance.getHP());
        tag.putInt("SP", instance.getSP());

        tag.putInt("pIndex", instance.getPersonaIndex());
        tag.putInt("stockSize", instance.getStockSize());

        int pCount = instance.getPersonaCount();
        tag.putInt("pCount", pCount);
        for(int i = 0; i < pCount; i++) {
            Persona p = instance.getPersona(i);

            tag.putString("pName" + i, p.getName());
            tag.putString("pModel" + i, p.getModelID());
            tag.putInt("pLevel" + i, p.getLevel());

            tag.putInt("pArcana" + i, p.getArcana().ordinal());

            int sCount = p.getSkillCount();
            tag.putInt("p" + i + "skillCount", sCount);
            for(int j = 0; j < sCount; j++)
                tag.putString("p" + i + "skill" + j, p.getSkill(j));

            for(int j = 0; j < PersonaStat.values().length; j++)
                tag.putInt("p" + i + PersonaStat.values()[j].toString(), p.getStat(PersonaStat.values()[j]));
        }

        return tag;
    }

    @Override
    public void readNBT(Capability<WildCard> capability, WildCard instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;

        instance.setLevel(tag.getInt("level"));

        instance.setMaxHP(tag.getInt("maxHP"));
        instance.setMaxSP(tag.getInt("maxSP"));

        instance.setHP(tag.getInt("HP"));
        instance.setSP(tag.getInt("SP"));

        instance.setStockSize(tag.getInt("stockSize"));

        int pCount = tag.getInt("pCount");
        for(int i = 0; i < pCount; i++) {
            Persona p = new Persona(tag.getString("pName" + i), tag.getString("pModel" + i));
            p.setLevel(tag.getInt("pLevel" + i));

            p.setArcana(PersonaArcana.values()[tag.getInt("pArcana" + i)]);

            int sCount = tag.getInt("p" + i + "skillCount");
            for(int j = 0; j < sCount; j++)
                p.addSkill(tag.getString("p" + i + "skill" + j));
                //tag.putString("p" + i + "skill" + j, p.getSkill(j));

            for(int j = 0; j < PersonaStat.values().length; j++)
                p.setStat(PersonaStat.values()[j], tag.getInt("p" + i + PersonaStat.values()[j].toString()));

            instance.addPersona(p);
        }

        instance.equipPersona(tag.getInt("pIndex"));
    }
}
