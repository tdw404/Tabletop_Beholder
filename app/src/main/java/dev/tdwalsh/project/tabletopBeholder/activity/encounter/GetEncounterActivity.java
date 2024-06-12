package dev.tdwalsh.project.tabletopBeholder.activity.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.GetEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.GetEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.EncounterNotFoundException;

import javax.inject.Inject;

/**
 * GetEncounterActivity handles negotiation with {@link EncounterDao} to retrieve a single {@link Encounter}.
 */
public class GetEncounterActivity {
    private final EncounterDao encounterDao;

    /**
     * Instantiates a new activity object.
     *
     * @param encounterDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetEncounterActivity(EncounterDao encounterDao) {
        this.encounterDao = encounterDao;
    };

    /**
     * This method handles the incoming request by retrieving a {@link Encounter} from the database, if it exists.
     * <p>
     * It then returns the retrieved object, or throws a {@link EncounterNotFoundException} if none is found.
     *
     * @param getEncounterRequest request object containing DAO search parameters
     * @return {@link GetEncounterResult} result object containing the retrieved {@link Encounter}
     */

    public GetEncounterResult handleRequest(GetEncounterRequest getEncounterRequest) {
        Encounter encounter = encounterDao.getSingle(getEncounterRequest.getUserEmail(), getEncounterRequest.getObjectId());
        if (encounter == null) {
            throw new EncounterNotFoundException(String.format("Could not find a encounter for [%s] with id [%s]",
                    getEncounterRequest.getUserEmail(), getEncounterRequest.getObjectId()));
        }
        return GetEncounterResult.builder()
                .withEncounter(encounter)
                .build();
    }
}
