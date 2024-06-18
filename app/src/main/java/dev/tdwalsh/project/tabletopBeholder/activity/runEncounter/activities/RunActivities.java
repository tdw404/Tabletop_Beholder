package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.activities;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;
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
}
