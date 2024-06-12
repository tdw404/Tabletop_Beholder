package dev.tdwalsh.project.tabletopBeholder.activity.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.GetCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.GetCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.CreatureNotFoundException;

import javax.inject.Inject;

/**
 * GetCreatureActivity handles negotiation with {@link CreatureDao} to retrieve a single {@link Creature}.
 */
public class GetCreatureActivity {
    private final CreatureDao creatureDao;

    /**
     * Instantiates a new activity object.
     *
     * @param creatureDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetCreatureActivity(CreatureDao creatureDao) {
        this.creatureDao = creatureDao;
    };

    /**
     * This method handles the incoming request by retrieving a {@link Creature} from the database, if it exists.
     * <p>
     * It then returns the retrieved object, or throws a {@link CreatureNotFoundException} if none is found.
     *
     * @param getCreatureRequest request object containing DAO search parameters
     * @return {@link GetCreatureResult} result object containing the retrieved {@link Creature}
     */

    public GetCreatureResult handleRequest(GetCreatureRequest getCreatureRequest) {
        Creature creature = creatureDao.getSingle(getCreatureRequest.getUserEmail(), getCreatureRequest.getObjectId());
        if (creature == null) {
            throw new CreatureNotFoundException(String.format("Could not find a creature for [%s] with id [%s]",
                getCreatureRequest.getUserEmail(), getCreatureRequest.getObjectId()));
        }
        return GetCreatureResult.builder()
                .withCreature(creature)
                .build();
    }
}
