package dev.tdwalsh.project.tabletopBeholder.activity.action;

import dev.tdwalsh.project.tabletopBeholder.activity.action.GetAllActionsActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.action.request.GetAllActionsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.GetAllActionsResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.ActionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.resource.ActionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetAllActionsActivityTest {
    @InjectMocks
    private GetAllActionsActivity getAllActionsActivity;
    @Mock
    private ActionDao actionDao;
    private GetAllActionsRequest getAllActionsRequest;
    private Action actionOne;
    private Action actionTwo;
    private List<Action> actionList;

    @BeforeEach
    public void setup() {
        openMocks(this);
        actionOne = ActionHelper.provideAction(1);
        actionTwo = ActionHelper.provideAction(2);
        actionList = new ArrayList<>();
        actionList.add(actionOne);
        actionList.add(actionTwo);
        getAllActionsRequest = GetAllActionsRequest.builder()
                .withUserEmail(actionOne.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_withEmail_returnsResult() {
        //GIVEN
        doReturn(actionList).when(actionDao).getMultiple(actionOne.getUserEmail());

        //WHEN
        GetAllActionsResult result = getAllActionsActivity.handleRequest(getAllActionsRequest);

        //THEN
        assertEquals(actionList, result.getActionList(), "Expected returned object to contain value sent from DAO");
    }
}