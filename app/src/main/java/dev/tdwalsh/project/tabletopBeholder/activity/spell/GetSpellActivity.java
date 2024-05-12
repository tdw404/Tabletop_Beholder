package dev.tdwalsh.project.tabletopBeholder.activity.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.SpellNotFoundException;

import javax.inject.Inject;

/**
 * GetSpellActivity handles negotiation with {@link SpellDao} to retrieve a single {@link Spell}.
 */
public class GetSpellActivity {
    private final SpellDao spellDao;

    /**
     * Instantiates a new activity object.
     *
     * @param spellDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetSpellActivity(SpellDao spellDao) {
        this.spellDao = spellDao;
    };

    /**
     * This method handles the incoming request by retrieving a {@link Spell} from the database, if it exists.
     * <p>
     * It then returns the retrieved object, or throws a {@link SpellNotFoundException} if none is found.
     *
     * @param getSpellRequest request object containing DAO search parameters
     * @return {@link GetSpellResult} result object containing the retrieved {@link Spell}
     */

    public GetSpellResult handleRequest(GetSpellRequest getSpellRequest) {
        Spell spell = spellDao.getSingle(getSpellRequest.getUserEmail(), getSpellRequest.getSpellId());
        if (spell == null) {
            throw new SpellNotFoundException();
        }
        return GetSpellResult.builder()
                .withSpell(spell)
                .build();
    }
}
