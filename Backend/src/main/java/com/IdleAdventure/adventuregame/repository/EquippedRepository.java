package com.IdleAdventure.adventuregame.repository;

import com.IdleAdventure.adventuregame.model.Equipped;
import com.IdleAdventure.adventuregame.model.EquippedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EquippedRepository extends JpaRepository<Equipped,Integer> {

    String Q_INITALIZE="INSERT INTO equipped(user_id) VALUES(:u_id)";
    @Modifying
    @Transactional
    @Query(value=Q_INITALIZE,nativeQuery = true)
    void initalizeEquipped(@Param("u_id")int u_id);

    String Q_GET_EQUIPPED_ITEMS = "SELECT helmet_id,chest_id,legging_id,boots_id,weapon_id,ring_id FROM equipped WHERE user_id = :u_id";
    @Query(value = Q_GET_EQUIPPED_ITEMS,nativeQuery = true)
    EquippedId get_equipped_items(@Param("u_id") int u_id);

    String Q_EQUIP_WEAPON = "update equipped e set e.weapon_id = :i_id where e.user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value = Q_EQUIP_WEAPON, nativeQuery=true)
    void equip_weapon(@Param("u_id") int u_id, @Param("i_id") int i_id);

    String Q_EQUIP_CHEST = "update equipped e set e.chest_id = :i_id where e.user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value = Q_EQUIP_CHEST, nativeQuery=true)
    void equip_chest(@Param("u_id") int u_id, @Param("i_id") int i_id);

    String Q_EQUIP_LEGGING = "update equipped e set e.legging_id = :i_id where e.user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value = Q_EQUIP_LEGGING, nativeQuery=true)
    void equip_legging(@Param("u_id") int u_id, @Param("i_id") int i_id);

    String Q_EQUIP_BOOTS = "update equipped e set e.boots_id = :i_id where e.user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value = Q_EQUIP_BOOTS, nativeQuery=true)
    void equip_boots(@Param("u_id") int u_id, @Param("i_id") int i_id);

    String Q_EQUIP_HELMET = "update equipped e set e.helmet_id = :i_id where e.user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value = Q_EQUIP_HELMET, nativeQuery=true)
    void equip_helmet(@Param("u_id") int u_id, @Param("i_id") int i_id);

    String Q_EQUIP_RING = "update equipped e set e.ring_id = :i_id where e.user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value = Q_EQUIP_RING, nativeQuery=true)
    void equip_ring(@Param("u_id") int u_id, @Param("i_id") int i_id);


    String Q_UNEQUIP_BOOTS ="update equipped e set boots_id = null where e.user_id=:u_id";
    @Modifying
    @Transactional
    @Query(value = Q_UNEQUIP_BOOTS, nativeQuery = true)
    void unequip_boots(@Param("u_id")int u_id);

    String Q_UNEQUIP_WEAPON ="update equipped e set weapon_id = null where e.user_id=:u_id";
    @Modifying
    @Transactional
    @Query(value = Q_UNEQUIP_WEAPON, nativeQuery = true)
    void unequip_weapon(@Param("u_id")int u_id);

    String Q_UNEQUIP_CHEST ="update equipped e set chest_id = null where e.user_id=:u_id";
    @Modifying
    @Transactional
    @Query(value = Q_UNEQUIP_CHEST, nativeQuery = true)
    void unequip_chest(@Param("u_id")int u_id);

    String Q_UNEQUIP_HELMET ="update equipped e set helmet_id = null where e.user_id=:u_id";
    @Modifying
    @Transactional
    @Query(value = Q_UNEQUIP_HELMET, nativeQuery = true)
    void unequip_helmet(@Param("u_id")int u_id);

    String Q_UNEQUIP_LEGGING ="update equipped e set legging_id = null where e.user_id=:u_id";
    @Modifying
    @Transactional
    @Query(value = Q_UNEQUIP_LEGGING, nativeQuery = true)
    void unequip_legging(@Param("u_id")int u_id);

    String Q_UNEQUIP_RING ="update equipped e set ring_id = null where e.user_id=:u_id";
    @Modifying
    @Transactional
    @Query(value = Q_UNEQUIP_RING, nativeQuery = true)
    void unequip_ring(@Param("u_id")int u_id);
}
