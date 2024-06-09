package dev.tdwalsh.project.tabletopBeholder.resource;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncounterHelper {
    public static Encounter provideEncounter(Integer mod) {
        Encounter encounter = new Encounter();
        encounter.setUserEmail("userEmail" + mod);
        encounter.setObjectId("objectId" + mod);
        encounter.setObjectName("objectName" + mod);
        Map<String, Creature> creatureMap = new HashMap<>();
        Creature creature1 = CreatureHelper.provideCreature(1);
        Creature creature2 = CreatureHelper.provideCreature(2);
        creatureMap.put(creature1.getObjectId(), creature1);
        creatureMap.put(creature2.getObjectId(), creature2);
        encounter.setEncounterTurn(mod);
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");
        stringList.add("test2");
        encounter.setTurnOrder(stringList);
        encounter.setTopOfOrder("topOfOrder" + mod);
        encounter.setSession("belongsToSession" + mod);
        encounter.setCreatureMap(creatureMap);
        encounter.setCreateDateTime(ZonedDateTime.now());
        encounter.setEditDateTime(ZonedDateTime.now());
        return encounter;
    }
}
