package dev.tdwalsh.project.tabletopBeholder.activity.session;

import dev.tdwalsh.project.tabletopBeholder.activity.session.request.DeleteSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.DeleteSessionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SessionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

/**
 * GetSessionActivity handles negotiation with {@link SessionDao} to delete a single {@link Session}.
 */
public class DeleteSessionActivity {
    private final SessionDao sessionDao;
    private final EncounterDao encounterDao;

    /**
     * Instantiates a new activity object.
     *
     * @param sessionDao DAO object necessary for this activity object to carry out its function.
     * @param encounterDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public DeleteSessionActivity(SessionDao sessionDao, EncounterDao encounterDao) {
        this.sessionDao = sessionDao;
        this.encounterDao = encounterDao;
    };

    /**
     * This method handles the incoming request by deleting a {@link Session} from the database, if it exists.
     * <p>
     * It then returns an empty response.
     *
     * @param deleteSessionRequest request object containing DAO search parameters
     * @return {@link DeleteSessionResult} result object containing the retrieved {@link Session}
     */

    public DeleteSessionResult handleRequest(DeleteSessionRequest deleteSessionRequest) {
        //First, deletes all encounters associated with session
        //Then, deletes the session itself
        Optional.ofNullable(Optional.ofNullable(sessionDao.getSingle(deleteSessionRequest.getUserEmail(), deleteSessionRequest.getObjectId())).orElse(new Session())
                        .getEncounterList()).orElse(Collections.emptyList())
                        .forEach(encounter -> encounterDao.deleteObject(encounter.getUserEmail(), encounter.getObjectId()));
        sessionDao.deleteObject(deleteSessionRequest.getUserEmail(), deleteSessionRequest.getObjectId());
        return null;
    }
}
