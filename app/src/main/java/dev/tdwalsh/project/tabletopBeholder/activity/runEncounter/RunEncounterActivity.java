package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter;

import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.activities.RunActivities;
import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.request.RunEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.result.RunEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;

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
        Encounter encounter = Optional.ofNullable(encounterDao.getSingle(
                runEncounterRequest.getUserEmail(), runEncounterRequest.getEncounterId()))
                .orElseThrow(() -> new MissingResourceException(
                        String.format("Could not find encounter with id %s", runEncounterRequest.getEncounterId())));
        if (runEncounterRequest.getActivity().equals("setInitiative")) {
            encounter = this.runActivities.setInitiative(encounter, runEncounterRequest.getBody());
        }
        if (runEncounterRequest.getActivity().equals("nextTurn")) {
            encounter = this.runActivities.nextTurn(encounter);
        }
        if (runEncounterRequest.getActivity().equals("applyDamage")) {
            encounter = this.runActivities.applyDamage(encounter, runEncounterRequest.getBody());
        }
        if (runEncounterRequest.getActivity().equals("heal")) {
            encounter = this.runActivities.heal(encounter, runEncounterRequest.getBody());
        }
        if (runEncounterRequest.getActivity().equals("knockOut")) {
            encounter = this.runActivities.knockOut(encounter, runEncounterRequest.getBody());
        }
        if (runEncounterRequest.getActivity().equals("kill")) {
            encounter = this.runActivities.kill(encounter, runEncounterRequest.getBody());
        }
        encounterDao.writeObject(encounter);
        return RunEncounterResult.builder()
                .withEncounter(encounter)
                .build();
    }
}
