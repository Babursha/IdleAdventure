package com.IdleAdventure.adventuregame.model;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name="user")
public class GameUser {

    @Id
    @GeneratedValue()
    private int id;

    @Column(nullable = false)
    private String username;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private String email;
    private String role;
    private int active;
    private double gold;
    private int goldProgress;
    private Date startWheel;
    private Date endWheel;
    private int statPoints;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

    public int getGoldProgress() {
        return goldProgress;
    }

    public void setGoldProgress(int goldProgress) {
        this.goldProgress = goldProgress;
    }

    public Date getStartWheel() {
        return startWheel;
    }

    public void setStartWheel(Date startWheel) {
        this.startWheel = startWheel;
    }

    public Date getEndWheel() {
        return endWheel;
    }

    public void setEndWheel(Date endWheel) {
        this.endWheel = endWheel;
    }

    public int getStatPoints() {
        return statPoints;
    }

    public void setStatPoints(int statPoints) {
        this.statPoints = statPoints;
    }
}
