package com.IdleAdventure.adventuregame.repository;

import com.IdleAdventure.adventuregame.model.Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface StatsRepository extends JpaRepository<Stats,Integer> {

    String Q_UPDATE_STATS="UPDATE stats s set s.attack=:attack,s.defense=:defense,s.hp=:hp,s.crit_chance=:crit where user_id =:u_id";
    @Query(value=Q_UPDATE_STATS,nativeQuery = true)
    @Modifying
    @Transactional
    void updateStats(@Param("attack") int attack,@Param("defense") int defense,@Param("hp") int hp,@Param("crit") int crit,@Param("u_id") int u_id);


}
