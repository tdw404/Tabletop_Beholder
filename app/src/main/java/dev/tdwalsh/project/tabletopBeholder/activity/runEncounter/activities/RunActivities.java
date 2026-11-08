package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.activities;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MalformedInputException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.ls.LSOutput;

import java.sql.Array;
import java.time.ZonedDateTime;
import java.util.*;

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
        Queue<String> turnQueue = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();
        for (int index = 0; index < array.length(); index++) {
            String id = array.getString(index);
            if (encounter.getCreatureMap() == null || encounter.getCreatureMap().get(id) == null || !seen.add(id)) {
                throw new MalformedInputException("Initiative must contain distinct creatures in this encounter.");
            }
            turnQueue.add(id);
        }
        if (turnQueue.isEmpty()) {
            throw new MalformedInputException("Initiative must contain at least one creature.");
        }
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
        Queue<String> messageQueue = new PriorityQueue<>();
        Queue<String> turnQueue = encounter.getTurnQueue();
        if (turnQueue == null || turnQueue.isEmpty() || encounter.getTopOfOrder() == null
                || encounter.getCreatureMap() == null
                || turnQueue.stream().anyMatch(id -> encounter.getCreatureMap().get(id) == null)) {
            throw new MalformedInputException("Set initiative with the current roster before advancing the turn.");
        }
        String exitingCreatureId = turnQueue.peek();
        turnQueue.add(turnQueue.remove());
        String enteringCreatureId = turnQueue.peek();
        encounter.setTurnQueue(turnQueue);
        if (encounter.getTopOfOrder().equals(turnQueue.peek())) {
            encounter.setEncounterRound(encounter.getEncounterRound() + 1);
            messageQueue.add(String.format("This is the start of round %s!", encounter.getEncounterRound()));
        }

        Map<String, Creature> creatureMap = encounter.getCreatureMap();
        Creature exitingCreature = creatureMap.get(exitingCreatureId);
        Creature enteringCreature = creatureMap.get(enteringCreatureId);
        Map<String, Effect> exitingEffects = exitingCreature.getActiveEffects();
        Map<String, Effect> enteringEffects = enteringCreature.getActiveEffects();
        List<String> removeEffects = new ArrayList<>();
        Optional.ofNullable(exitingEffects).orElse(new HashMap<>()).values()
                .forEach(effect -> {
                    String blameName = "NONE";
                    if (!effect.getBlameCreatureId().equals("0")) {
                        blameName = creatureMap.get(effect.getBlameCreatureId()).getEncounterCreatureName();
                    }
                    effect
                        .setTurnDuration(Optional.ofNullable(
                                effect.getTurnDuration()).orElse(0) - 1);
                    if (effect.getTurnDuration() < 1) {
                        messageQueue.add(String.format("[%s] effect, caused by [%s], ended for [%s].",
                                effect.getEffectName(),
                                blameName,
                                exitingCreature.getEncounterCreatureName()));
                        removeEffects.add(effect.getObjectId());
                    } else if (effect.getSaveOn().contains("end")) {
                                messageQueue.add(String.format("[%s] needs to roll a [%s] save versus [%o], against [%s]'s [%s] effect.",
                                exitingCreature.getEncounterCreatureName(),
                                effect.getSaveType(),
                                effect.getSaveDC(),
                                blameName,
                                effect.getEffectName()));
                   }
                });
        removeEffects
                .forEach(effectId -> exitingEffects.remove(effectId));
        exitingCreature.setActiveEffects(exitingEffects);
        creatureMap.put(exitingCreatureId, exitingCreature);

        Optional.ofNullable(enteringEffects).orElse(new HashMap<>()).values()
                .forEach(effect -> {
                    if (effect.getSaveOn().contains("start")) {
                        String blameName = "NONE";
                        if (!effect.getBlameCreatureId().equals("0")) {
                            blameName = creatureMap.get(effect.getBlameCreatureId()).getEncounterCreatureName();
                        }
                        messageQueue.add(String.format("[%s] needs to roll a [%s] save versus [%o], against [%s]'s [%s] effect.",
                        enteringCreature.getEncounterCreatureName(),
                        effect.getSaveType(),
                        effect.getSaveDC(),
                        blameName,
                        effect.getEffectName()));
                    }
                });

        enteringCreature.setActiveEffects(enteringEffects);
        creatureMap.put(enteringCreatureId, enteringCreature);
        encounter.setCreatureMap(creatureMap);
        encounter.setMessageQueue(messageQueue);
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
        Creature creature = requireTarget(creatureMap, body);
        int damageValue = nonnegativeInteger(body, "damageValue", "Amount");
        if (!creature.getIsPC()) {
            creature.setCurrentHitPoints((int) Math.max(Integer.MIN_VALUE,
                    (long) creature.getCurrentHitPoints() - damageValue));
            if ((long) creature.getCurrentHitPoints() + creature.getHitPoints() < 0) {
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
        Creature creature = requireTarget(creatureMap, body);
        int damageValue = nonnegativeInteger(body, "damageValue", "Amount");
        if (!creature.getIsPC()) {
            creature.setCurrentHitPoints((int) Math.min(
                    (long) creature.getCurrentHitPoints() + damageValue,
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
        Creature creature = requireTarget(creatureMap, body);
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
        Creature creature = requireTarget(creatureMap, body);
        creature.setKnockedOut(false);
        creature.setDead(true);
        creature.setCurrentHitPoints(0);
        creatureMap.put(creature.getEncounterCreatureId(), creature);
        encounter.setCreatureMap(creatureMap);
        return encounter;
    }

    /**
     * Uses provided details to add an effect to a create.
     * @param encounter Old state {@link Encounter} object to be updated.
     * @param body Details to complete task.
     * @return Updated encounter object.
     */
    public Encounter addEffect(Encounter encounter, JSONObject body) {
        Map<String, Creature> creatureMap = encounter.getCreatureMap();
        Creature creature = requireTarget(creatureMap, body);
        Effect effect = new Effect();
        JSONObject jsonEffect = body.getJSONObject("effect");
        String effectId = jsonEffect.getString("objectId");
        String effectName = jsonEffect.getString("effectName");
        if (effectId.isBlank() || effectName.isBlank()) {
            throw new MalformedInputException("An effect ID and name are required.");
        }
        effect.setObjectId(effectId);
        effect.setEffectName(effectName);
        int duration = nonnegativeInteger(jsonEffect, "turnDuration", "Effect duration");
        if (duration == 0) {
            throw new MalformedInputException("Effect duration must be at least one turn.");
        }
        effect.setTurnDuration(duration);
        String sourceId = jsonEffect.getString("blameCreatureId");
        if (!"0".equals(sourceId) && creatureMap.get(sourceId) == null) {
            throw new MalformedInputException("The effect source must be a creature in this encounter.");
        }
        effect.setBlameCreatureId(sourceId);
        effect.setSaveType(jsonEffect.getString("saveType"));
        effect.setSaveDC(nonnegativeInteger(jsonEffect, "saveDC", "Save DC"));
        JSONArray array = jsonEffect.getJSONArray("saveOn");
        Set<String> stringSet = new HashSet<>();
        for (int index = 0; index < array.length(); index++) {
            String timing = array.getString(index);
            if (!Set.of("start", "end", "damage").contains(timing)) {
                throw new MalformedInputException("Choose a supported saving throw timing.");
            }
            stringSet.add(timing);
        }
        effect.setSaveOn(stringSet);
        Map<String, Effect> activeEffects = Optional.ofNullable(creature.getActiveEffects()).orElse(new HashMap<>());
        activeEffects.put(effect.getObjectId(), effect);
        creature.setActiveEffects(activeEffects);
        creatureMap.put(creature.getEncounterCreatureId(), creature);
        encounter.setCreatureMap(creatureMap);
        return encounter;
    }

    /**
     * Uses provided details to add an effect to a create.
     * @param encounter Old state {@link Encounter} object to be updated.
     * @param body Details to complete task.
     * @return Updated encounter object.
     */
    public Encounter removeEffect(Encounter encounter, JSONObject body) {
        Map<String, Creature> creatureMap = encounter.getCreatureMap();
        Creature creature = requireTarget(creatureMap, body);
        Map<String, Effect> activeEffects = Optional.ofNullable(creature.getActiveEffects()).orElse(new HashMap<>());
        activeEffects.remove(body.getString("effectId"));
        creature.setActiveEffects(activeEffects);
        creatureMap.put(creature.getEncounterCreatureId(), creature);
        encounter.setCreatureMap(creatureMap);
        return encounter;
    }
    private static Creature requireTarget(Map<String, Creature> creatures, JSONObject body) {
        String id = body.getString("targetId");
        Creature target = creatures == null ? null : creatures.get(id);
        if (target == null) {
            throw new MalformedInputException("Choose a target in this encounter.");
        }
        return target;
    }

    private static int nonnegativeInteger(JSONObject body, String key, String label) {
        try {
            int value = Integer.parseInt(body.getString(key));
            if (value >= 0) {
                return value;
            }
        } catch (NumberFormatException exception) {
            throw new MalformedInputException(label + " must be a nonnegative whole number.", exception);
        }
        throw new MalformedInputException(label + " must be a nonnegative whole number.");
    }
}
