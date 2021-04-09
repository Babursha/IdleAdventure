package com.IdleAdventure.adventuregame.model;

public interface ItemCraftable {

    String getName();
    String getDescription();
    Integer getLevel();
    String getItem_type();
    Integer getSell();
    String getRarity();
    String getIngredient1();
    int getIngredient1Amount();
    String getIngredient2();
    int getIngredient2Amount();
    String getIngredient3();
    int getIngredient3Amount();
    String getIngredient4();
    int getIngredient4Amount();
    String getIngredient5();
    int getIngredient5Amount();
    int gold();
}
