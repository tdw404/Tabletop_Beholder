package dev.tdwalsh.project.tabletopBeholder.activity.session;

import dev.tdwalsh.project.tabletopBeholder.activity.session.CreateSessionActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.session.request.CreateSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.CreateSessionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SessionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.SessionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreateSessionActivityTest {
    @InjectMocks
    private CreateSessionActivity createSessionActivity;
    @Mock
    private SessionDao sessionDao;
    private CreateSessionRequest createSessionRequest;
    private CreateSessionResult createSessionResult;
    private Session session;


    @BeforeEach
    public void setup() {
        openMocks(this);
        session = SessionHelper.provideSession(1);
        createSessionRequest = CreateSessionRequest.builder()
                .withSession(session)
                .withUserEmail(session.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_objectNameUnique_returnsResult() {
        //GIVEN
        String orgSessionId = session.getObjectId();
        //WHEN
        CreateSessionResult result = createSessionActivity.handleRequest(createSessionRequest);

        //THEN
        assertEquals(session, result.getSession(), "Expected returned object to contain value sent from DAO");
        assertNotEquals(orgSessionId, result.getSession().getObjectId(), "Expected session to have been given a new ID");
    }

    @Test
    public void handleRequest_objectNameNotUnique_throwsError() {
        //GIVEN
        doReturn(true).when(sessionDao).objectNameExists(anyString(), anyString());
        //WHEN
        //THEN
        assertThrows(DuplicateResourceException.class, () -> createSessionActivity.handleRequest(createSessionRequest));
    }
}