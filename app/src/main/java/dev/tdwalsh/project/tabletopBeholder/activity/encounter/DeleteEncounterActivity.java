package dev.tdwalsh.project.tabletopBeholder.activity.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.DeleteEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.DeleteEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

import javax.inject.Inject;

/**
 * GetEncounterActivity handles negotiation with {@link EncounterDao} to delete a single {@link Encounter}.
 */
public class DeleteEncounterActivity {
    private final EncounterDao encounterDao;

    /**
     * Instantiates a new activity object.
     *
     * @param encounterDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public DeleteEncounterActivity(EncounterDao encounterDao) {
        this.encounterDao = encounterDao;
    };

    /**
     * This method handles the incoming request by deleting a {@link Encounter} from the database, if it exists.
     * <p>
     * It then returns an empty response.
     *
     * @param deleteEncounterRequest request object containing DAO search parameters
     * @return {@link DeleteEncounterResult} result object containing the retrieved {@link Encounter}
     */

    public DeleteEncounterResult handleRequest(DeleteEncounterRequest deleteEncounterRequest) {
        encounterDao.deleteObject(deleteEncounterRequest.getUserEmail(), deleteEncounterRequest.getObjectId());
        return null;
    }
}
