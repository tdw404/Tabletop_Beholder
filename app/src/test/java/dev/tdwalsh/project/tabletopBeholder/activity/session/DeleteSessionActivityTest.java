package dev.tdwalsh.project.tabletopBeholder.activity.session;

import dev.tdwalsh.project.tabletopBeholder.activity.session.DeleteSessionActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.session.request.DeleteSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SessionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;
import dev.tdwalsh.project.tabletopBeholder.resource.SessionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class DeleteSessionActivityTest {
    @InjectMocks
    private DeleteSessionActivity deleteSessionActivity;
    @Mock
    private SessionDao sessionDao;
    private DeleteSessionRequest deleteSessionRequest;
    private Session session;

    @BeforeEach
    public void setup() {
        openMocks(this);
        session = SessionHelper.provideSession(1);
        deleteSessionRequest = DeleteSessionRequest.builder()
                .withObjectId(session.getObjectId())
                .withUserEmail(session.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_validSessionProvided_callsDaoDeleteMethod() {
        //GIVEN
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        //WHEN
        deleteSessionActivity.handleRequest(deleteSessionRequest);

        //THEN
        verify(sessionDao, times(1)).deleteObject(emailCaptor.capture(), idCaptor.capture());
        assertEquals(session.getUserEmail(), emailCaptor.getValue(), "Expected activity class to make delete call with provided values");
        assertEquals(session.getObjectId(), idCaptor.getValue(), "Expected activity class to make delete call with provided values");
    }
}