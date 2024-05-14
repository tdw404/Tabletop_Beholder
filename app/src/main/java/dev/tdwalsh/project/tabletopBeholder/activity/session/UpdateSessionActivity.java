package dev.tdwalsh.project.tabletopBeholder.activity.session;

import dev.tdwalsh.project.tabletopBeholder.activity.helpers.NameHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.session.request.UpdateSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.UpdateSessionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SessionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * GetSessionActivity handles negotiation with {@link SessionDao} to create a {@link Session}.
 */
public class UpdateSessionActivity {
    private final SessionDao sessionDao;

    /**
     * Instantiates a new activity object.
     *
     * @param sessionDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public UpdateSessionActivity(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    };

    /**
     * This method handles the incoming request by updating a {@link Session} on the database if it exists.
     * <p>
     * It then returns the updated object.
     *
     * @param updateSessionRequest request object containing object attributes
     * @return {@link UpdateSessionResult} result object containing the created {@link Session}
     */

    public UpdateSessionResult handleRequest(UpdateSessionRequest updateSessionRequest) {
        //First, assigns the updated object to a new variable
        //Then, pulls userEmail from the authenticated email field in the request
        //Then, retrieves the previous version from the DB, or throws an error if it does not exist
        //Then, if the name has changed, checks for name uniqueness, and throws an error if violated
        //Then, writes the new version to the DB
        //Finally, returns the updated version
        Session newSession  = updateSessionRequest.getSession();
        newSession.setUserEmail(updateSessionRequest.getUserEmail());
        Session oldSession = Optional.ofNullable(sessionDao.getSingle(newSession.getUserEmail(), newSession.getObjectId())).orElseThrow(() -> new MissingResourceException(String.format("Resource with id [%s] could not be retrieved from database", newSession.getObjectId())));
        if (!newSession.getObjectName().equals(oldSession.getObjectName())) {
            NameHelper.objectNameUniqueness(sessionDao, newSession);
        }
        newSession.setCreateDateTime(oldSession.getCreateDateTime());
        newSession.setEditDateTime(ZonedDateTime.now());
        sessionDao.writeObject(newSession);
        return UpdateSessionResult.builder()
                .withSession(newSession)
                .build();
    }
}
