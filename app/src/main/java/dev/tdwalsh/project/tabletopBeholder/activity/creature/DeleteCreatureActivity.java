package dev.tdwalsh.project.tabletopBeholder.activity.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.DeleteCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.DeleteCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;

import javax.inject.Inject;

/**
 * GetCreatureActivity handles negotiation with {@link CreatureDao} to delete a single {@link Creature}.
 */
public class DeleteCreatureActivity {
    private final CreatureDao creatureDao;

    /**
     * Instantiates a new activity object.
     *
     * @param creatureDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public DeleteCreatureActivity(CreatureDao creatureDao) {
        this.creatureDao = creatureDao;
    };

    /**
     * This method handles the incoming request by deleting a {@link Creature} from the database, if it exists.
     * <p>
     * It then returns an empty response.
     *
     * @param deleteCreatureRequest request object containing DAO search parameters
     * @return {@link DeleteCreatureResult} result object containing the retrieved {@link Creature}
     */

    public DeleteCreatureResult handleRequest(DeleteCreatureRequest deleteCreatureRequest) {
        creatureDao.deleteObject(deleteCreatureRequest.getUserEmail(), deleteCreatureRequest.getObjectId());
        return null;
    }
}
