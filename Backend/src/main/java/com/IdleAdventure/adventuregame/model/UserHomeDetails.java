package com.IdleAdventure.adventuregame.model;

import java.util.Date;

public interface UserHomeDetails {
    String getUsername();
    Integer getLevel();
    Integer getXp_current();
    Integer getXp_gained();
    Integer getXp_lvl_up();
    Integer getXp_progress();
    Integer getGold();
    Integer getGold_progress();
    Integer getHp();
    Integer getDefense();
    Integer getAttack();
    Integer getCrit_chance();
    Date getEnd_wheel();
}
