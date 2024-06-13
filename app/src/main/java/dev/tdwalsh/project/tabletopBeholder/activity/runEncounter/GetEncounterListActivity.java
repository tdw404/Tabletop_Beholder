package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter;

import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.request.GetEncounterListRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.result.GetEncounterListResult;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.EncounterName;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;

/**
 * GetSpellActivity handles negotiation with {@link EncounterDao} to retrieve a list of {@link EncounterName} objects
 * belonging to provided session id.
 */
public class GetEncounterListActivity {
    private final EncounterDao encounterDao;

    /**
     * Instantiates a new activity object.
     *
     * @param encounterDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetEncounterListActivity(EncounterDao encounterDao) {
        this.encounterDao = encounterDao;
    };

    /**
     * This method handles the incoming request by retrieving a list of
     * {@link dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter} objects from the database, if they exist.
     * <p>
     * Any returned objects are converted to {@link EncounterName} and returned as a list.
     *
     * @param getEncounterListRequest request object containing DAO search parameters
     * @return {@link GetSpellResult} result object containing the retrieved {@link Spell}
     */

    public GetEncounterListResult handleRequest(GetEncounterListRequest getEncounterListRequest) {
        return GetEncounterListResult.builder()
                .withEncounterNameList(Optional.ofNullable(encounterDao.getEncounterBySession(
                        getEncounterListRequest.getUserEmail(), getEncounterListRequest.getSessionId()))
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(encounter -> new EncounterName(encounter.getUserEmail(),
                                encounter.getObjectId(),
                                encounter.getObjectName()))
                        .collect(Collectors.toList()))
                .build();
    }
}
