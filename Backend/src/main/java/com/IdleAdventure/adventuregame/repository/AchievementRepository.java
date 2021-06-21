package com.IdleAdventure.adventuregame.repository;

import com.IdleAdventure.adventuregame.model.Achievement;
import com.IdleAdventure.adventuregame.model.Badges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface AchievementRepository extends JpaRepository<Achievement,Integer> {

    String Q_GET_USER_BADGES="SELECT total_f_monsters,total_d_monsters,total_c_monsters,total_v_monsters,total_s_monsters from achievement a where a.user_id = :u_id";
    @Query(value=Q_GET_USER_BADGES,nativeQuery = true)
    Badges getUserBadges(@Param("u_id")int u_id);

    String Q_UPDATE_TOTAL_F_MONSTERS = "UPDATE achievement a set a.total_f_monsters = a.total_f_monsters +:add where user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value= Q_UPDATE_TOTAL_F_MONSTERS,nativeQuery = true)
    void updateFMonsters(@Param("u_id") int u_id,@Param("add") int add);

    String Q_UPDATE_TOTAL_D_MONSTERS = "UPDATE achievement a set a.total_d_monsters = a.total_d_monsters +:add where user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value= Q_UPDATE_TOTAL_D_MONSTERS,nativeQuery = true)
    void updateDMonsters(@Param("u_id") int u_id,@Param("add") int add);

    String Q_UPDATE_TOTAL_C_MONSTERS = "UPDATE achievement a set a.total_c_monsters = a.total_c_monsters +:add where user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value= Q_UPDATE_TOTAL_C_MONSTERS,nativeQuery = true)
    void updateCMonsters(@Param("u_id") int u_id,@Param("add") int add);

    String Q_UPDATE_TOTAL_V_MONSTERS = "UPDATE achievement a set a.total_v_monsters = a.total_v_monsters +:add where user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value= Q_UPDATE_TOTAL_V_MONSTERS,nativeQuery = true)
    void updateVMonsters(@Param("u_id") int u_id,@Param("add") int add);

    String Q_UPDATE_TOTAL_S_MONSTERS = "UPDATE achievement a set a.total_s_monsters = a.total_s_monsters +:add where user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value= Q_UPDATE_TOTAL_S_MONSTERS,nativeQuery = true)
    void updateSMonsters(@Param("u_id") int u_id,@Param("add") int add);
}
