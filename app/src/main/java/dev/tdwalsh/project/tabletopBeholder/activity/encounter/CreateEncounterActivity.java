package dev.tdwalsh.project.tabletopBeholder.activity.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.helpers.CreateObjectHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.helpers.NameHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.CreateEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.CreateEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

import javax.inject.Inject;

/**
 * GetEncounterActivity handles negotiation with {@link EncounterDao} to create a {@link Encounter}.
 */
public class CreateEncounterActivity {
    private final EncounterDao encounterDao;

    /**
     * Instantiates a new activity object.
     *
     * @param encounterDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public CreateEncounterActivity(EncounterDao encounterDao) {
        this.encounterDao = encounterDao;
    };

    /**
     * This method handles the incoming request by creating a new {@link Encounter} on the database.
     * <p>
     * It then returns the created object.
     *
     * @param createEncounterRequest request object containing object attributes
     * @return {@link CreateEncounterResult} result object containing the created {@link Encounter}
     */

    public CreateEncounterResult handleRequest(CreateEncounterRequest createEncounterRequest) {
        //First, assigned contained object to new variable
        //Then, pulls userEmail from the authenticated email field in the request
        //Then, checks name for uniqueness
        //Then, uses the create helper to finish building the object
        //Finally, returns the newly created object
        Encounter encounter = createEncounterRequest.getEncounter();
        encounter.setUserEmail(createEncounterRequest.getUserEmail());
        NameHelper.objectNameUniqueness(encounterDao, encounter);

        return CreateEncounterResult.builder()
                .withEncounter((Encounter)CreateObjectHelper.createObject(encounterDao, encounter))
                .build();
    }
}
