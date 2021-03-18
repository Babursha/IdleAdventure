package com.IdleAdventure.adventuregame.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Stats {
    @Id
    private int userId;
    private int level;
    private int attack;
    private int defense;
    private int hp;
    private int critChance;
    private int xpGained;
    private int xpCurrent;
    private int xpLvlUp;
    private int xpProgress;

    public Stats(){

    }

    public Stats(int userId, int level, int attack, int defense, int hp, int critChance, int xpGained, int xpCurrent, int xpLvlUp, int xpProgress) {
        this.userId = userId;
        this.level = level;
        this.attack = attack;
        this.defense = defense;
        this.hp = hp;
        this.critChance = critChance;
        this.xpGained = xpGained;
        this.xpCurrent = xpCurrent;
        this.xpLvlUp = xpLvlUp;
        this.xpProgress = xpProgress;
    }

    public int getUserId() {
        return userId;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getCritChance() {
        return critChance;
    }

    public void setCritChance(int critChance) {
        this.critChance = critChance;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXpGained() {
        return xpGained;
    }

    public void setXpGained(int xpGained) {
        this.xpGained = xpGained;
    }

    public int getXpCurrent() {
        return xpCurrent;
    }

    public void setXpCurrent(int xpCurrent) {
        this.xpCurrent = xpCurrent;
    }

    public int getXpLvlUp() {
        return xpLvlUp;
    }

    public void setXpLvlUp(int xpLvlUp) {
        this.xpLvlUp = xpLvlUp;
    }

    public int getXpProgress() {
        return xpProgress;
    }

    public void setXpProgress(int xpProgress) {
        this.xpProgress = xpProgress;
    }
}
