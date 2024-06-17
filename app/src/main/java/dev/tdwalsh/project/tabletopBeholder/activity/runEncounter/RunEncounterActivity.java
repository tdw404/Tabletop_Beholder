package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter;

import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.request.RunEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.result.RunEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Optional;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

/**
 * RunEncounterActivity performs a wide range of activities related to running and encounter.
 */
public class RunEncounterActivity {
    private final EncounterDao encounterDao;

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

        return RunEncounterResult.builder()
                .withEncounter(encounter)
                .build();
    }
}
