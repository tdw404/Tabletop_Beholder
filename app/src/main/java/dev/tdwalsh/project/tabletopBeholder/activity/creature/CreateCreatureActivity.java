package dev.tdwalsh.project.tabletopBeholder.activity.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.helpers.CreateObjectHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.helpers.NameHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.CreateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.CreateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;

import javax.inject.Inject;

/**
 * GetCreatureActivity handles negotiation with {@link CreatureDao} to create a {@link Creature}.
 */
public class CreateCreatureActivity {
    private final CreatureDao creatureDao;

    /**
     * Instantiates a new activity object.
     *
     * @param creatureDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public CreateCreatureActivity(CreatureDao creatureDao) {
        this.creatureDao = creatureDao;
    };

    /**
     * This method handles the incoming request by creating a new {@link Creature} on the database.
     * <p>
     * It then returns the created object.
     *
     * @param createCreatureRequest request object containing object attributes
     * @return {@link CreateCreatureResult} result object containing the created {@link Creature}
     */

    public CreateCreatureResult handleRequest(CreateCreatureRequest createCreatureRequest) {
        //First, assigned contained object to new variable
        //Then, pulls userEmail from the authenticated email field in the request
        //Then, checks name for uniqueness
        //Then, uses the create helper to finish building the object
        //Finally, returns the newly created object
        Creature creature = createCreatureRequest.getCreature();
        creature.setUserEmail(createCreatureRequest.getUserEmail());
        NameHelper.objectNameUniqueness(creatureDao, creature);

        return CreateCreatureResult.builder()
                .withCreature((Creature)CreateObjectHelper.createObject(creatureDao, creature))
                .build();
    }
}
