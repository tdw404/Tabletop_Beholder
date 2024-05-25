package dev.tdwalsh.project.tabletopBeholder.activity.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.CreateEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.CreateEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.EncounterHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreateEncounterActivityTest {
    @InjectMocks
    private CreateEncounterActivity createEncounterActivity;
    @Mock
    private EncounterDao encounterDao;
    private CreateEncounterRequest createEncounterRequest;
    private CreateEncounterResult createEncounterResult;
    private Encounter encounter;


    @BeforeEach
    public void setup() {
        openMocks(this);
        encounter = EncounterHelper.provideEncounter(1);
        createEncounterRequest = CreateEncounterRequest.builder()
                .withEncounter(encounter)
                .withUserEmail(encounter.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_objectNameUnique_returnsResult() {
        //GIVEN
        String orgEncounterId = encounter.getObjectId();
        //WHEN
        CreateEncounterResult result = createEncounterActivity.handleRequest(createEncounterRequest);

        //THEN
        assertEquals(encounter, result.getEncounter(), "Expected returned object to contain value sent from DAO");
        assertNotEquals(orgEncounterId, result.getEncounter().getObjectId(), "Expected encounter to have been given a new ID");
    }
}