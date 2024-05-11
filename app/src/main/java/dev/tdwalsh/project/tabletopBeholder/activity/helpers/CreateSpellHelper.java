package dev.tdwalsh.project.tabletopBeholder.activity.helpers;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.CreateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;

import java.util.UUID;

/**
 * This is a helper class to allow multiple activities to create a {@link Spell} using the same method.
 */
public class CreateSpellHelper {

    /**
     * This method takes in a {@link Spell} object, gives it a unique ID, then writes it to the database.
     * <p>
     * It then returns the created object.
     *
     * @param spellDao the DAO to use in the static method
     * @param spell the {@link Spell} object to process and write
     * @return {@link Spell} the updated created object
     */
    public static Spell createSpell(SpellDao spellDao, Spell spell) {
        spellNameUniqueness(spellDao, spell.getUserEmail(), spell.getSpellName());
        spell.setSpellId(UUID.randomUUID().toString());
        while (spellDao.getSingleSpell(spell.getUserEmail(), spell.getSpellId()) != null) {
            spell.setSpellId(UUID.randomUUID().toString());
        }
        spellDao.writeSpell(spell);
        return spell;
    }

    /**
     * This method checks that a {@link Spell} object has a unique name for the current user.
     * <p>
     * It returns nothing, or throws an error if the name is not unique.
     *
     * @param spellDao the DAO to use in the static method
     * @param userName to include in the search
     * @param spellName to check against the DB
     */
    private static void spellNameUniqueness(SpellDao spellDao, String userName, String spellName) {
        if (spellDao.spellNameExists(userName, spellName)) {
            throw new DuplicateResourceException(String.format("Resource with name [%s] already exists", spellName));
        }
    }
}
