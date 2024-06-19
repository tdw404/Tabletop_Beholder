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

    /**
     * Constructor for RunActivities class.
     */
    public RunActivities() {
    }

    /**
     * Uses provided details to set the encounter's turn order following initiative roll.
     * @param encounter Old state {@link Encounter} object to be updated.
     * @param body Details to complete task.
     * @return Updated encounter object.
     */
    public Encounter setInitiative(Encounter encounter, JSONObject body) {
        JSONArray array = body.getJSONArray("queueList");
        Queue<String> turnQueue = new PriorityQueue<>();
        array.forEach(arrayItem -> turnQueue.add(arrayItem.toString()));
        encounter.setTurnQueue(turnQueue);
        encounter.setEncounterRound(1);
        encounter.setTopOfOrder(turnQueue.peek());
        encounter.setEditDateTime(ZonedDateTime.now());
        return encounter;
    }

    /**
     * Iterates to next turn in order.
     * @param encounter Old state {@link Encounter} object to be updated.
     * @return Updated encounter object.
     */
    public Encounter nextTurn(Encounter encounter) {
        Queue<String> turnQueue = encounter.getTurnQueue();
        turnQueue.add(turnQueue.remove());
        encounter.setTurnQueue(turnQueue);
        if (encounter.getTopOfOrder().equals(turnQueue.peek())) {
            encounter.setEncounterRound(encounter.getEncounterRound() + 1);
        }
        return encounter;
    }

    /**
     * Uses provided details to apply damage to target creature.
     * @param encounter Old state {@link Encounter} object to be updated.
     * @param body Details to complete task.
     * @return Updated encounter object.
     */
    public Encounter applyDamage(Encounter encounter, JSONObject body) {
        Map<String, Creature> creatureMap = encounter.getCreatureMap();
        Creature creature = creatureMap.get(body.getString("targetId"));
        int damageValue = Integer.parseInt(body.getString("damageValue"));
        if (!creature.getIsPC()) {
            creature.setCurrentHitPoints(creature.getCurrentHitPoints() - damageValue);
            if (creature.getCurrentHitPoints() + creature.getHitPoints() < 0) {
                creature.setKnockedOut(false);
                creature.setDead(true);
                creature.setCurrentHitPoints(0);
            } else if (creature.getCurrentHitPoints() <= 0) {
                creature.setDead(true);
                creature.setCurrentHitPoints(0);
            }
        }
        creatureMap.put(creature.getEncounterCreatureId(), creature);
        encounter.setCreatureMap(creatureMap);
        return encounter;
    }

    /**
     * Uses provided details to heal target creature.
     * @param encounter Old state {@link Encounter} object to be updated.
     * @param body Details to complete task.
     * @return Updated encounter object.
     */
    public Encounter heal(Encounter encounter, JSONObject body) {
        Map<String, Creature> creatureMap = encounter.getCreatureMap();
        Creature creature = creatureMap.get(body.getString("targetId"));
        int damageValue = Integer.parseInt(body.getString("damageValue"));
        if (!creature.getIsPC()) {
            creature.setCurrentHitPoints(Math.min(
                    creature.getCurrentHitPoints() + damageValue,
                    creature.getHitPoints()));
            creature.setDead(false);
            creature.setKnockedOut(false);
        }
        creatureMap.put(creature.getEncounterCreatureId(), creature);
        encounter.setCreatureMap(creatureMap);
        return encounter;
    }

    /**
     * Uses provided details to set target creature to unconscious, sets hp to zero.
     * @param encounter Old state {@link Encounter} object to be updated.
     * @param body Details to complete task.
     * @return Updated encounter object.
     */
    public Encounter knockOut(Encounter encounter, JSONObject body) {
        Map<String, Creature> creatureMap = encounter.getCreatureMap();
        Creature creature = creatureMap.get(body.getString("targetId"));
        creature.setKnockedOut(true);
        creature.setDead(false);
        creature.setCurrentHitPoints(0);
        creatureMap.put(creature.getEncounterCreatureId(), creature);
        encounter.setCreatureMap(creatureMap);
        return encounter;
    }

    /**
     * Uses provided details to set target creature to dead, sets hp to zero.
     * @param encounter Old state {@link Encounter} object to be updated.
     * @param body Details to complete task.
     * @return Updated encounter object.
     */
    public Encounter kill(Encounter encounter, JSONObject body) {
        Map<String, Creature> creatureMap = encounter.getCreatureMap();
        Creature creature = creatureMap.get(body.getString("targetId"));
        creature.setKnockedOut(false);
        creature.setDead(true);
        creature.setCurrentHitPoints(0);
        creatureMap.put(creature.getEncounterCreatureId(), creature);
        encounter.setCreatureMap(creatureMap);
        return encounter;
    }
}
