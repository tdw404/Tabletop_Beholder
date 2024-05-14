package dev.tdwalsh.project.tabletopBeholder.activity.action;

import dev.tdwalsh.project.tabletopBeholder.activity.action.request.DeleteActionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.DeleteActionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.ActionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;

import javax.inject.Inject;

/**
 * GetActionActivity handles negotiation with {@link ActionDao} to delete a single {@link Action}.
 */
public class DeleteActionActivity {
    private final ActionDao actionDao;

    /**
     * Instantiates a new activity object.
     *
     * @param actionDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public DeleteActionActivity(ActionDao actionDao) {
        this.actionDao = actionDao;
    };

    /**
     * This method handles the incoming request by deleting a {@link Action} from the database, if it exists.
     * <p>
     * It then returns an empty response.
     *
     * @param deleteActionRequest request object containing DAO search parameters
     * @return {@link DeleteActionResult} result object containing the retrieved {@link Action}
     */

    public DeleteActionResult handleRequest(DeleteActionRequest deleteActionRequest) {
        actionDao.deleteObject(deleteActionRequest.getUserEmail(), deleteActionRequest.getObjectId());
        return null;
    }
}
