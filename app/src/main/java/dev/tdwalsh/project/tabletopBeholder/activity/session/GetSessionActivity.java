package dev.tdwalsh.project.tabletopBeholder.activity.session;

import dev.tdwalsh.project.tabletopBeholder.activity.session.request.GetSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.GetSessionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SessionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.SessionNotFoundException;

import javax.inject.Inject;

/**
 * GetSessionActivity handles negotiation with {@link SessionDao} to retrieve a single {@link Session}.
 */
public class GetSessionActivity {
    private final SessionDao sessionDao;

    /**
     * Instantiates a new activity object.
     *
     * @param sessionDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetSessionActivity(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    };

    /**
     * This method handles the incoming request by retrieving a {@link Session} from the database, if it exists.
     * <p>
     * It then returns the retrieved object, or throws a {@link SessionNotFoundException} if none is found.
     *
     * @param getSessionRequest request object containing DAO search parameters
     * @return {@link GetSessionResult} result object containing the retrieved {@link Session}
     */

    public GetSessionResult handleRequest(GetSessionRequest getSessionRequest) {
        Session session = sessionDao.getSingle(getSessionRequest.getUserEmail(), getSessionRequest.getObjectId());
        if (session == null) {
            throw new SessionNotFoundException(String.format("Could not find a session for [%s] with id [%s]", getSessionRequest.getUserEmail(), getSessionRequest.getObjectId()));
        }
        return GetSessionResult.builder()
                .withSession(session)
                .build();
    }
}
