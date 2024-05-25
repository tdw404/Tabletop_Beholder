package dev.tdwalsh.project.tabletopBeholder.activity.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.helpers.NameHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.UpdateEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.UpdateEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * GetEncounterActivity handles negotiation with {@link EncounterDao} to create a {@link Encounter}.
 */
public class UpdateEncounterActivity {
    private final EncounterDao encounterDao;

    /**
     * Instantiates a new activity object.
     *
     * @param encounterDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public UpdateEncounterActivity(EncounterDao encounterDao) {
        this.encounterDao = encounterDao;
    };

    /**
     * This method handles the incoming request by updating a {@link Encounter} on the database if it exists.
     * <p>
     * It then returns the updated object.
     *
     * @param updateEncounterRequest request object containing object attributes
     * @return {@link UpdateEncounterResult} result object containing the created {@link Encounter}
     */

    public UpdateEncounterResult handleRequest(UpdateEncounterRequest updateEncounterRequest) {
        //First, assigns the updated object to a new variable
        //Then, pulls userEmail from the authenticated email field in the request
        //Then, retrieves the previous version from the DB, or throws an error if it does not exist
        //Then, if the name has changed, checks for name uniqueness, and throws an error if violated
        //Then, writes the new version to the DB
        //Finally, returns the updated version
        Encounter newEncounter  = updateEncounterRequest.getEncounter();
        newEncounter.setUserEmail(updateEncounterRequest.getUserEmail());
        Encounter oldEncounter = Optional.ofNullable(encounterDao.getSingle(newEncounter.getUserEmail(), newEncounter.getObjectId())).orElseThrow(() -> new MissingResourceException(String.format("Resource with id [%s] could not be retrieved from database", newEncounter.getObjectId())));
        newEncounter.setCreateDateTime(oldEncounter.getCreateDateTime());
        newEncounter.setEditDateTime(ZonedDateTime.now());
        encounterDao.writeObject(newEncounter);
        return UpdateEncounterResult.builder()
                .withEncounter(newEncounter)
                .build();
    }
}
