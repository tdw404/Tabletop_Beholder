package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.activities;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RunActivitiesTest {
    private final RunActivities activities = new RunActivities();

    private Encounter encounter(List<String> order) {
        Encounter encounter = new Encounter();
        HashMap<String, Creature> creatures = new HashMap<>();
        for (String id : order) {
            Creature creature = new Creature();
            creature.setEncounterCreatureId(id);
            creature.setEncounterCreatureName(id);
            creature.setActiveEffects(new HashMap<>());
            creatures.put(id, creature);
        }
        encounter.setCreatureMap(creatures);
        return encounter;
    }

    @Test
    void initiativePreservesSuppliedOrderRatherThanSortingCreatureIds() {
        List<String> order = List.of("rogue-z", "goblin-a", "wizard-m");
        Encounter encounter = encounter(order);

        activities.setInitiative(encounter, new JSONObject().put("queueList", order));

        assertEquals(order, new ArrayList<>(encounter.getTurnQueue()));
        assertEquals("rogue-z", encounter.getTopOfOrder());
        assertEquals(1, encounter.getEncounterRound());
    }

    @Test
    void nextTurnCyclesInInitiativeOrderAndAdvancesRoundOnlyAfterFullCycle() {
        List<String> order = List.of("rogue-z", "goblin-a", "wizard-m");
        Encounter encounter = encounter(order);
        activities.setInitiative(encounter, new JSONObject().put("queueList", order));

        for (int turn = 1; turn <= 6; turn++) {
            activities.nextTurn(encounter);
            assertEquals(order.get(turn % 3), encounter.getTurnQueue().peek());
            assertEquals(1 + turn / 3, encounter.getEncounterRound());
            assertEquals("rogue-z", encounter.getTopOfOrder());
            if (turn % 3 == 0) {
                assertTrue(encounter.getMessageQueue().contains(
                        "This is the start of round " + (1 + turn / 3) + "!"));
                assertEquals(order, new ArrayList<>(encounter.getTurnQueue()));
            } else {
                assertTrue(encounter.getMessageQueue().isEmpty());
            }
        }
    }

    @Test
    void singleCreatureStartsANewRoundEachTurn() {
        Encounter encounter = encounter(List.of("solo"));
        activities.setInitiative(encounter, new JSONObject().put("queueList", List.of("solo")));

        activities.nextTurn(encounter);

        assertEquals("solo", encounter.getTurnQueue().peek());
        assertEquals(2, encounter.getEncounterRound());
    }
}
