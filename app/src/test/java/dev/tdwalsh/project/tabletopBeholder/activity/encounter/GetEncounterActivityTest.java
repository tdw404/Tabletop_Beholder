package dev.tdwalsh.project.tabletopBeholder.activity.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.encounter.GetEncounterActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.GetEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.GetEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.EncounterNotFoundException;
import dev.tdwalsh.project.tabletopBeholder.resource.EncounterHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetEncounterActivityTest {
    @InjectMocks
    private GetEncounterActivity getEncounterActivity;
    @Mock
    private EncounterDao encounterDao;
    private GetEncounterRequest getEncounterRequest;
    private GetEncounterResult getEncounterResult;
    private Encounter encounter;
    private String userEmail;
    private String objectId;

    @BeforeEach
    public void setup() {
        openMocks(this);
        encounter = EncounterHelper.provideEncounter(1);
        userEmail = encounter.getUserEmail();
        objectId = encounter.getObjectId();
        getEncounterRequest = GetEncounterRequest.builder()
                .withObjectId(objectId)
                .withUserEmail(userEmail)
                .build();
    }

    @Test
    public void handleRequest_objectsExistsOnDB_returnsResult() {
        //GIVEN
        doReturn(encounter).when(encounterDao).getSingle(userEmail, objectId);

        //WHEN
        GetEncounterResult result = getEncounterActivity.handleRequest(getEncounterRequest);

        //THEN
        assertEquals(encounter, result.getEncounter(), "Expected returned object to contain value sent from DAO");
    }

    @Test
    public void handleRequest_objectsDoesNotExists_throwsError() {
        //GIVEN
        doReturn(null).when(encounterDao).getSingle(userEmail, objectId);

        //WHEN
        //THEN
        assertThrows(EncounterNotFoundException.class, () -> getEncounterActivity.handleRequest(getEncounterRequest));
    }
}