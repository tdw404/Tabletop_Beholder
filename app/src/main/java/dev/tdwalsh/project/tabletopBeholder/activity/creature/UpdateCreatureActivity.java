package dev.tdwalsh.project.tabletopBeholder.activity.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.UpdateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.UpdateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.activity.helpers.NameHelper;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;

import java.time.ZonedDateTime;
import java.util.Optional;
import javax.inject.Inject;

/**
 * GetCreatureActivity handles negotiation with {@link CreatureDao} to create a {@link Creature}.
 */
public class UpdateCreatureActivity {
    private final CreatureDao creatureDao;

    /**
     * Instantiates a new activity object.
     *
     * @param creatureDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public UpdateCreatureActivity(CreatureDao creatureDao) {
        this.creatureDao = creatureDao;
    };

    /**
     * This method handles the incoming request by updating a {@link Creature} on the database if it exists.
     * <p>
     * It then returns the updated object.
     *
     * @param updateCreatureRequest request object containing object attributes
     * @return {@link UpdateCreatureResult} result object containing the created {@link Creature}
     */

    public UpdateCreatureResult handleRequest(UpdateCreatureRequest updateCreatureRequest) {
        //First, assigns the updated object to a new variable
        //Then, pulls userEmail from the authenticated email field in the request
        //Then, retrieves the previous version from the DB, or throws an error if it does not exist
        //Then, if the name has changed, checks for name uniqueness, and throws an error if violated
        //Then, writes the new version to the DB
        //Finally, returns the updated version
        Creature newCreature  = updateCreatureRequest.getCreature();
        newCreature.setUserEmail(updateCreatureRequest.getUserEmail());
        Creature oldCreature = Optional.ofNullable(creatureDao.getSingle(newCreature.getUserEmail(), newCreature.getObjectId())).orElseThrow(()
            -> new MissingResourceException(String.format("Resource with id [%s] could not be retrieved from database", newCreature.getObjectId())));
        if (!newCreature.getObjectName().equals(oldCreature.getObjectName())) {
            NameHelper.objectNameUniqueness(creatureDao, newCreature);
        }
        newCreature.setCreateDateTime(oldCreature.getCreateDateTime());
        newCreature.setEditDateTime(ZonedDateTime.now());
        creatureDao.writeObject(newCreature);
        return UpdateCreatureResult.builder()
                .withCreature(newCreature)
                .build();
    }
}
