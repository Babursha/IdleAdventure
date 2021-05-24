package com.IdleAdventure.adventuregame.repository;

import com.IdleAdventure.adventuregame.model.GameUser;
import com.IdleAdventure.adventuregame.model.UserHomeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;


@Repository
public interface UserRepository extends JpaRepository<GameUser,Integer> {
    GameUser findByUsername(String username);
    GameUser findByEmail(String email);


    String Q_CHANGEPASSWORD = "UPDATE user u set u.password = :pass where username = :username";
    @Modifying
    @Transactional
    @Query(value= Q_CHANGEPASSWORD,nativeQuery = true)
    void changePassword(@Param("username") String username, @Param("pass") String pass);

    String Q_WHEEL_TIMER = "UPDATE user u set u.start_wheel = :start, u.end_wheel = :end where u.id = :u_id";
    @Modifying
    @Transactional
    @Query(value= Q_WHEEL_TIMER,nativeQuery = true)
    void spinWheel(@Param("u_id")int u_id, @Param("start") Date start, @Param("end") Date end);

    String Q_USER_HOME_DETAILS= "SELECT username,level,xp_current,xp_gained,xp_lvl_up,xp_progress,gold,gold_progress,hp,defense,attack,crit_chance,end_wheel from user INNER JOIN stats ON user.id=stats.user_id WHERE user.id = :u_id";
    @Query(value = Q_USER_HOME_DETAILS,nativeQuery = true)
    UserHomeDetails getUserDetails(@Param("u_id") int u_id);

    String Q_COLLECT_GOLD="UPDATE user u SET u.gold = :gold WHERE u.id = :u_id";
    @Modifying
    @Transactional
    @Query(value=Q_COLLECT_GOLD,nativeQuery = true)
    void updateGold(@Param("u_id")int u_id,@Param("gold")int gold);

    String Q_COLLECT_XP="UPDATE stats s SET s.level = :level,s.xp_current=:xp_curr,s.xp_lvl_up =:xp_lvl WHERE s.user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value=Q_COLLECT_XP,nativeQuery = true)
    void updateXp(@Param("u_id")int u_id,@Param("level")int level,@Param("xp_curr") int xp_current,@Param("xp_lvl") int xp_lvl_up);

    String Q_UPDATE_STATS = "UPDATE user u set u.attack = :attack where  iduser = :u_id";
    @Modifying
    @Transactional
    @Query(value = Q_UPDATE_STATS,nativeQuery = true)
    void updateStats(@Param("u_id") int u_id, @Param("attack") int attack);

    String Q_UPDATE_LOOT = "UPDATE user u set u.gold = :gold, u.xp_gained = :xpGain, u.xp_current = :xpCurrent,u.xp_lvl_up = :xpLvlUp, u.level = :level where iduser = :u_id";
    @Modifying
    @Transactional
    @Query(value = Q_UPDATE_LOOT,nativeQuery = true)
    void updateLoot(@Param("u_id") int u_id,@Param("gold")double gold,@Param("xpGain") int xpGain,@Param("xpCurrent") int xpCurrent,@Param("xpLvlUp") int xpLvlUp,@Param("level")int level);

    String Q_SAVE_GOLD_PROGRESS = "UPDATE user u set u.gold_progress = :gold_progress where  id = :u_id";
    @Modifying
    @Transactional
    @Query(value = Q_SAVE_GOLD_PROGRESS,nativeQuery = true)
    void saveGoldProgress(@Param("u_id") int u_id, @Param("gold_progress") int gold_progress);

    String Q_SAVE_XP_PROGRESS = "UPDATE stats s set s.xp_progress = :xp_progress where  user_id = :u_id";
    @Modifying
    @Transactional
    @Query(value = Q_SAVE_XP_PROGRESS,nativeQuery = true)
    void saveXpProgress(@Param("u_id") int u_id, @Param("xp_progress") int xp_progress);
}
