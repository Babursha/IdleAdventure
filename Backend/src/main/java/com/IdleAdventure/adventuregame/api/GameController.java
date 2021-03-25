package com.IdleAdventure.adventuregame.api;


import com.IdleAdventure.adventuregame.model.*;
import com.IdleAdventure.adventuregame.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javax.management.Query.value;

@CrossOrigin
@RestController
public class GameController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MonsterRepository monsterRepository;

    @Autowired
    private EquippedRepository equippedRepository;

    @Autowired
    private StatsRepository statsRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

/*    *//**
     * Searches the item database to find it by name, if it already exist's in
     * the players inventory then it adds to the amount. Otherwise, it will create
     * a record for the user and itemid in the inventory database.
     *
     * @param  data An item that already exists in the database.
     */
/*    @RequestMapping("/api/get_item")
    public void getItem(@RequestBody Item data) throws Exception{

        Item find = itemRepository.findByItemName(data.getName());
        try {
            int check = inventoryRepository.itemExists(data.getIduser(), find.getItemId());
            if (check == 0) {
                Inventory add = new Inventory(data.getIduser(), find.getItemId(), 1);
                inventoryRepository.save(add);
            } else {
                inventoryRepository.addItem(data.getIduser(), find.getItemId());
            }
        }
        catch(Exception e){
            System.out.print(e);
        }

    }*/
    /**
     * Based on the SecurityContextHolder, it determines the user that is logged in
     * and uses the two functions in the Inventory repository, getAllEquipItems and
     * getAllPotionItems, to display all the items in the users inventory.
     *
     * @return      List of items and amount in user inventory.
     */
    @RequestMapping("/api/show_inventory")
    public List<Object> showInventory() throws Exception{
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser userInv = userRepository.findByUsername(username);
        List<ItemEquipment> equips = inventoryRepository.getAllEquipItems(userInv.getId());
        List<ItemPotion> potions = inventoryRepository.getAllPotionItems(userInv.getId());
        List<ItemLootBag> lootBags = inventoryRepository.getAllLootBagItems(userInv.getId());
        List<Object> inventory = new ArrayList<>();
        inventory.addAll(equips);
        inventory.addAll(potions);
        inventory.addAll(lootBags);
        
        return inventory;
    }

    /**
     * Grabs all the monsters in the database with the location field set
     * as 'forest'.
     *
     * @return      List of monsters based on the location ex.forest.
     */

    @RequestMapping("/api/dungeons/forestDetails")
    public List<Monster> forestMonstersDetails() throws Exception{
        return monsterRepository.getForestMonsters();


    }

    /**
     * Grabs all the monsters in the database with the location field set
     * as 'desert'.
     *
     * @return      List of monsters based on the location from the desert
     */

    @RequestMapping("/api/dungeons/desertDetails")
    public List<Monster> desertMonstersDetails() throws Exception{
        return monsterRepository.getDesertMonsters();


    }

    /**
     * Takes a list of random monsters from the database with the 'forest'
     * location and sends it back to the client.
     *
     * @return  Random list of monsters in the database with 'forest' location.
     */

    @RequestMapping("/api/dungeons/forest/battle")
    public List<Monster> forestBattleSim() throws Exception {
        List<Monster> monsters = new ArrayList<>();
        int pick;
        for (int i = 0; i < 100; i++) {
            double bossEncounter = Math.random();
            List<Monster> monster = monsterRepository.getForestMonsters();
            pick = new Random().nextInt(4);
            if(bossEncounter < .10){
                pick = 4;
            }
            monsters.add(monster.get(pick));
        }

        return monsters;

    }

    @RequestMapping("/api/dungeons/desert/battle")
    public List<Monster> desertBattleSim() throws Exception{
        List<Monster> monsters = new ArrayList<>();
        int pick;
        for(int i =0;i <100;i++){
            List<Monster> monster = monsterRepository.getDesertMonsters();
            pick = new Random().nextInt(3);
            monsters.add(monster.get(pick));
        }

        return monsters;

    }

    @RequestMapping("/api/inventory/get_equipped")
    public List<EquippedItem> getEquipped() throws Exception{
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user= userRepository.findByUsername(username);
        EquippedId equippedIds = equippedRepository.get_equipped_items(user.getId());
        List<EquippedItem> userEquipped = new ArrayList<>();
        if(equippedIds.getHelmet_id() != null ){
            userEquipped.add(itemRepository.getEquippedItemDetails(equippedIds.getHelmet_id()));
        }
        if(equippedIds.getChest_id() != null){
            System.out.print(equippedIds.getChest_id());
            userEquipped.add(itemRepository.getEquippedItemDetails(equippedIds.getChest_id()));
        }
        if(equippedIds.getLegging_id() != null){
            userEquipped.add(itemRepository.getEquippedItemDetails(equippedIds.getLegging_id()));
        }
        if(equippedIds.getBoots_id() != null){
            userEquipped.add(itemRepository.getEquippedItemDetails(equippedIds.getBoots_id()));
        }
        if(equippedIds.getWeapon_id() != null){
            userEquipped.add(itemRepository.getEquippedItemDetails(equippedIds.getWeapon_id()));
        }
        if(equippedIds.getRing_id() != null){
            userEquipped.add(itemRepository.getEquippedItemDetails(equippedIds.getRing_id()));
        }
        return userEquipped;
    }


    @RequestMapping("/api/inventory/equipItem")
    public void equipItem(@RequestBody UserItemEquip data) throws Exception{
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        Item item = itemRepository.findByName(data.getName());

        String equipType = (data.getEquip_type());
        if(equipType.equals("Weapon")){
            equippedRepository.equip_weapon(user.getId(),item.getId());
            updateStatsEquip(user,item);
        }
        else if(equipType.equals("Chest")){
            equippedRepository.equip_chest(user.getId(),item.getId());
            updateStatsEquip(user,item);
        }
        else if(equipType.equals("Legging")){
            equippedRepository.equip_legging(user.getId(),item.getId());
            updateStatsEquip(user,item);
        }
        else if(equipType.equals("Helmet")){
            equippedRepository.equip_helmet(user.getId(),item.getId());
            updateStatsEquip(user,item);
        }
        else if(equipType.equals("Boots")){
            equippedRepository.equip_boots(user.getId(),item.getId());
            updateStatsEquip(user,item);
        }
        else if(equipType.equals("Ring")){
            equippedRepository.equip_ring(user.getId(),item.getId());
            updateStatsEquip(user,item);
        }

        inventoryRepository.removeItem(user.getId(),item.getId());
        inventoryRepository.deleteEntry(user.getId(),item.getId());

    }

    @RequestMapping("api/inventory/unequipItem")
    public void unequipItem(@RequestBody UserItemEquip data){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        Item item = itemRepository.findByName(data.getName());

        String equipType = (data.getEquip_type());
        if(equipType.equals("Weapon")){
            equippedRepository.unequip_weapon(user.getId());
            updateStatsUnequip(user,item);
        }
        else if(equipType.equals("Chest")){
            equippedRepository.unequip_chest(user.getId());
            updateStatsUnequip(user,item);
        }
        else if(equipType.equals("Legging")){
            equippedRepository.unequip_legging(user.getId());
            updateStatsUnequip(user,item);
        }
        else if(equipType.equals("Helmet")){
            equippedRepository.unequip_helmet(user.getId());
            updateStatsUnequip(user,item);
        }
        else if(equipType.equals("Boots")){
            equippedRepository.unequip_boots(user.getId());
            updateStatsUnequip(user,item);
        }
        else if(equipType.equals("Ring")){
            equippedRepository.unequip_ring(user.getId());
            updateStatsUnequip(user,item);
        }
        if(inventoryRepository.itemExists(user.getId(),item.getId()) == 1){
            inventoryRepository.addItem(user.getId(),item.getId());
        }
        else {
            inventoryRepository.addNewItem(item.getId(),user.getId());
        }
    }

    public void updateStatsEquip(GameUser user, Item item){
        UserHomeDetails userStats = userRepository.getUserDetails(user.getId());
        EquippedItem itemEquip = itemRepository.getEquippedItemDetails(item.getId());
        statsRepository.updateStats(userStats.getAttack()+itemEquip.getAttack(),
                userStats.getDefense()+itemEquip.getDefense(),userStats.getHp()+itemEquip.getHp(),
                userStats.getCrit_chance()+itemEquip.getCrit_chance(),user.getId());
    }

    public void updateStatsUnequip(GameUser user, Item item){
        UserHomeDetails userStats = userRepository.getUserDetails(user.getId());
        EquippedItem itemEquip = itemRepository.getEquippedItemDetails(item.getId());
        statsRepository.updateStats(userStats.getAttack()-itemEquip.getAttack(),
                userStats.getDefense()-itemEquip.getDefense(),userStats.getHp()-itemEquip.getHp(),
                userStats.getCrit_chance()-itemEquip.getCrit_chance(),user.getId());

    }
