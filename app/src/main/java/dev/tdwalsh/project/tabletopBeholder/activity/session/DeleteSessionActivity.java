package dev.tdwalsh.project.tabletopBeholder.activity.session;

import dev.tdwalsh.project.tabletopBeholder.activity.session.request.DeleteSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.DeleteSessionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SessionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;

import javax.inject.Inject;

/**
 * GetSessionActivity handles negotiation with {@link SessionDao} to delete a single {@link Session}.
 */
public class DeleteSessionActivity {
    private final SessionDao sessionDao;

    /**
     * Instantiates a new activity object.
     *
     * @param sessionDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public DeleteSessionActivity(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
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
        sessionDao.deleteObject(deleteSessionRequest.getUserEmail(), deleteSessionRequest.getObjectId());
        return null;
    }
}
