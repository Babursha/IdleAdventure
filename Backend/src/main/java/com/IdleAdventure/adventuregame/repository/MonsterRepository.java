package com.IdleAdventure.adventuregame.repository;

import com.IdleAdventure.adventuregame.model.Monster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonsterRepository extends JpaRepository<Monster,Integer> {
    String GET_FOREST_MONSTERS = "select * from monster where location = 'forest'";
    @Query(value = GET_FOREST_MONSTERS,nativeQuery = true)
    List<Monster> getForestMonsters();

    String GET_DESERT_MONSTERS = "select * from monster where location = 'desert'";
    @Query(value = GET_DESERT_MONSTERS, nativeQuery = true)
    List<Monster> getDesertMonsters();
}
