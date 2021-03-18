package com.IdleAdventure.adventuregame.model;

import javax.persistence.Column;

public class UserItemEquip {
    private int user_id;
    private String name;
    private String equip_type;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEquip_type() {
        return equip_type;
    }

    public void setEquip_type(String equip_type) {
        this.equip_type = equip_type;
    }
}
