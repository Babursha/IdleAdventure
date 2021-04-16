package com.IdleAdventure.adventuregame.repository;

import com.IdleAdventure.adventuregame.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Integer> {
    String Q_GET_USER_EQUIPS = "select name,description,level,price,rarity,craftable,sell,item_type,attack,crit_chance," +
            "defense,hp,equip_type,amount from item inner join equipment on item.id = equipment.item_id inner join inventory i " +
            "on i.item_id = item.id where user_id = :u_id";
    @Query(value = Q_GET_USER_EQUIPS,nativeQuery = true)
    List<ItemEquipment> getAllEquipItems(@Param("u_id") int u_id);

    String Q_GET_USER_POTIONS = "select name,description,level,item_type,heal,duration,price,sell,rarity,craftable,buff,amount from " +
            "item inner join potion on item.id = potion.item_id inner join inventory i on i.item_id = item.id where user_id = :u_id ";
    @Query(value = Q_GET_USER_POTIONS,nativeQuery = true)
    List<ItemPotion> getAllPotionItems(@Param("u_id") int u_id);

    String Q_GET_USER_LOOTBAGS = "select name,description,level,item_type,price,sell,rarity,craftable,amount from" +
            " item inner join inventory on item.id = inventory.item_id and item_type=\"Mystery\" where user_id = :u_id ";
    @Query(value = Q_GET_USER_LOOTBAGS,nativeQuery = true)
    List<ItemLootBag> getAllLootBagItems(@Param("u_id") int u_id);

    String Q_GET_ALL_CRAFT_ITEMS = "select name,description,level,item_type,sell,rarity,ingredient1,ingredient1Amount,ingredient2,ingredient2Amount," +
            "ingredient3,ingredient3Amount,ingredient4,ingredient4Amount,ingredient5,ingredient5Amount,gold from item i inner join craft_item c where i.id = c.item_id";
    @Query(value = Q_GET_ALL_CRAFT_ITEMS, nativeQuery = true)
    List<ItemCraftable> getAllCraftableItems();

    String Q_GET_CRAFT_ITEM = "select name,description,level,item_type,sell,rarity,ingredient1,ingredient1Amount,ingredient2,ingredient2Amount," +
            "ingredient3,ingredient3Amount,ingredient4,ingredient4Amount,ingredient5,ingredient5Amount,gold from item i inner join craft_item c where i.id = c.item_id and c.item_id = :i_id;";
    @Query(value = Q_GET_CRAFT_ITEM, nativeQuery = true)
    List<ItemCraftable> getCraftableItem(@Param("i_id") int i_id);

    String Q_GET_USER_CRAFT_MATERIALS = "select name,amount,description,level,item_type,price,sell,rarity from inventory inv inner join item i where i.id = inv.item_id and inv.user_id=:u_id and i.item_type = \"Material\";";
    @Query(value=Q_GET_USER_CRAFT_MATERIALS,nativeQuery = true)
    List<UserCraftMaterial> getAllUserMaterials(@Param("u_id")int u_id);

    String Q_ITEM_EXISTS = "SELECT (EXISTS (SELECT 1 FROM inventory WHERE user_id = :u_id and item_id = :i_id))";
    @Query(value = Q_ITEM_EXISTS,nativeQuery = true)
    int itemExists(@Param("u_id") int u_id, @Param("i_id") int i_id);

    String Q_ITEM_ADD = "UPDATE inventory i set i.amount = i.amount+1 where user_id = :u_id and item_id = :i_id";
    @Modifying
    @Transactional
    @Query(value= Q_ITEM_ADD,nativeQuery = true)
    int addItem(@Param("u_id") int u_id, @Param("i_id") int i_id);

    String Q_ITEM_ADD_AMOUNT = "UPDATE inventory i set i.amount = i.amount + :amount where user_id = :u_id and item_id = :i_id";
    @Modifying
    @Transactional
    @Query(value= Q_ITEM_ADD_AMOUNT,nativeQuery = true)
    int addItemAmount(@Param("u_id") int u_id, @Param("i_id") int i_id,@Param("amount")int amount);

    String Q_ITEM_ADD_NEW = "INSERT INTO inventory (amount,item_id,user_id) VALUES (1,:i_id,:u_id)";
    @Modifying
    @Transactional
    @Query(value=Q_ITEM_ADD_NEW,nativeQuery = true)
    int addNewItem(@Param("i_id")int i_id,@Param("u_id")int u_id);

    String Q_ITEM_ADD_NEW_AMOUNT = "INSERT INTO inventory (amount,item_id,user_id) VALUES (:quantity,:i_id,:u_id)";
    @Modifying
    @Transactional
    @Query(value=Q_ITEM_ADD_NEW_AMOUNT,nativeQuery = true)
    int addNewItemAmount(@Param("u_id")int u_id,@Param("i_id")int i_id,@Param("quantity")int quantity);

    String Q_ITEM_REMOVE = "UPDATE inventory i set i.amount = i.amount-1 where user_id = :u_id and item_id = :i_id";
    @Modifying
    @Transactional
    @Query(value = Q_ITEM_REMOVE,nativeQuery = true)
    void removeItem(@Param("u_id")int u_id, @Param("i_id") int i_id);

    String Q_ITEM_REMOVE_AMOUNT = "UPDATE inventory i set i.amount = i.amount- :amount where user_id = :u_id and item_id = :i_id";
    @Modifying
    @Transactional
    @Query(value = Q_ITEM_REMOVE_AMOUNT,nativeQuery = true)
    void removeItemAmount(@Param("u_id")int u_id, @Param("i_id") int i_id,@Param("amount")int amount);

    String Q_ZERO_LEFT = "DELETE FROM inventory where user_id = :u_id and item_id = :i_id and amount <=0";
    @Modifying
    @Transactional
    @Query(value=Q_ZERO_LEFT,nativeQuery = true)
    void deleteEntry(@Param("u_id") int u_id, @Param("i_id") int i_id);
}
