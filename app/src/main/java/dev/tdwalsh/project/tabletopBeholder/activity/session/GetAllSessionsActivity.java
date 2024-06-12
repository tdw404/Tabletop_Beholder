package dev.tdwalsh.project.tabletopBeholder.activity.session;

import dev.tdwalsh.project.tabletopBeholder.activity.session.request.GetAllSessionsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.GetAllSessionsResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SessionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;

import java.util.List;
import javax.inject.Inject;

/**
 * GetSessionActivity handles negotiation with {@link SessionDao} to retrieve a list of {@link Session}.
 */
public class GetAllSessionsActivity {
    private final SessionDao sessionDao;

    /**
     * Instantiates a new activity object.
     *
     * @param sessionDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetAllSessionsActivity(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    };

    /**
     * This method handles the incoming request by retrieving a list of {@link Session} from the database, if any exist.
     * <p>
     * It then returns the retrieved object, or an empty list if none is found.
     *
     * @param getAllSessionsRequest request object containing DAO search parameters
     * @return {@link GetAllSessionsResult} result object containing the retrieved {@link Session} list
     */

    public GetAllSessionsResult handleRequest(GetAllSessionsRequest getAllSessionsRequest) {
        return GetAllSessionsResult.builder()
                .withSessionList((List<Session>) sessionDao.getMultiple(getAllSessionsRequest.getUserEmail()))
                .build();
    }
}
