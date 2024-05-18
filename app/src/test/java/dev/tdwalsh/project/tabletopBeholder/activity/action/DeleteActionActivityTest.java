package dev.tdwalsh.project.tabletopBeholder.activity.action;

import dev.tdwalsh.project.tabletopBeholder.activity.action.DeleteActionActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.action.request.DeleteActionRequest;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.ActionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.resource.ActionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class DeleteActionActivityTest {
    @InjectMocks
    private DeleteActionActivity deleteActionActivity;
    @Mock
    private ActionDao actionDao;
    private DeleteActionRequest deleteActionRequest;
    private Action action;

    @BeforeEach
    public void setup() {
        openMocks(this);
        action = ActionHelper.provideAction(1);
        deleteActionRequest = DeleteActionRequest.builder()
                .withObjectId(action.getObjectId())
                .withUserEmail(action.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_validActionProvided_callsDaoDeleteMethod() {
        //GIVEN
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        //WHEN
        deleteActionActivity.handleRequest(deleteActionRequest);

        //THEN
        verify(actionDao, times(1)).deleteObject(emailCaptor.capture(), idCaptor.capture());
        assertEquals(action.getUserEmail(), emailCaptor.getValue(), "Expected activity class to make delete call with provided values");
        assertEquals(action.getObjectId(), idCaptor.getValue(), "Expected activity class to make delete call with provided values");
    }
}