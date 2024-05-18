package dev.tdwalsh.project.tabletopBeholder.activity.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.GetCreatureActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.GetCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.GetCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.CreatureNotFoundException;
import dev.tdwalsh.project.tabletopBeholder.resource.CreatureHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetCreatureActivityTest {
    @InjectMocks
    private GetCreatureActivity getCreatureActivity;
    @Mock
    private CreatureDao creatureDao;
    private GetCreatureRequest getCreatureRequest;
    private GetCreatureResult getCreatureResult;
    private Creature creature;
    private String userEmail;
    private String objectId;

    @BeforeEach
    public void setup() {
        openMocks(this);
        creature = CreatureHelper.provideCreature(1);
        userEmail = creature.getUserEmail();
        objectId = creature.getObjectId();
        getCreatureRequest = GetCreatureRequest.builder()
                .withObjectId(objectId)
                .withUserEmail(userEmail)
                .build();
    }

    @Test
    public void handleRequest_objectsExistsOnDB_returnsResult() {
        //GIVEN
        doReturn(creature).when(creatureDao).getSingle(userEmail, objectId);

        //WHEN
        GetCreatureResult result = getCreatureActivity.handleRequest(getCreatureRequest);

        //THEN
        assertEquals(creature, result.getCreature(), "Expected returned object to contain value sent from DAO");
    }

    @Test
    public void handleRequest_objectsDoesNotExists_throwsError() {
        //GIVEN
        doReturn(null).when(creatureDao).getSingle(userEmail, objectId);

        //WHEN
        //THEN
        assertThrows(CreatureNotFoundException.class, () -> getCreatureActivity.handleRequest(getCreatureRequest));
    }
}