package com.sekai.personabattlemod.persona.impl;

import com.sekai.personabattlemod.persona.IPersona;
import com.sekai.personabattlemod.persona.property.PersonaArcana;
import com.sekai.personabattlemod.persona.property.PersonaStat;

import java.util.ArrayList;

public class WildCard implements IPersona {
    private int xp;

    private int level;

    private int maxHP;
    private int HP;

    private int maxSP;
    private int SP;

    //TODO Replace this with an actual item power and accuracy, maybe add some slots for weapons or use the one in hand
    private int meleePower = 60;
    private int meleeAccuracy = 96;
    //TODO Same here though with bows
    private int rangedPower = 70;
    private int rangedAccuracy = 90;

    private ArrayList<Persona> personas = new ArrayList<Persona>();
    private int personaIndex;
    private int stockSize;

    public WildCard()
    {
        setLevel(1);
        setMaxHP(80);
        setMaxSP(20);
        setHP(80);
        setSP(20);
    }

    public void clone(WildCard wc)
    {
        setLevel(wc.getLevel());
        setMaxHP(wc.getMaxHP());
        setMaxSP(wc.getMaxSP());
        setHP(getHP());
        setSP(getSP());

        setStockSize(getStockSize());
        for(int i = 0; i < wc.getPersonaCount(); i++) {
            addPersona(wc.getPersona(i));
        }
        equipPersona(wc.getPersonaIndex());
    }

    /**
     * @param persona Persona to add.
     * @return Whether or not the stock is full.
     */
    public void addPersona(Persona persona) {
        personas.add(persona);
    }

    public void clearPersonas() {
        personas.clear();
    }

    public boolean isStockFull() {
        return getPersonaCount() >= getStockSize();
    }

    public Persona getPersona(int index) {
        if(index < getPersonaCount())
            return personas.get(index);
        return null;
    }

    public Persona getEquipedPersona() {
        return getPersona(personaIndex);
    }

    public int getPersonaIndex() {
        return personaIndex;
    }

    public void equipPersona(int index) {
        if(index < getPersonaCount())
            personaIndex = index;
    }

    public int getPersonaCount() {
        return personas.size();
    }

    public int getStockSize() {
        return stockSize;
    }

    public void setStockSize(int stockSize) {
        this.stockSize = stockSize;
    }

    /*
        implementation
     */
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public PersonaArcana getArcana() {
        return getEquipedPersona().getArcana();
    }

    public void setArcana(PersonaArcana arcana) {}

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
        return meleePower;
    }

    public int getMeleeAccuracy() {
        return meleeAccuracy;
    }

    public int getRangedDamage() {
        return rangedPower;
    }

    public int getRangedAccuracy() {
        return rangedAccuracy;
    }

    public int getStat(PersonaStat stat) {
        return getEquipedPersona().getStat(stat);
    }

    public void setStat(PersonaStat stat, int value) {
        getEquipedPersona().setStat(stat, value);
    }

    public void addStat(PersonaStat stat, int value) {
        getEquipedPersona().addStat(stat, value);
    }

    public void clearStats() { getEquipedPersona().clearStats(); }

    public String getSkill(int index) {
        return getEquipedPersona().getSkill(index);
    }

    public int getSkillCount() {
        return getEquipedPersona().getSkillCount();
    }

    public boolean addSkill(String id) {
        return getEquipedPersona().addSkill(id);
    }

    public void removeSkill(int index) {
        getEquipedPersona().removeSkill(index);
    }

    public void removeSkill(String id) {
        getEquipedPersona().removeSkill(id);
    }
}
