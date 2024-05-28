package dev.tdwalsh.project.tabletopBeholder.activity.action;

import dev.tdwalsh.project.tabletopBeholder.activity.action.request.GetAllActionsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.GetAllActionsResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.ActionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;

import java.util.List;
import javax.inject.Inject;


/**
 * GetActionActivity handles negotiation with {@link ActionDao} to retrieve a list of {@link Action}.
 */
public class GetAllActionsActivity {
    private final ActionDao actionDao;

    /**
     * Instantiates a new activity object.
     *
     * @param actionDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetAllActionsActivity(ActionDao actionDao) {
        this.actionDao = actionDao;
    };

    /**
     * This method handles the incoming request by retrieving a list of {@link Action} from the database, if any exist.
     * <p>
     * It then returns the retrieved object, or an empty list if none is found.
     *
     * @param getAllActionsRequest request object containing DAO search parameters
     * @return {@link GetAllActionsResult} result object containing the retrieved {@link Action} list
     */

    public GetAllActionsResult handleRequest(GetAllActionsRequest getAllActionsRequest) {
        return GetAllActionsResult.builder()
                .withActionList((List<Action>) actionDao.getMultiple(getAllActionsRequest.getUserEmail()))
                .build();
    }
}
