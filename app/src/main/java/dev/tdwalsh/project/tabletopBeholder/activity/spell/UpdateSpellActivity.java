package dev.tdwalsh.project.tabletopBeholder.activity.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.helpers.NameHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.UpdateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.UpdateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;

import java.time.ZonedDateTime;
import java.util.Optional;
import javax.inject.Inject;

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
        //First, assigns the updated object to a new variable
        //Then, pulls userEmail from the authenticated email field in the request
        //Then, retrieves the previous version from the DB, or throws an error if it does not exist
        //Then, if the name has changed, checks for name uniqueness, and throws an error if violated
        //Then, writes the new version to the DB
        //Finally, returns the updated version
        Spell newSpell  = updateSpellRequest.getSpell();
        newSpell.setUserEmail(updateSpellRequest.getUserEmail());
        Spell oldSpell = Optional.ofNullable(spellDao.getSingle(newSpell.getUserEmail(), newSpell.getObjectId())).orElseThrow(() ->
                new MissingResourceException(String.format("Resource with id [%s] could not be retrieved from database", newSpell.getObjectId())));
        if (!newSpell.getObjectName().equals(oldSpell.getObjectName())) {
            NameHelper.objectNameUniqueness(spellDao, newSpell);
        }
        newSpell.setCreateDateTime(oldSpell.getCreateDateTime());
        newSpell.setEditDateTime(ZonedDateTime.now());
        spellDao.writeObject(newSpell);
        return UpdateSpellResult.builder()
                .withSpell(newSpell)
                .build();
    }
}
