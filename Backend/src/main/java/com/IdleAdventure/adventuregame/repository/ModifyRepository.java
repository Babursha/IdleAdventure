package com.IdleAdventure.adventuregame.repository;


import com.IdleAdventure.adventuregame.model.Modify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ModifyRepository extends JpaRepository<Modify,Integer> {

    String Q_ITEM_ADD_NEW = "INSERT INTO modify (item_id,user_id,hp,defense,attack,crit_chance) VALUES (:i_id,:u_id,:hp,:defense,:attack,:crit_chance)";
    @Modifying
    @Transactional
    @Query(value=Q_ITEM_ADD_NEW,nativeQuery = true)
    int addNewItem(@Param("i_id")int i_id, @Param("u_id")int u_id, @Param("hp")int hp, @Param("defense")int defense, @Param("attack")int attack, @Param("crit_chance")int critChance);

}
