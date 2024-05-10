package dev.tdwalsh.project.tabletopBeholder.activity.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetAllSpellsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetAllSpellsResult;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.SpellNotFoundException;

import javax.inject.Inject;
import java.util.List;

/**
 * GetSpellActivity handles negotiation with {@link SpellDao} to retrieve a list of {@link Spell}.
 */
public class GetAllSpellsActivity {
    private final SpellDao spellDao;

    /**
     * Instantiates a new activity object.
     *
     * @param spellDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetAllSpellsActivity(SpellDao spellDao) {
        this.spellDao = spellDao;
    };

    /**
     * This method handles the incoming request by retrieving a list of {@link Spell} from the database, if any exist.
     * <p>
     * It then returns the retrieved object, or an empty list if none is found.
     *
     * @param getAllSpellsRequest request object containing DAO search parameters
     * @return {@link GetAllSpellsResult} result object containing the retrieved {@link Spell} list
     */

    public GetAllSpellsResult handleRequest(GetAllSpellsRequest getAllSpellsRequest) {
        return GetAllSpellsResult.builder()
                .withSpellList(spellDao.getSpellsByUser(getAllSpellsRequest.getUserEmail()))
                .build();
    }
}
