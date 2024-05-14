package dev.tdwalsh.project.tabletopBeholder.activity.action;

import dev.tdwalsh.project.tabletopBeholder.activity.action.request.GetActionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.GetActionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.ActionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.ActionNotFoundException;

import javax.inject.Inject;

/**
 * GetActionActivity handles negotiation with {@link ActionDao} to retrieve a single {@link Action}.
 */
public class GetActionActivity {
    private final ActionDao actionDao;

    /**
     * Instantiates a new activity object.
     *
     * @param actionDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetActionActivity(ActionDao actionDao) {
        this.actionDao = actionDao;
    };

    /**
     * This method handles the incoming request by retrieving a {@link Action} from the database, if it exists.
     * <p>
     * It then returns the retrieved object, or throws a {@link ActionNotFoundException} if none is found.
     *
     * @param getActionRequest request object containing DAO search parameters
     * @return {@link GetActionResult} result object containing the retrieved {@link Action}
     */

    public GetActionResult handleRequest(GetActionRequest getActionRequest) {
        Action action = actionDao.getSingle(getActionRequest.getUserEmail(), getActionRequest.getObjectId());
        if (action == null) {
            throw new ActionNotFoundException(String.format("Could not find a action for [%s] with id [%s]", getActionRequest.getUserEmail(), getActionRequest.getObjectId()));
        }
        return GetActionResult.builder()
                .withAction(action)
                .build();
    }
}
