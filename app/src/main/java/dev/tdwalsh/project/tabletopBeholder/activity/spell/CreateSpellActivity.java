package dev.tdwalsh.project.tabletopBeholder.activity.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.CreateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetAllSpellsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.CreateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetAllSpellsResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;

import javax.inject.Inject;
import java.util.UUID;

/**
 * GetSpellActivity handles negotiation with {@link SpellDao} to create a {@link Spell}.
 */
public class CreateSpellActivity {
    private final SpellDao spellDao;

    /**
     * Instantiates a new activity object.
     *
     * @param spellDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public CreateSpellActivity(SpellDao spellDao) {
        this.spellDao = spellDao;
    };

    /**
     * This method handles the incoming request by creating a new {@link Spell} on the database.
     * <p>
     * It then returns the created object.
     *
     * @param createSpellRequest request object containing object attributes
     * @return {@link CreateSpellResult} result object containing the created {@link Spell}
     */

    public CreateSpellResult handleRequest(CreateSpellRequest createSpellRequest) {
        Spell spell = new Spell();
        spell.setUserEmail(createSpellRequest.getUserEmail());
        spell.setSpellName(createSpellRequest.getSpellName());
        spell.setSpellDescription(createSpellRequest.getSpellDescription());
        spell.setSpellHigherLevel(createSpellRequest.getSpellHigherLevel());
        spell.setSpellRange(createSpellRequest.getSpellRange());
        spell.setSpellComponents(createSpellRequest.getSpellComponents());
        spell.setSpellMaterial(createSpellRequest.getSpellMaterial());
        spell.setRitualCast(createSpellRequest.getRitualCast());
        spell.setCastingTime(createSpellRequest.getCastingTime());
        spell.setSpellLevel(createSpellRequest.getSpellLevel());
        spell.setSpellSchool(createSpellRequest.getSpellSchool());
        spell.setAppliesEffects(createSpellRequest.getAppliesEffects());

        return CreateSpellResult.builder()
                .withSpell(createSpell(spell))
                .build();
    }

    public Spell createSpell(Spell spell) {
        spellNameUniqueness(spell.getUserEmail(), spell.getSpellName());
        spell.setSpellId(UUID.randomUUID().toString());
        while (spellDao.getSingleSpell(spell.getUserEmail(), spell.getSpellId()) != null) {
            spell.setSpellId(UUID.randomUUID().toString());
        }
        spellDao.writeSpell(spell);
        return spell;
    }

    private void spellNameUniqueness(String userName, String spellName) {
        if (spellDao.spellNameExists(userName, spellName)) {
            throw new DuplicateResourceException(String.format("Resource with name [%s] already exists", spellName));
        }
    }
}
