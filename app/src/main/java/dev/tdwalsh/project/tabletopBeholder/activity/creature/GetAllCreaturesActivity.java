package dev.tdwalsh.project.tabletopBeholder.activity.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.GetAllCreaturesRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.GetAllCreaturesResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;

import java.util.List;
import javax.inject.Inject;

/**
 * GetCreatureActivity handles negotiation with {@link CreatureDao} to retrieve a list of {@link Creature}.
 */
public class GetAllCreaturesActivity {
    private final CreatureDao creatureDao;

    /**
     * Instantiates a new activity object.
     *
     * @param creatureDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetAllCreaturesActivity(CreatureDao creatureDao) {
        this.creatureDao = creatureDao;
    };

    /**
     * This method handles the incoming request by retrieving a list of {@link Creature} from the database, if any exist.
     * <p>
     * It then returns the retrieved object, or an empty list if none is found.
     *
     * @param getAllCreaturesRequest request object containing DAO search parameters
     * @return {@link GetAllCreaturesResult} result object containing the retrieved {@link Creature} list
     */

    public GetAllCreaturesResult handleRequest(GetAllCreaturesRequest getAllCreaturesRequest) {
        return GetAllCreaturesResult.builder()
                .withCreatureList((List<Creature>) creatureDao.getMultiple(getAllCreaturesRequest.getUserEmail()))
                .build();
    }
}
