package dev.tdwalsh.project.tabletopBeholder.activity.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.CreateCreatureActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.CreateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.CreateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.CreatureHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreateCreatureActivityTest {
    @InjectMocks
    private CreateCreatureActivity createCreatureActivity;
    @Mock
    private CreatureDao creatureDao;
    private CreateCreatureRequest createCreatureRequest;

    private Creature creature;


    @BeforeEach
    public void setup() {
        openMocks(this);
        creature = CreatureHelper.provideCreature(1);
        createCreatureRequest = CreateCreatureRequest.builder()
                .withCreature(creature)
                .withUserEmail(creature.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_objectNameUnique_returnsResult() {
        //GIVEN
        String orgCreatureId = creature.getObjectId();
        //WHEN
        CreateCreatureResult result = createCreatureActivity.handleRequest(createCreatureRequest);

        //THEN
        assertEquals(creature, result.getCreature(), "Expected returned object to contain value sent from DAO");
        assertNotEquals(orgCreatureId, result.getCreature().getObjectId(), "Expected creature to have been given a new ID");
    }

    @Test
    public void handleRequest_objectNameNotUnique_throwsError() {
        //GIVEN
        doReturn(true).when(creatureDao).objectNameExists(anyString(), anyString());
        //WHEN
        //THEN
        assertThrows(DuplicateResourceException.class, () -> createCreatureActivity.handleRequest(createCreatureRequest));
    }
}