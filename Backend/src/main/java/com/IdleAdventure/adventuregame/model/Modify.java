package com.IdleAdventure.adventuregame.model;

import javax.persistence.*;

@Entity
public class Modify {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name="user_id")
    private int userId;
    @Column(name = "item_id")
    private int itemId;

    private int hp;
    private int attack;
    private int defense;
    private int critChance;

    public Modify(int hp, int attack, int defense, int critChance) {
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.critChance = critChance;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
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

    public int getCritChance() {
        return critChance;
    }

    public void setCritChance(int critChance) {
        this.critChance = critChance;
    }
}
