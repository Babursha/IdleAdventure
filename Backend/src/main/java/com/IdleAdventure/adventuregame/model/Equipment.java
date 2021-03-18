package com.IdleAdventure.adventuregame.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Equipment {
    @Id
    @Column(name="item_id")
    private int itemId;
    private String equipType;
    private int hp;
    private int attack;
    private int defense;
    private int critChance;

}
