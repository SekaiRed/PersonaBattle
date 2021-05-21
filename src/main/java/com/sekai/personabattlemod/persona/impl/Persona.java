package com.sekai.personabattlemod.persona.impl;

import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.persona.IPersona;
import com.sekai.personabattlemod.persona.property.PersonaArcana;
import com.sekai.personabattlemod.persona.property.PersonaStat;

import java.util.ArrayList;
import java.util.Random;

/**
 * General class for Persona implementation for shadows, characters and wild cards.
 */
public class Persona implements IPersona {
    private String name;
    private String modelID;

    private int maxHP;
    private int HP;

    private int maxSP;
    private int SP;

    private int stats[] = new int[PersonaStat.values().length];

    private ArrayList<String> skills = new ArrayList<String>();

    private int level;
    private PersonaArcana arcana;

    public Persona(String name, String modelID)
    {
        this.name = name;
        this.modelID = modelID;

        for(int i = 0; i < PersonaStat.values().length; i++)
            setStat(PersonaStat.values()[i], 1);

        setLevel(1);
        setMaxHP(80);
        setMaxSP(20);
        setHP(80);
        setSP(20);
    }

    public String getName() { return name; }

    public String getModelID() {
        return modelID;
    }

    public void automaticStatDistribution()
    {
        clearStats();
        int total = level * 3;
        Random random = new Random();
        //total number of stat points to distribute
        for(int i = 0; i < total; i++)
        {
            int x = random.nextInt(PersonaStat.values().length);
            PersonaStat stat = PersonaStat.values()[x];
            addStat(stat, 1);
        }

        setMaxHP(80 + level * 10);
        setMaxSP(20 + level * 5);
    }

    //implementation
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public PersonaArcana getArcana() {
        return arcana;
    }

    public void setArcana(PersonaArcana arcana) { this.arcana = arcana; }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getMaxSP() {
        return maxSP;
    }

    public void setMaxSP(int maxSP) {
        this.maxSP = maxSP;
    }

    public int getSP() {
        return SP;
    }

    public void setSP(int SP) {
        this.SP = SP;
    }

    public int getMeleeDamage() {
        return 40;
    }

    public int getMeleeAccuracy() {
        return 80;
    }

    public int getRangedDamage() {
        return 50;
    }

    public int getRangedAccuracy() {
        return 70;
    }

    public int getStat(PersonaStat stat) {
        return stats[stat.ordinal()];
    }

    public void setStat(PersonaStat stat, int value) {
        this.stats[stat.ordinal()] = value;
    }

    public void addStat(PersonaStat stat, int value) {
        this.stats[stat.ordinal()] = getStat(stat) + value;
    }

    public void clearStats() { for(int i = 0; i < PersonaStat.values().length; i++) setStat(PersonaStat.values()[i], 1);}

    public String getSkill(int index) {
        return skills.get(index);
    }

    public int getSkillCount() {
        return skills.size();
    }

    public boolean addSkill(String id) {
        if(PersonaBattle.MAX_SKILL == getSkillCount())
            return false;

        if(skills.contains(id))
            return false;

        skills.add(id);
        return true;
    }

    public void removeSkill(int index) {
        skills.remove(index);
    }

    public void removeSkill(String id) {
        skills.remove(id);
    }
}
