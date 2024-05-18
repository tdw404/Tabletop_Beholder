package dev.tdwalsh.project.tabletopBeholder.activity.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.DeleteCreatureActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.DeleteCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.resource.CreatureHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class DeleteCreatureActivityTest {
    @InjectMocks
    private DeleteCreatureActivity deleteCreatureActivity;
    @Mock
    private CreatureDao creatureDao;
    private DeleteCreatureRequest deleteCreatureRequest;
    private Creature creature;

    @BeforeEach
    public void setup() {
        openMocks(this);
        creature = CreatureHelper.provideCreature(1);
        deleteCreatureRequest = DeleteCreatureRequest.builder()
                .withObjectId(creature.getObjectId())
                .withUserEmail(creature.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_validCreatureProvided_callsDaoDeleteMethod() {
        //GIVEN
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        //WHEN
        deleteCreatureActivity.handleRequest(deleteCreatureRequest);

        //THEN
        verify(creatureDao, times(1)).deleteObject(emailCaptor.capture(), idCaptor.capture());
        assertEquals(creature.getUserEmail(), emailCaptor.getValue(), "Expected activity class to make delete call with provided values");
        assertEquals(creature.getObjectId(), idCaptor.getValue(), "Expected activity class to make delete call with provided values");
    }
}