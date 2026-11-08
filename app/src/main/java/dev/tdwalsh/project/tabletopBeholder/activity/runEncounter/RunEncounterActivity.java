package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter;

import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.activities.RunActivities;
import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.request.RunEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.result.RunEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MalformedInputException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;
import javax.inject.Inject;

/**
 * RunEncounterActivity performs a wide range of activities related to running and encounter.
 */
public class RunEncounterActivity {
    private final EncounterDao encounterDao;
    private final RunActivities runActivities = new RunActivities();

    /**
     * Instantiates a new activity object.
     * @param encounterDao Dao needed for class to perform activities.
     *
     */

    @Inject
    public RunEncounterActivity(EncounterDao encounterDao) {
        this.encounterDao = encounterDao;
    };

    /**
     * This method handles the incoming request by running an encounter activity.
     * <p>
     * It then returns an {@link Encounter} with updated state.
     *
     * @param runEncounterRequest request object containing activity parameters
     * @return {@link RunEncounterResult} result object containing the updated {@link Encounter}
     */

    public RunEncounterResult handleRequest(RunEncounterRequest runEncounterRequest) {
        String action = runEncounterRequest.getActivity();
        if (action == null || action.isBlank()) {
            throw new MalformedInputException("Choose a supported encounter action.");
        }
        Encounter encounter = Optional.ofNullable(encounterDao.getSingle(
                runEncounterRequest.getUserEmail(), runEncounterRequest.getEncounterId()))
                .orElseThrow(() -> new MissingResourceException("Encounter not found."));
        try {
            encounter = applyAction(action, encounter, runEncounterRequest.getBody());
        } catch (JSONException exception) {
            throw new MalformedInputException("The encounter action has missing or invalid fields.", exception);
        }
        encounterDao.writeObject(encounter);
        return RunEncounterResult.builder()
                .withEncounter(encounter)
                .build();
    }
    private Encounter applyAction(String action, Encounter encounter, JSONObject body) {
        switch (action) {
            case "setInitiative":
                return runActivities.setInitiative(encounter, body);
            case "nextTurn":
                return runActivities.nextTurn(encounter);
            case "applyDamage":
                return runActivities.applyDamage(encounter, body);
            case "heal":
                return runActivities.heal(encounter, body);
            case "knockOut":
                return runActivities.knockOut(encounter, body);
            case "kill":
                return runActivities.kill(encounter, body);
            case "addEffect":
                return runActivities.addEffect(encounter, body);
            case "removeEffect":
                return runActivities.removeEffect(encounter, body);
            default:
                throw new MalformedInputException("Choose a supported encounter action.");
        }
    }
}
