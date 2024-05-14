package dev.tdwalsh.project.tabletopBeholder.activity.action;

import dev.tdwalsh.project.tabletopBeholder.activity.helpers.NameHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.action.request.UpdateActionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.UpdateActionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.ActionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * GetActionActivity handles negotiation with {@link ActionDao} to create a {@link Action}.
 */
public class UpdateActionActivity {
    private final ActionDao actionDao;

    /**
     * Instantiates a new activity object.
     *
     * @param actionDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public UpdateActionActivity(ActionDao actionDao) {
        this.actionDao = actionDao;
    };

    /**
     * This method handles the incoming request by updating a {@link Action} on the database if it exists.
     * <p>
     * It then returns the updated object.
     *
     * @param updateActionRequest request object containing object attributes
     * @return {@link UpdateActionResult} result object containing the created {@link Action}
     */

    public UpdateActionResult handleRequest(UpdateActionRequest updateActionRequest) {
        //First, assigns the updated object to a new variable
        //Then, pulls userEmail from the authenticated email field in the request
        //Then, retrieves the previous version from the DB, or throws an error if it does not exist
        //Then, if the name has changed, checks for name uniqueness, and throws an error if violated
        //Then, writes the new version to the DB
        //Finally, returns the updated version
        Action newAction  = updateActionRequest.getAction();
        newAction.setUserEmail(updateActionRequest.getUserEmail());
        Action oldAction = Optional.ofNullable(actionDao.getSingle(newAction.getUserEmail(), newAction.getObjectId())).orElseThrow(() -> new MissingResourceException(String.format("Resource with id [%s] could not be retrieved from database", newAction.getObjectId())));
        if (!newAction.getObjectName().equals(oldAction.getObjectName())) {
            NameHelper.objectNameUniqueness(actionDao, newAction);
        }
        newAction.setCreateDateTime(oldAction.getCreateDateTime());
        newAction.setEditDateTime(ZonedDateTime.now());
        actionDao.writeObject(newAction);
        return UpdateActionResult.builder()
                .withAction(newAction)
                .build();
    }
}
