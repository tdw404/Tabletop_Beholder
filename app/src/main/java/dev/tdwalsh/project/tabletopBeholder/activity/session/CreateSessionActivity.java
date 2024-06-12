package dev.tdwalsh.project.tabletopBeholder.activity.session;

import dev.tdwalsh.project.tabletopBeholder.activity.helpers.CreateObjectHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.helpers.NameHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.session.request.CreateSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.CreateSessionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SessionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;

import javax.inject.Inject;

/**
 * GetSessionActivity handles negotiation with {@link SessionDao} to create a {@link Session}.
 */
public class CreateSessionActivity {
    private final SessionDao sessionDao;

    /**
     * Instantiates a new activity object.
     *
     * @param sessionDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public CreateSessionActivity(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    };

    /**
     * This method handles the incoming request by creating a new {@link Session} on the database.
     * <p>
     * It then returns the created object.
     *
     * @param createSessionRequest request object containing object attributes
     * @return {@link CreateSessionResult} result object containing the created {@link Session}
     */

    public CreateSessionResult handleRequest(CreateSessionRequest createSessionRequest) {
        //First, assigned contained object to new variable
        //Then, pulls userEmail from the authenticated email field in the request
        //Then, checks name for uniqueness
        //Then, uses the create helper to finish building the object
        //Finally, returns the newly created object
        Session session = createSessionRequest.getSession();
        session.setUserEmail(createSessionRequest.getUserEmail());
        NameHelper.objectNameUniqueness(sessionDao, session);

        return CreateSessionResult.builder()
                .withSession((Session) CreateObjectHelper.createObject(sessionDao, session))
                .build();
    }
}
