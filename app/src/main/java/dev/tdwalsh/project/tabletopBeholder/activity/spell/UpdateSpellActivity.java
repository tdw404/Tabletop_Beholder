package dev.tdwalsh.project.tabletopBeholder.activity.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.CreateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.UpdateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.CreateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.UpdateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;

import javax.inject.Inject;
import java.util.UUID;

/**
 * GetSpellActivity handles negotiation with {@link SpellDao} to create a {@link Spell}.
 */
public class UpdateSpellActivity {
    private final SpellDao spellDao;

    /**
     * Instantiates a new activity object.
     *
     * @param spellDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public UpdateSpellActivity(SpellDao spellDao) {
        this.spellDao = spellDao;
    };

    /**
     * This method handles the incoming request by updating a {@link Spell} on the database if it exists.
     * <p>
     * It then returns the updated object.
     *
     * @param updateSpellRequest request object containing object attributes
     * @return {@link UpdateSpellResult} result object containing the created {@link Spell}
     */

    public UpdateSpellResult handleRequest(UpdateSpellRequest updateSpellRequest) {
        Spell spell = new Spell();
        spell.setUserEmail(updateSpellRequest.getUserEmail());
        spell.setSpellId(updateSpellRequest.getSpellId());
        spell.setSpellName(updateSpellRequest.getSpellName());
        spell.setSpellDescription(updateSpellRequest.getSpellDescription());
        spell.setSpellHigherLevel(updateSpellRequest.getSpellHigherLevel());
        spell.setSpellRange(updateSpellRequest.getSpellRange());
        spell.setSpellComponents(updateSpellRequest.getSpellComponents());
        spell.setSpellMaterial(updateSpellRequest.getSpellMaterial());
        spell.setRitualCast(updateSpellRequest.getRitualCast());
        spell.setCastingTime(updateSpellRequest.getCastingTime());
        spell.setSpellLevel(updateSpellRequest.getSpellLevel());
        spell.setSpellSchool(updateSpellRequest.getSpellSchool());
        spell.setAppliesEffects(updateSpellRequest.getAppliesEffects());

        spellExists(spell.getUserEmail(), spell.getSpellId());
        spellDao.writeSpell(spell);
        return UpdateSpellResult.builder()
                .withSpell(spell)
                .build();
    }

    private void spellExists(String userName, String spellId) {
        if (spellDao.getSingleSpell(userName, spellId) == null) {
            throw new MissingResourceException(String.format("Resource with id [%s] could not be retrieved from database", spellId));
        }
    }
}
