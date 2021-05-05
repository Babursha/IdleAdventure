package com.IdleAdventure.adventuregame.model;

public class CraftEquip {

    private String equip_type;
    private String ingredient1;
    private int ingredient1Amount;
    private String ingredient2;
    private int ingredient2Amount;
    private String ingredient3;
    private int ingredient3Amount;
    private String ingredient4;
    private int ingredient4Amount;
    private String ingredient5;
    private int ingredient5Amount;
    private int gold;

    public CraftEquip(String type, String ing1,int ing1Amount,String ing2,int ing2Amount,String ing3,int ing3Amount,String ing4,int ing4Amount,String ing5,int ing5Amount,int gold){
        this.equip_type = type;
        this.ingredient1 = ing1;
        this.ingredient1Amount=ing1Amount;
        this.ingredient2 = ing2;
        this.ingredient2Amount=ing2Amount;
        this.ingredient3 = ing3;
        this.ingredient3Amount=ing3Amount;
        this.ingredient4 = ing4;
        this.ingredient4Amount=ing4Amount;
        this.ingredient5 = ing5;
        this.ingredient5Amount=ing5Amount;
        this.gold = gold;
    }
    public String getEquip_type() {
        return equip_type;
    }

    public void setEquip_type(String equip_type) {
        this.equip_type = equip_type;
    }

    public String getIngredient1() {
        return ingredient1;
    }

    public void setIngredient1(String ingredient1) {
        this.ingredient1 = ingredient1;
    }

    public int getIngredient1Amount() {
        return ingredient1Amount;
    }

    public void setIngredient1Amount(int ingredient1Amount) {
        this.ingredient1Amount = ingredient1Amount;
    }

    public String getIngredient2() {
        return ingredient2;
    }

    public void setIngredient2(String ingredient2) {
        this.ingredient2 = ingredient2;
    }

    public int getIngredient2Amount() {
        return ingredient2Amount;
    }

    public void setIngredient2Amount(int ingredient2Amount) {
        this.ingredient2Amount = ingredient2Amount;
    }

    public String getIngredient3() {
        return ingredient3;
    }

    public void setIngredient3(String ingredient3) {
        this.ingredient3 = ingredient3;
    }

    public int getIngredient3Amount() {
        return ingredient3Amount;
    }

    public void setIngredient3Amount(int ingredient3Amount) {
        this.ingredient3Amount = ingredient3Amount;
    }

    public String getIngredient4() {
        return ingredient4;
    }

    public void setIngredient4(String ingredient4) {
        this.ingredient4 = ingredient4;
    }

    public int getIngredient4Amount() {
        return ingredient4Amount;
    }

    public void setIngredient4Amount(int ingredient4Amount) {
        this.ingredient4Amount = ingredient4Amount;
    }

    public String getIngredient5() {
        return ingredient5;
    }

    public void setIngredient5(String ingredient5) {
        this.ingredient5 = ingredient5;
    }

    public int getIngredient5Amount() {
        return ingredient5Amount;
    }

    public void setIngredient5Amount(int ingredient5Amount) {
        this.ingredient5Amount = ingredient5Amount;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
