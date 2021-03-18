package com.IdleAdventure.adventuregame.repository;

import com.IdleAdventure.adventuregame.model.EquippedItem;
import com.IdleAdventure.adventuregame.model.Item;
import com.IdleAdventure.adventuregame.model.ItemEquipment;
import com.IdleAdventure.adventuregame.model.ItemPotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Integer> {
    Item findByName(String name);
    Item findById(int id);

    String GET_EQUIPPED_ITEM_DETAILS = "SELECT name,description,level,rarity,sell,attack,crit_chance,defense,hp,equip_type from item inner join equipment on item.id = equipment.item_id where item.id = :i_id";
    @Query(value=GET_EQUIPPED_ITEM_DETAILS,nativeQuery = true)
    EquippedItem getEquippedItemDetails(@Param("i_id")int i_id);

    String GET_SHOP_ITEMS = "SELECT * FROM item ORDER BY RAND() LIMIT 10";
    @Query(value = GET_SHOP_ITEMS,nativeQuery = true)
    List<Item> getShopItems();

    String Q_GET_USER_EQUIPS = "select name,description,level,price,rarity,craftable,sell,item_type,attack,crit_chance," +
            "defense,hp,equip_type from item inner join equipment on item.id = equipment.item_id ORDER BY RAND() LIMIT 8";
    @Query(value = Q_GET_USER_EQUIPS,nativeQuery = true)
    List<ItemEquipment> getAllEquipments();

    String Q_GET_USER_POTIONS = "select name,description,level,item_type,heal,duration,price,sell,rarity,craftable,buff from " +
            "item inner join potion on item.id = potion.item_id ORDER BY RAND() LIMIT 2";
    @Query(value = Q_GET_USER_POTIONS,nativeQuery = true)
    List<ItemPotion> getAllPotions();
}
