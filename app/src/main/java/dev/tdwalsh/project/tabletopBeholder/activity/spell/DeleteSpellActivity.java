package dev.tdwalsh.project.tabletopBeholder.activity.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.DeleteSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.DeleteSpellResult;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.SpellNotFoundException;

import javax.inject.Inject;

/**
 * GetSpellActivity handles negotiation with {@link SpellDao} to delete a single {@link Spell}.
 */
public class DeleteSpellActivity {
    private final SpellDao spellDao;

    /**
     * Instantiates a new activity object.
     *
     * @param spellDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public DeleteSpellActivity(SpellDao spellDao) {
        this.spellDao = spellDao;
    };

    /**
     * This method handles the incoming request by deleting a {@link Spell} from the database, if it exists.
     * <p>
     * It then returns an empty response.
     *
     * @param deleteSpellRequest request object containing DAO search parameters
     * @return {@link DeleteSpellResult} result object containing the retrieved {@link Spell}
     */

    public DeleteSpellResult handleRequest(DeleteSpellRequest deleteSpellRequest) {
        spellDao.deleteObject(deleteSpellRequest.getUserEmail(), deleteSpellRequest.getSpellId());
        return null;
    }
}
