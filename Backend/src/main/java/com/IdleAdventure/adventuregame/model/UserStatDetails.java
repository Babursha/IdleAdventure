package com.IdleAdventure.adventuregame.model;

public class UserStatDetails {

    private int attack;
    private int defense;
    private int hp;
    private int crit_chance;
    private int stat_points;

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

    public int getCrit_chance() {
        return crit_chance;
    }

    public void setCrit_chance(int crit_chance) {
        this.crit_chance = crit_chance;
    }

    public int getStat_points() {
        return stat_points;
    }

    public void setStat_points(int stat_points) {
        this.stat_points = stat_points;
    }
}
