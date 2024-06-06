package dev.tdwalsh.project.tabletopBeholder.resource;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class EncounterHelper {
    public static Encounter provideEncounter(Integer mod) {
        Encounter encounter = new Encounter();
        encounter.setUserEmail("userEmail" + mod);
        encounter.setObjectId("objectId" + mod);
        encounter.setObjectName("objectName" + mod);
        List<Creature> creatureList = new ArrayList<>();
        creatureList.add(CreatureHelper.provideCreature(1));
        creatureList.add(CreatureHelper.provideCreature(2));
        encounter.setEncounterTurn(mod);
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");
        stringList.add("test2");
        encounter.setTurnOrder(stringList);
        encounter.setTopOfOrder("topOfOrder");
        encounter.setCreatureList(creatureList);
        encounter.setCreateDateTime(ZonedDateTime.now());
        encounter.setEditDateTime(ZonedDateTime.now());
        return encounter;
    }
}
