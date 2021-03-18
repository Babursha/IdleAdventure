package com.IdleAdventure.adventuregame.model;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Equipped {
    @Id
    @Column(name="user_id")
    private int userId;
    private int helmetId;
    private int chestId;
    private int leggingId;
    private int bootsId;
    private int weaponId;
    private int ringId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getHelmetId() {
        return helmetId;
    }

    public void setHelmetId(int helmetId) {
        this.helmetId = helmetId;
    }

    public int getChestId() {
        return chestId;
    }

    public void setChestId(int chestId) {
        this.chestId = chestId;
    }

    public int getLeggingId() {
        return leggingId;
    }

    public void setLeggingId(int leggingId) {
        this.leggingId = leggingId;
    }

    public int getBootsId() {
        return bootsId;
    }

    public void setBootsId(int bootsId) {
        this.bootsId = bootsId;
    }

    public int getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }

    public int getRingId() {
        return ringId;
    }

    public void setRingId(int ringId) {
        this.ringId = ringId;
    }
}
