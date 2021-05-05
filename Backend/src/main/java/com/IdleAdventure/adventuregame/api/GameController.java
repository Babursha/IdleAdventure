package com.IdleAdventure.adventuregame.api;


import com.IdleAdventure.adventuregame.model.*;
import com.IdleAdventure.adventuregame.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        List<UserCraftMaterial> materials = inventoryRepository.getAllUserMaterials(userInv.getId());
        List<Object> inventory = new ArrayList<>();
        inventory.addAll(equips);
        inventory.addAll(potions);
        inventory.addAll(lootBags);
        inventory.addAll(materials);
        
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
     * Grabs all the monsters in the database with the location field set
     * as 'cave'.
     *
     * @return      List of monsters based on the location from the cave
     */

    @RequestMapping("/api/dungeons/caveDetails")
    public List<Monster> caveMonstersDetails() throws Exception{
        return monsterRepository.getCaveMonsters();


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

    @RequestMapping("/api/dungeons/cave/battle")
    public List<Monster> caveBattleSim() throws Exception{
        List<Monster> monsters = new ArrayList<>();
        int pick;
        for(int i =0;i <100;i++){
            List<Monster> monster = monsterRepository.getCaveMonsters();
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

    @RequestMapping("/api/inventory/openLootBag")
    public void openLootBag(@RequestBody ItemLoot item){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        String[] forestLootChance = {"Jar of Slime","Wolf Pelt","Pixie Dust","Golem Artifact"};

        if(item.getName().equals("Forest Loot Bag")){
            for(int i = 0;i < 4;i++){
                Item material = itemRepository.findByName(forestLootChance[i]);
                double getCheck = Math.random()*100;
                double amountCheck = Math.random()*100;
                if(getCheck > 40){
                    if(amountCheck >= 90){
                        if(inventoryRepository.itemExists(user.getId(),material.getId()) ==1){
                            inventoryRepository.addItemAmount(user.getId(),material.getId(),12);
                        }
                        else{
                            inventoryRepository.addNewItemAmount(user.getId(),material.getId(),12);
                        }
                    }
                    else if(amountCheck >60 && amountCheck < 90){
                        if(inventoryRepository.itemExists(user.getId(),material.getId()) ==1){
                            inventoryRepository.addItemAmount(user.getId(),material.getId(),6);
                        }
                        else{
                            inventoryRepository.addNewItemAmount(user.getId(),material.getId(),6);
                        }
                    }
                    else {
                        if(inventoryRepository.itemExists(user.getId(),material.getId()) ==1){
                            inventoryRepository.addItemAmount(user.getId(),material.getId(),3);
                        }
                        else{
                            inventoryRepository.addNewItemAmount(user.getId(),material.getId(),3);
                        }
                    }
                    }
                }
            if(item.getAmount() == 1) {
                inventoryRepository.removeItemAmount(user.getId(), itemRepository.findByName("Forest Loot Bag").getId(), 1);
                inventoryRepository.deleteEntry(user.getId(), itemRepository.findByName("Forest Loot Bag").getId());
            }
            else
                inventoryRepository.removeItemAmount(user.getId(),itemRepository.findByName("Forest Loot Bag").getId(),1);
            }


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
    public double sellAtShop(@RequestBody Item data){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        Item item = itemRepository.findByName(data.getName());
        System.out.print(item.getPrice());
        userRepository.updateGold(user.getId(),(int)user.getGold()+item.getSell());
        inventoryRepository.removeItem(user.getId(),item.getId());
        inventoryRepository.deleteEntry(user.getId(),item.getId());
        return user.getGold();
    }

    @RequestMapping("/api/tavern/userCraftItem")
    public double userCraftItem(@RequestBody CraftedItem item){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        if(user.getGold() < item.getGold())
            return -1;
        List<UserCraftMaterial> userMaterials = inventoryRepository.getAllUserMaterials(user.getId());
        boolean craftSuccess = false;
        if(item.getIngredient1() != null){
            for (int j = 0; j < userMaterials.size(); j++) {
                if (userMaterials.get(j).getName().equals(item.getIngredient1()) && userMaterials.get(j).getAmount() > item.getIngredient1Amount()) {
                    inventoryRepository.removeItemAmount(user.getId(), itemRepository.findByName(item.getIngredient1()).getId(), item.getIngredient1Amount());
                    craftSuccess = true;
                    break;
                }
            }
            if(!craftSuccess)
                return -1;
        }
        if (item.getIngredient2() != null) {
            for (int j = 0; j < userMaterials.size(); j++) {
                if (userMaterials.get(j).getName().equals(item.getIngredient2()) && userMaterials.get(j).getAmount() > item.getIngredient2Amount()) {
                    inventoryRepository.removeItemAmount(user.getId(), itemRepository.findByName(item.getIngredient2()).getId(), item.getIngredient2Amount());
                    craftSuccess = true;
                }
            }
            if(!craftSuccess)
                return -1;
        }
        if (item.getIngredient3() != null) {
            for (int j = 0; j < userMaterials.size(); j++) {
                if (userMaterials.get(j).getName().equals(item.getIngredient3()) && userMaterials.get(j).getAmount() > item.getIngredient3Amount()) {
                    inventoryRepository.removeItemAmount(user.getId(), itemRepository.findByName(item.getIngredient3()).getId(), item.getIngredient3Amount());
                    craftSuccess = true;
                }
            }
            if(!craftSuccess)
                return -1;
        }
        if (item.getIngredient4() != null) {
            for (int j = 0; j < userMaterials.size(); j++) {
                if (userMaterials.get(j).getName().equals(item.getIngredient4()) && userMaterials.get(j).getAmount() > item.getIngredient4Amount()) {
                    inventoryRepository.removeItemAmount(user.getId(), itemRepository.findByName(item.getIngredient4()).getId(), item.getIngredient4Amount());
                    craftSuccess = true;
                }
            }
            if(!craftSuccess)
                return -1;
        }
        if (item.getIngredient5() != null) {
            for (int j = 0; j < userMaterials.size(); j++) {
                if (userMaterials.get(j).getName().equals(item.getIngredient5()) && userMaterials.get(j).getAmount() > item.getIngredient5Amount()) {
                    inventoryRepository.removeItemAmount(user.getId(), itemRepository.findByName(item.getIngredient5()).getId(), item.getIngredient5Amount());
                    craftSuccess = true;
                }
            }
            if(!craftSuccess)
                return -1;
        }
        addCraftedItemToInv(user,item);
        userRepository.updateGold(user.getId(),(int)user.getGold()-item.getGold());
        return user.getGold();

    }

    @RequestMapping("/api/tavern/userCraftEquipment")
    public double userCraftItemEquipment(@RequestBody CraftEquip item){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        if(user.getGold() < item.getGold())
            return -1;
        List<UserCraftMaterial> userMaterials = inventoryRepository.getAllUserMaterials(user.getId());
        boolean craftSuccess = false;
        if(item.getIngredient1() != null){
            for (int j = 0; j < userMaterials.size(); j++) {
                if (userMaterials.get(j).getName().equals(item.getIngredient1()) && userMaterials.get(j).getAmount() > item.getIngredient1Amount()) {
                    inventoryRepository.removeItemAmount(user.getId(), itemRepository.findByName(item.getIngredient1()).getId(), item.getIngredient1Amount());
                    craftSuccess = true;
                    break;
                }
            }
            if(!craftSuccess)
                return -1;
        }
        if (item.getIngredient2() != null) {
            for (int j = 0; j < userMaterials.size(); j++) {
                if (userMaterials.get(j).getName().equals(item.getIngredient2()) && userMaterials.get(j).getAmount() > item.getIngredient2Amount()) {
                    inventoryRepository.removeItemAmount(user.getId(), itemRepository.findByName(item.getIngredient2()).getId(), item.getIngredient2Amount());
                    craftSuccess = true;
                }
            }
            if(!craftSuccess)
                return -1;
        }
        if (item.getIngredient3() != null) {
            for (int j = 0; j < userMaterials.size(); j++) {
                if (userMaterials.get(j).getName().equals(item.getIngredient3()) && userMaterials.get(j).getAmount() > item.getIngredient3Amount()) {
                    inventoryRepository.removeItemAmount(user.getId(), itemRepository.findByName(item.getIngredient3()).getId(), item.getIngredient3Amount());
                    craftSuccess = true;
                }
            }
            if(!craftSuccess)
                return -1;
        }
        if (item.getIngredient4() != null) {
            for (int j = 0; j < userMaterials.size(); j++) {
                if (userMaterials.get(j).getName().equals(item.getIngredient4()) && userMaterials.get(j).getAmount() > item.getIngredient4Amount()) {
                    inventoryRepository.removeItemAmount(user.getId(), itemRepository.findByName(item.getIngredient4()).getId(), item.getIngredient4Amount());
                    craftSuccess = true;
                }
            }
            if(!craftSuccess)
                return -1;
        }
        if (item.getIngredient5() != null) {
            for (int j = 0; j < userMaterials.size(); j++) {
                if (userMaterials.get(j).getName().equals(item.getIngredient5()) && userMaterials.get(j).getAmount() > item.getIngredient5Amount()) {
                    inventoryRepository.removeItemAmount(user.getId(), itemRepository.findByName(item.getIngredient5()).getId(), item.getIngredient5Amount());
                    craftSuccess = true;
                }
            }
            if(!craftSuccess)
                return -1;
        }

        craftRandomEquip(user,item);
        userRepository.updateGold(user.getId(),(int)user.getGold()-item.getGold());
        return user.getGold();

    }

    public void craftRandomEquip(GameUser user,CraftEquip item){
        double rand = Math.random()*100;
        String craftedItem = "Test Sword";
        if(item.getEquip_type().equals("Helmet")) {
            List<EquippedItem> randomize = itemRepository.getAllHelmets();
            Collections.shuffle(randomize);
            if(rand >97)
                craftedItem = searchCraftArray(randomize,"Artifact");
            else if(rand <=97 && rand >90)
                craftedItem = searchCraftArray(randomize,"Legendary");
            else if(rand <=90 && rand >78)
                craftedItem = searchCraftArray(randomize,"Mythic");
            else if(rand <=78 && rand >60)
                craftedItem = searchCraftArray(randomize,"Rare");
            else if(rand <=60 && rand >35)
                craftedItem = searchCraftArray(randomize,"Uncommon");
            else if(rand <=35 && rand >=0)
                craftedItem = searchCraftArray(randomize,"Common");
        }
        else if(item.getEquip_type().equals("Ring")){
            List<EquippedItem> randomize = itemRepository.getAllRings();
            Collections.shuffle(randomize);
            if(rand >97)
                craftedItem = searchCraftArray(randomize,"Artifact");
            else if(rand <=97 && rand >90)
                craftedItem = searchCraftArray(randomize,"Legendary");
            else if(rand <=90 && rand >78)
                craftedItem = searchCraftArray(randomize,"Mythic");
            else if(rand <=78 && rand >60)
                craftedItem = searchCraftArray(randomize,"Rare");
            else if(rand <=60 && rand >35)
                craftedItem = searchCraftArray(randomize,"Uncommon");
            else if(rand <=35 && rand >=0)
                craftedItem = searchCraftArray(randomize,"Common");
        }
        else if(item.getEquip_type().equals("Weapon")){
            List<EquippedItem> randomize = itemRepository.getAllWeapons();
            Collections.shuffle(randomize);
            if(rand >97)
                craftedItem = searchCraftArray(randomize,"Artifact");
            else if(rand <=97 && rand >90)
                craftedItem = searchCraftArray(randomize,"Legendary");
            else if(rand <=90 && rand >78)
                craftedItem = searchCraftArray(randomize,"Mythic");
            else if(rand <=78 && rand >60)
                craftedItem = searchCraftArray(randomize,"Rare");
            else if(rand <=60 && rand >35)
                craftedItem = searchCraftArray(randomize,"Uncommon");
            else if(rand <=35 && rand >=0)
                craftedItem = searchCraftArray(randomize,"Common");
        }
        else if(item.getEquip_type().equals("Chest")){
            List<EquippedItem> randomize = itemRepository.getAllChests();
            Collections.shuffle(randomize);
            if(rand >97)
                craftedItem = searchCraftArray(randomize,"Artifact");
            else if(rand <=97 && rand >90)
                craftedItem = searchCraftArray(randomize,"Legendary");
            else if(rand <=90 && rand >78)
                craftedItem = searchCraftArray(randomize,"Mythic");
            else if(rand <=78 && rand >60)
                craftedItem = searchCraftArray(randomize,"Rare");
            else if(rand <=60 && rand >35)
                craftedItem = searchCraftArray(randomize,"Uncommon");
            else if(rand <=35 && rand >=0)
                craftedItem = searchCraftArray(randomize,"Common");
        }
        else if(item.getEquip_type().equals("Legging")){
            List<EquippedItem> randomize = itemRepository.getAllLeggings();
            Collections.shuffle(randomize);
            if(rand >97)
                craftedItem = searchCraftArray(randomize,"Artifact");
            else if(rand <=97 && rand >90)
                craftedItem = searchCraftArray(randomize,"Legendary");
            else if(rand <=90 && rand >78)
                craftedItem = searchCraftArray(randomize,"Mythic");
            else if(rand <=78 && rand >60)
                craftedItem = searchCraftArray(randomize,"Rare");
            else if(rand <=60 && rand >35)
                craftedItem = searchCraftArray(randomize,"Uncommon");
            else if(rand <=35 && rand >=0)
                craftedItem = searchCraftArray(randomize,"Common");
        }
        else if(item.getEquip_type().equals("Boots")){
            List<EquippedItem> randomize = itemRepository.getAllBoots();
            Collections.shuffle(randomize);
            if(rand >97)
                craftedItem = searchCraftArray(randomize,"Artifact");
            else if(rand <=97 && rand >90)
                craftedItem = searchCraftArray(randomize,"Legendary");
            else if(rand <=90 && rand >78)
                craftedItem = searchCraftArray(randomize,"Mythic");
            else if(rand <=78 && rand >60)
                craftedItem = searchCraftArray(randomize,"Rare");
            else if(rand <=60 && rand >35)
                craftedItem = searchCraftArray(randomize,"Uncommon");
            else if(rand <=35 && rand >=0)
                craftedItem = searchCraftArray(randomize,"Common");
        }
        Item mysteryCraft = itemRepository.findByName(craftedItem);
        System.out.print(mysteryCraft.getName());
        if(inventoryRepository.itemExists(user.getId(),mysteryCraft.getId()) !=1)
            inventoryRepository.addNewItemAmount(user.getId(),mysteryCraft.getId(),1);
        else
            inventoryRepository.addItemAmount(user.getId(),mysteryCraft.getId(),1);
    }

    public String searchCraftArray(List<EquippedItem> craftEquips,String rarity){
        for(EquippedItem prize: craftEquips){
            if(prize.getRarity().equals(rarity)){
                return prize.getName();
            }
        }
        return "Test Sword";
    }

    public void addCraftedItemToInv(GameUser user, CraftedItem item){
        Item crafted = itemRepository.findByName(item.getName());
        if(inventoryRepository.itemExists(user.getId(),crafted.getId()) != 1){
            inventoryRepository.addNewItemAmount(user.getId(),crafted.getId(),1);
        }
        else
            inventoryRepository.addItemAmount(user.getId(),crafted.getId(),1);
    }

    @RequestMapping("/api/tavern/lucky")
    public void userWonPrize(@RequestBody String prize){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        GameUser user = userRepository.findByUsername(username);
        if(prize.equals("500") || prize.equals("20000") || prize.equals("5000")){
            userRepository.updateGold(user.getId(),(Integer.valueOf(prize) +(int) user.getGold()));
        }
        else if(prize.equals("mystery")){
            double random = Math.random()*100;
            if(random >= 90){
                if(inventoryRepository.itemExists(user.getId(),1001) == 1){
                    inventoryRepository.addItem(user.getId(),1001);
                }
                else
                    inventoryRepository.addNewItem(user.getId(),1001);
            }
            else if (random < 90 && random >=50){
                if(inventoryRepository.itemExists(user.getId(),17) == 1){
                    inventoryRepository.addItem(user.getId(),17);
                }
                else
                    inventoryRepository.addNewItem(user.getId(),17);
            }
            else if(random <50 && random >=25){
                userRepository.updateGold(user.getId(), (int)user.getGold()+1000);
            }
        }
        else if(prize.equals("enchant")){
            if(inventoryRepository.itemExists(user.getId(),15) == 1){
                inventoryRepository.addItem(user.getId(),15);
            }
            else
                inventoryRepository.addNewItem(user.getId(),15);
        }
        else if(prize.equals("lvl")){
            if(inventoryRepository.itemExists(user.getId(),14) == 1){
                inventoryRepository.addItem(user.getId(),14);
            }
            else
                inventoryRepository.addNewItem(user.getId(),14);
        }
        else if(prize.equals("potion")){
            if(inventoryRepository.itemExists(user.getId(),4) == 1){
                inventoryRepository.addItemAmount(user.getId(),4,10);
            }
            else
                inventoryRepository.addNewItemAmount(user.getId(),4,10);
        }
        else {
            if(inventoryRepository.itemExists(user.getId(),16) == 1){
                inventoryRepository.addItem(user.getId(),16);
            }
            else
                inventoryRepository.addNewItem(user.getId(),16);
        }

    }

    @RequestMapping("/api/tavern/getCraftables")
    public List<Object> getCraftableItems(){
        List<Object> allCrafts = new ArrayList<>();
        List<CraftEquip> craftEquips = new ArrayList<>();
        CraftEquip weapon = new CraftEquip("Weapon","Golem Artifact",3,"Wolf Pelt",4,"Pixie Dust",2,null,0,null,0,750);
        CraftEquip helmet = new CraftEquip("Helmet","Golem Artifact",1,"Wolf Pelt",2,"Pixie Dust",1,null,0,null,0,750);
        CraftEquip legging = new CraftEquip("Legging","Golem Artifact",2,"Wolf Pelt",3,"Pixie Dust",1,null,0,null,0,750);
        CraftEquip chest = new CraftEquip("Chest","Golem Artifact",2,"Wolf Pelt",4,"Pixie Dust",2,null,0,null,0,750);
        CraftEquip boots = new CraftEquip("Boots","Golem Artifact",1,"Wolf Pelt",2,"Pixie Dust",1,null,0,null,0,750);
        CraftEquip ring = new CraftEquip("Ring","Golem Artifact",2,"Wolf Pelt",3,"Pixie Dust",2,null,0,null,0,750);
        craftEquips.add(weapon);
        craftEquips.add(helmet);
        craftEquips.add(legging);
        craftEquips.add(chest);
        craftEquips.add(boots);
        craftEquips.add(ring);
        allCrafts.add(craftEquips);
        allCrafts.add(inventoryRepository.getAllCraftableItems());
        return allCrafts;
    }

}