/*
    @RequestMapping("api/inventory/statUpdate")
    public void statUpdate(@RequestBody User data){
        User user = usersRepository.findByUsername(String.valueOf(data.getUsername()));
        usersRepository.updateStats(user.getId(),data.getAttack());
    }
*/
/*
    @RequestMapping("api/dungeons/collectXp")
    public void dungeonsCollectXp(@RequestBody Integer xp){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        userRepository.dungeonCollectXp(user.getId());
    }
*/
    @RequestMapping("api/tavern/shop/buy")
    public List<Object> displayShop(){
        List<ItemEquipment> randomEquipments = itemRepository.getAllEquipments();
        List<ItemPotion> randomPotions =itemRepository.getAllPotions();
        List<Object> randomShop = new ArrayList<>();
        randomShop.addAll(randomEquipments);
        randomShop.addAll(randomPotions);
        return randomShop;

    }

    @RequestMapping("api/home/collectGold")
    public void collectGold(@RequestBody int gold){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        userRepository.updateGold(user.getId(),gold);
    }

    @RequestMapping("api/home/collectXp")
    public void collectGold(@RequestBody UserLevelDetails data){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        userRepository.updateXp(user.getId(),data.getLevel(),data.getXpCurrent(),data.getXpLvlUp());
    }

    @RequestMapping("api/home/goldProgress")
    public void saveGoldProgress(@RequestBody int gold){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        userRepository.saveGoldProgress(user.getId(),gold);

    }

    @RequestMapping("api/home/xpProgress")
    public void saveXpProgress(@RequestBody int xp){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        userRepository.saveXpProgress(user.getId(),xp);

    }

    @RequestMapping("api/dungeons/get_potions")
    public List<ItemPotion> getPotions() throws Exception{
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser userInv = userRepository.findByUsername(username);
        List<ItemPotion> potions = inventoryRepository.getAllPotionItems(userInv.getId());

        return potions;
    }

    @RequestMapping("/api/dungeons/collectLoot")
    public void collectLoot(@RequestBody CollectLoot bag){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser userInv = userRepository.findByUsername(username);
        Item search_basic = new Item();
        Item search_boss = new Item();
        if(bag.getType().equals("forest")){
            search_basic = itemRepository.findByName("Forest Loot Bag");
            search_boss = itemRepository.findByName("Rare Forest Loot Bag");
        }

        if(inventoryRepository.itemExists(userInv.getId(),search_basic.getId()) == 1){
            inventoryRepository.addItemAmount(userInv.getId(),search_basic.getId(),bag.getBasic());
        }
        else{
            if(bag.getBasic() !=0)
                inventoryRepository.addNewItemAmount(userInv.getId(),search_basic.getId(),bag.getBasic());
        }
        if(inventoryRepository.itemExists(userInv.getId(),search_boss.getId())==1){
            inventoryRepository.addItemAmount(userInv.getId(),search_boss.getId(),bag.getRare());
        }
        else{
            if(bag.getRare() != 0)
                inventoryRepository.addNewItemAmount(userInv.getId(),search_boss.getId(),bag.getRare());
        }

    }

    @RequestMapping("api/tavern/shop/buyItem")
    public double buyAtShop(@RequestBody Item data){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        Item item = itemRepository.findByName(data.getName());
        if(user.getGold() <item.getPrice()){
            return -1;
        }
        user.setGold(user.getGold()-item.getPrice());
        if(inventoryRepository.itemExists(user.getId(),item.getId()) == 1){
            inventoryRepository.addItem(user.getId(),item.getId());
        }
        else {
            inventoryRepository.addNewItem(item.getId(),user.getId());
        }
        return user.getGold();
    }


    @RequestMapping("api/tavern/shop/sell")
    public int sellAtShop(@RequestBody Item data){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        Item item = itemRepository.findByName(data.getName());
        System.out.print(item.getPrice());
        userRepository.updateGold(user.getId(),(int)user.getGold()+item.getSell());
        inventoryRepository.removeItem(user.getId(),item.getId());
        inventoryRepository.deleteEntry(user.getId(),item.getId());
        return (int)user.getGold()+item.getSell();
    }

}
