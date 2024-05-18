package dev.tdwalsh.project.tabletopBeholder.activity.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.UpdateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.UpdateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.CreatureHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdateCreatureActivityTest {
    @InjectMocks
    private UpdateCreatureActivity updateCreatureActivity;
    @Mock
    private CreatureDao creatureDao;
    private UpdateCreatureRequest updateCreatureRequest;
    private Creature oldCreature;
    private Creature newCreature;

    @BeforeEach
    public void setup() {
        openMocks(this);
        oldCreature = CreatureHelper.provideCreature(1);
        newCreature = CreatureHelper.provideCreature(2);
        newCreature.setObjectId(oldCreature.getObjectId());
        newCreature.setUserEmail(oldCreature.getUserEmail());
        updateCreatureRequest = UpdateCreatureRequest.builder()
                .withCreature(newCreature)
                .withUserEmail(newCreature.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_objectNameSame_returnsResult() {
        //GIVEN
        doReturn(oldCreature).when(creatureDao).getSingle(newCreature.getUserEmail(), newCreature.getObjectId());

        //WHEN
        UpdateCreatureResult result = updateCreatureActivity.handleRequest(updateCreatureRequest);
        //THEN
        verify(creatureDao).writeObject(newCreature);
        assertEquals(newCreature, result.getCreature(), "Expected returned object to contain new value");
    }

    @Test
    public void handleRequest_objectNameDifferent_returnsResult() {
        //GIVEN
        newCreature.setObjectName("different");
        doReturn(oldCreature).when(creatureDao).getSingle(newCreature.getUserEmail(), newCreature.getObjectId());
        doReturn(false).when(creatureDao).objectNameExists(newCreature.getUserEmail(), newCreature.getObjectName());
        //WHEN
        UpdateCreatureResult result = updateCreatureActivity.handleRequest(updateCreatureRequest);
        //THEN
        verify(creatureDao).writeObject(newCreature);
        assertEquals(newCreature, result.getCreature(), "Expected returned object to contain new value");
    }

    @Test
    public void handleRequest_objectNameNotUnique_throwsError() {
        //GIVEN
        newCreature.setObjectName("different");
        doReturn(oldCreature).when(creatureDao).getSingle(newCreature.getUserEmail(), newCreature.getObjectId());
        doReturn(true).when(creatureDao).objectNameExists(newCreature.getUserEmail(), newCreature.getObjectName());
        //WHEN
        //THEN
        assertThrows(DuplicateResourceException.class, () -> updateCreatureActivity.handleRequest(updateCreatureRequest));
    }

    @Test
    public void handleRequest_noOldObjectFound_throwsError() {
        //GIVEN
        newCreature.setObjectName("different");
        doReturn(null).when(creatureDao).getSingle(newCreature.getUserEmail(), newCreature.getObjectId());
        //WHEN
        //THEN
        assertThrows(MissingResourceException.class, () -> updateCreatureActivity.handleRequest(updateCreatureRequest));
    }

}