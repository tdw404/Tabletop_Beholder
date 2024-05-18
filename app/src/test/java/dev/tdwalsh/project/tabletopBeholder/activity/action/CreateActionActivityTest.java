package dev.tdwalsh.project.tabletopBeholder.activity.action;

import dev.tdwalsh.project.tabletopBeholder.activity.action.CreateActionActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.action.request.CreateActionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.CreateActionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.ActionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.ActionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreateActionActivityTest {
    @InjectMocks
    private CreateActionActivity createActionActivity;
    @Mock
    private ActionDao actionDao;
    private CreateActionRequest createActionRequest;
    private Action action;


    @BeforeEach
    public void setup() {
        openMocks(this);
        action = ActionHelper.provideAction(1);
        createActionRequest = CreateActionRequest.builder()
                .withAction(action)
                .withUserEmail(action.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_objectNameUnique_returnsResult() {
        //GIVEN
        String orgActionId = action.getObjectId();
        //WHEN
        CreateActionResult result = createActionActivity.handleRequest(createActionRequest);

        //THEN
        assertEquals(action, result.getAction(), "Expected returned object to contain value sent from DAO");
        assertNotEquals(orgActionId, result.getAction().getObjectId(), "Expected action to have been given a new ID");
    }

    @Test
    public void handleRequest_objectNameNotUnique_throwsError() {
        //GIVEN
        doReturn(true).when(actionDao).objectNameExists(anyString(), anyString());
        //WHEN
        //THEN
        assertThrows(DuplicateResourceException.class, () -> createActionActivity.handleRequest(createActionRequest));
    }
}