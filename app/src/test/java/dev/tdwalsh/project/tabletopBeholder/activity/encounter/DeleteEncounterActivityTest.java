package dev.tdwalsh.project.tabletopBeholder.activity.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.DeleteEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.resource.EncounterHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class DeleteEncounterActivityTest {
    @InjectMocks
    private DeleteEncounterActivity deleteEncounterActivity;
    @Mock
    private EncounterDao encounterDao;
    private DeleteEncounterRequest deleteEncounterRequest;
    private Encounter encounter;

    @BeforeEach
    public void setup() {
        openMocks(this);
        encounter = EncounterHelper.provideEncounter(1);
        deleteEncounterRequest = DeleteEncounterRequest.builder()
                .withObjectId(encounter.getObjectId())
                .withUserEmail(encounter.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_validEncounterProvided_callsDaoDeleteMethod() {
        //GIVEN
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        //WHEN
        deleteEncounterActivity.handleRequest(deleteEncounterRequest);

        //THEN
        verify(encounterDao, times(1)).deleteObject(emailCaptor.capture(), idCaptor.capture());
        assertEquals(encounter.getUserEmail(), emailCaptor.getValue(), "Expected activity class to make delete call with provided values");
        assertEquals(encounter.getObjectId(), idCaptor.getValue(), "Expected activity class to make delete call with provided values");
    }
}