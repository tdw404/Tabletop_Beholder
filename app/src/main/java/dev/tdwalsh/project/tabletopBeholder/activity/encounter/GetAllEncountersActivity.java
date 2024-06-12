package dev.tdwalsh.project.tabletopBeholder.activity.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.GetAllEncountersRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.GetAllEncountersResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

import java.util.List;
import javax.inject.Inject;

/**
 * GetEncounterActivity handles negotiation with {@link EncounterDao} to retrieve a list of {@link Encounter}.
 */
public class GetAllEncountersActivity {
    private final EncounterDao encounterDao;

    /**
     * Instantiates a new activity object.
     *
     * @param encounterDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetAllEncountersActivity(EncounterDao encounterDao) {
        this.encounterDao = encounterDao;
    };

    /**
     * This method handles the incoming request by retrieving a list of {@link Encounter} from the database, if any exist.
     * <p>
     * It then returns the retrieved object, or an empty list if none is found.
     *
     * @param getAllEncountersRequest request object containing DAO search parameters
     * @return {@link GetAllEncountersResult} result object containing the retrieved {@link Encounter} list
     */

    public GetAllEncountersResult handleRequest(GetAllEncountersRequest getAllEncountersRequest) {
        return GetAllEncountersResult.builder()
                .withEncounterList((List<Encounter>) encounterDao.getMultiple(getAllEncountersRequest.getUserEmail()))
                .build();
    }
}
