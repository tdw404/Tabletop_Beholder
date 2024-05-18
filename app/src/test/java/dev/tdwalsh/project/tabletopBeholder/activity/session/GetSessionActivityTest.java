package dev.tdwalsh.project.tabletopBeholder.activity.session;

import dev.tdwalsh.project.tabletopBeholder.activity.session.request.GetSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.GetSessionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SessionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.SessionNotFoundException;
import dev.tdwalsh.project.tabletopBeholder.resource.SessionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetSessionActivityTest {
    @InjectMocks
    private GetSessionActivity getSessionActivity;
    @Mock
    private SessionDao sessionDao;
    private GetSessionRequest getSessionRequest;
    private GetSessionResult getSessionResult;
    private Session session;
    private String userEmail;
    private String objectId;

    @BeforeEach
    public void setup() {
        openMocks(this);
        session = SessionHelper.provideSession(1);
        userEmail = session.getUserEmail();
        objectId = session.getObjectId();
        getSessionRequest = GetSessionRequest.builder()
                .withObjectId(objectId)
                .withUserEmail(userEmail)
                .build();
    }

    @Test
    public void handleRequest_objectsExistsOnDB_returnsResult() {
        //GIVEN
        doReturn(session).when(sessionDao).getSingle(userEmail, objectId);

        //WHEN
        GetSessionResult result = getSessionActivity.handleRequest(getSessionRequest);

        //THEN
        assertEquals(session, result.getSession(), "Expected returned object to contain value sent from DAO");
    }

    @Test
    public void handleRequest_objectsDoesNotExists_throwsError() {
        //GIVEN
        doReturn(null).when(sessionDao).getSingle(userEmail, objectId);

        //WHEN
        //THEN
        assertThrows(SessionNotFoundException.class, () -> getSessionActivity.handleRequest(getSessionRequest));
    }
}