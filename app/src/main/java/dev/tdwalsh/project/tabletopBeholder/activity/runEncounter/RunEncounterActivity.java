package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter;

import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.activities.RunActivities;
import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.request.RunEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.result.RunEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;

import javax.inject.Inject;
import java.util.Optional;

/**
 * RunEncounterActivity performs a wide range of activities related to running and encounter.
 */
public class RunEncounterActivity {
    private final EncounterDao encounterDao;
    private final RunActivities runActivities = new RunActivities();

    /**
     * Instantiates a new activity object.
     *
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
        if(runEncounterRequest.getActivity().equals("setInitiative")) {
            encounter = this.runActivities.setInitiative(encounter, runEncounterRequest.getBody());
        }
        encounterDao.writeObject(encounter);
        return RunEncounterResult.builder()
                .withEncounter(encounter)
                .build();
    }
}
