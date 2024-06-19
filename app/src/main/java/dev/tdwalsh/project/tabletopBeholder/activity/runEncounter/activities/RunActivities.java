package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.activities;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class RunActivities {

    public RunActivities() {
    }

    public Encounter setInitiative(Encounter encounter, JSONObject body) {
        JSONArray array = body.getJSONArray("queueList");
        Queue<String> turnQueue = new PriorityQueue<>();
        for (int i = 0; i < array.length(); i++) {
            turnQueue.add(array.get(i).toString());
        }
        encounter.setTurnQueue(turnQueue);
        encounter.setEncounterRound(1);
        encounter.setTopOfOrder(turnQueue.peek());
        encounter.setEditDateTime(ZonedDateTime.now());
        return encounter;
    }

    public Encounter nextTurn(Encounter encounter) {
        Queue<String> turnQueue = encounter.getTurnQueue();
        turnQueue.add(turnQueue.remove());
        encounter.setTurnQueue(turnQueue);
        if (encounter.getTopOfOrder().equals(turnQueue.peek())) {
            encounter.setEncounterRound(encounter.getEncounterRound() + 1);
        }
        return encounter;
    }

    public Encounter applyDamage(Encounter encounter, JSONObject body) {
        Map<String, Creature> creatureMap = encounter.getCreatureMap();
        Creature creature = creatureMap.get(body.getString("targetId"));
        int damageValue = Integer.parseInt(body.getString("damageValue"));
        creature.setCurrentHitPoints(creature.getCurrentHitPoints() - damageValue);
        if (!creature.getIsPC()) {
            if (creature.getCurrentHitPoints() + creature.getHitPoints() < 0) {
                creature.setKnockedOut(false);
                creature.setDead(true);
                creature.setCurrentHitPoints(0);
            } else if (creature.getCurrentHitPoints() <= 0) {
                creature.setDead(true);
                creature.setCurrentHitPoints(0);
            };
        };
        creatureMap.put(creature.getEncounterCreatureId(), creature);
        encounter.setCreatureMap(creatureMap);
        return encounter;
    }
}
