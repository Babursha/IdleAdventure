package com.IdleAdventure.adventuregame.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Achievement {

    @Id
    @GeneratedValue
    private int id;
    @Column(name="user_id")
    private int userId;

    @Column(name="total_f_monsters")
    private int totalFMonsters;
    @Column(name="total_d_monsters")
    private int totalDMonsters;
    @Column(name="total_c_monsters")
    private int totalCMonsters;
    @Column(name="total_v_monsters")
    private int totalVMonsters;
    @Column(name="total_s_monsters")
    private int totalSMonsters;

    public Achievement(int userId, int totalFMonsters, int totalDMonsters, int totalCMonsters, int totalVMonsters, int totalSMonsters) {
        this.userId = userId;
        this.totalFMonsters = totalFMonsters;
        this.totalDMonsters = totalDMonsters;
        this.totalCMonsters = totalCMonsters;
        this.totalVMonsters = totalVMonsters;
        this.totalSMonsters = totalSMonsters;
    }

    public int getTotalFMonsters() {
        return totalFMonsters;
    }

    public void setTotalFMonsters(int totalFMonsters) {
        this.totalFMonsters = totalFMonsters;
    }

    public int getTotalDMonsters() {
        return totalDMonsters;
    }

    public void setTotalDMonsters(int totalDMonsters) {
        this.totalDMonsters = totalDMonsters;
    }

    public int getTotalCMonsters() {
        return totalCMonsters;
    }

    public void setTotalCMonsters(int totalCMonsters) {
        this.totalCMonsters = totalCMonsters;
    }

    public int getTotalVMonsters() {
        return totalVMonsters;
    }

    public void setTotalVMonsters(int totalVMonsters) {
        this.totalVMonsters = totalVMonsters;
    }

    public int getTotalSMonsters() {
        return totalSMonsters;
    }

    public void setTotalSMonsters(int totalSMonsters) {
        this.totalSMonsters = totalSMonsters;
    }
}
