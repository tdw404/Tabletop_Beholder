package dev.tdwalsh.project.tabletopBeholder.activity.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.encounter.UpdateEncounterActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.UpdateEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.UpdateEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.EncounterHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdateEncounterActivityTest {
    @InjectMocks
    private UpdateEncounterActivity updateEncounterActivity;
    @Mock
    private EncounterDao encounterDao;
    private UpdateEncounterRequest updateEncounterRequest;
    private Encounter oldEncounter;
    private Encounter newEncounter;

    @BeforeEach
    public void setup() {
        openMocks(this);
        oldEncounter = EncounterHelper.provideEncounter(1);
        newEncounter = EncounterHelper.provideEncounter(2);
        newEncounter.setObjectId(oldEncounter.getObjectId());
        newEncounter.setUserEmail(oldEncounter.getUserEmail());
        updateEncounterRequest = UpdateEncounterRequest.builder()
                .withEncounter(newEncounter)
                .withUserEmail(newEncounter.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_objectNameSame_returnsResult() {
        //GIVEN
        doReturn(oldEncounter).when(encounterDao).getSingle(newEncounter.getUserEmail(), newEncounter.getObjectId());

        //WHEN
        UpdateEncounterResult result = updateEncounterActivity.handleRequest(updateEncounterRequest);
        //THEN
        verify(encounterDao).writeObject(newEncounter);
        assertEquals(newEncounter, result.getEncounter(), "Expected returned object to contain new value");
    }

    @Test
    public void handleRequest_objectNameDifferent_returnsResult() {
        //GIVEN
        newEncounter.setObjectName("different");
        doReturn(oldEncounter).when(encounterDao).getSingle(newEncounter.getUserEmail(), newEncounter.getObjectId());
        doReturn(false).when(encounterDao).objectNameExists(newEncounter.getUserEmail(), newEncounter.getObjectName());
        //WHEN
        UpdateEncounterResult result = updateEncounterActivity.handleRequest(updateEncounterRequest);
        //THEN
        verify(encounterDao).writeObject(newEncounter);
        assertEquals(newEncounter, result.getEncounter(), "Expected returned object to contain new value");
    }

    @Test
    public void handleRequest_noOldObjectFound_throwsError() {
        //GIVEN
        newEncounter.setObjectName("different");
        doReturn(null).when(encounterDao).getSingle(newEncounter.getUserEmail(), newEncounter.getObjectId());
        //WHEN
        //THEN
        assertThrows(MissingResourceException.class, () -> updateEncounterActivity.handleRequest(updateEncounterRequest));
    }

}