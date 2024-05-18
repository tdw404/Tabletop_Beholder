package dev.tdwalsh.project.tabletopBeholder.activity.action;

import dev.tdwalsh.project.tabletopBeholder.activity.action.UpdateActionActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.action.request.UpdateActionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.UpdateActionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.ActionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.ActionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdateActionActivityTest {
    @InjectMocks
    private UpdateActionActivity updateActionActivity;
    @Mock
    private ActionDao actionDao;
    private UpdateActionRequest updateActionRequest;
    private Action oldAction;
    private Action newAction;

    @BeforeEach
    public void setup() {
        openMocks(this);
        oldAction = ActionHelper.provideAction(1);
        newAction = ActionHelper.provideAction(2);
        newAction.setObjectId(oldAction.getObjectId());
        newAction.setUserEmail(oldAction.getUserEmail());
        updateActionRequest = UpdateActionRequest.builder()
                .withAction(newAction)
                .withUserEmail(newAction.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_objectNameSame_returnsResult() {
        //GIVEN
        doReturn(oldAction).when(actionDao).getSingle(newAction.getUserEmail(), newAction.getObjectId());

        //WHEN
        UpdateActionResult result = updateActionActivity.handleRequest(updateActionRequest);
        //THEN
        verify(actionDao).writeObject(newAction);
        assertEquals(newAction, result.getAction(), "Expected returned object to contain new value");
    }

    @Test
    public void handleRequest_objectNameDifferent_returnsResult() {
        //GIVEN
        newAction.setObjectName("different");
        doReturn(oldAction).when(actionDao).getSingle(newAction.getUserEmail(), newAction.getObjectId());
        doReturn(false).when(actionDao).objectNameExists(newAction.getUserEmail(), newAction.getObjectName());
        //WHEN
        UpdateActionResult result = updateActionActivity.handleRequest(updateActionRequest);
        //THEN
        verify(actionDao).writeObject(newAction);
        assertEquals(newAction, result.getAction(), "Expected returned object to contain new value");
    }

    @Test
    public void handleRequest_objectNameNotUnique_throwsError() {
        //GIVEN
        newAction.setObjectName("different");
        doReturn(oldAction).when(actionDao).getSingle(newAction.getUserEmail(), newAction.getObjectId());
        doReturn(true).when(actionDao).objectNameExists(newAction.getUserEmail(), newAction.getObjectName());
        //WHEN
        //THEN
        assertThrows(DuplicateResourceException.class, () -> updateActionActivity.handleRequest(updateActionRequest));
    }

    @Test
    public void handleRequest_noOldObjectFound_throwsError() {
        //GIVEN
        newAction.setObjectName("different");
        doReturn(null).when(actionDao).getSingle(newAction.getUserEmail(), newAction.getObjectId());
        //WHEN
        //THEN
        assertThrows(MissingResourceException.class, () -> updateActionActivity.handleRequest(updateActionRequest));
    }

}