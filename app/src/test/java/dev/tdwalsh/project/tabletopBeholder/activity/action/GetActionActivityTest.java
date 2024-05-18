package dev.tdwalsh.project.tabletopBeholder.activity.action;

import dev.tdwalsh.project.tabletopBeholder.activity.action.GetActionActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.action.request.GetActionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.GetActionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.ActionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.ActionNotFoundException;
import dev.tdwalsh.project.tabletopBeholder.resource.ActionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetActionActivityTest {
    @InjectMocks
    private GetActionActivity getActionActivity;
    @Mock
    private ActionDao actionDao;
    private GetActionRequest getActionRequest;
    private GetActionResult getActionResult;
    private Action action;
    private String userEmail;
    private String objectId;

    @BeforeEach
    public void setup() {
        openMocks(this);
        action = ActionHelper.provideAction(1);
        userEmail = action.getUserEmail();
        objectId = action.getObjectId();
        getActionRequest = GetActionRequest.builder()
                .withObjectId(objectId)
                .withUserEmail(userEmail)
                .build();
    }

    @Test
    public void handleRequest_objectsExistsOnDB_returnsResult() {
        //GIVEN
        doReturn(action).when(actionDao).getSingle(userEmail, objectId);

        //WHEN
        GetActionResult result = getActionActivity.handleRequest(getActionRequest);

        //THEN
        assertEquals(action, result.getAction(), "Expected returned object to contain value sent from DAO");
    }

    @Test
    public void handleRequest_objectsDoesNotExists_throwsError() {
        //GIVEN
        doReturn(null).when(actionDao).getSingle(userEmail, objectId);

        //WHEN
        //THEN
        assertThrows(ActionNotFoundException.class, () -> getActionActivity.handleRequest(getActionRequest));
    }
}