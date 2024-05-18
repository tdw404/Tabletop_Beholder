package dev.tdwalsh.project.tabletopBeholder.activity.session;

import dev.tdwalsh.project.tabletopBeholder.activity.session.UpdateSessionActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.session.request.UpdateSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.UpdateSessionResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SessionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.SessionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdateSessionActivityTest {
    @InjectMocks
    private UpdateSessionActivity updateSessionActivity;
    @Mock
    private SessionDao sessionDao;
    private UpdateSessionRequest updateSessionRequest;
    private Session oldSession;
    private Session newSession;

    @BeforeEach
    public void setup() {
        openMocks(this);
        oldSession = SessionHelper.provideSession(1);
        newSession = SessionHelper.provideSession(2);
        newSession.setObjectId(oldSession.getObjectId());
        newSession.setUserEmail(oldSession.getUserEmail());
        updateSessionRequest = UpdateSessionRequest.builder()
                .withSession(newSession)
                .withUserEmail(newSession.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_objectNameSame_returnsResult() {
        //GIVEN
        doReturn(oldSession).when(sessionDao).getSingle(newSession.getUserEmail(), newSession.getObjectId());

        //WHEN
        UpdateSessionResult result = updateSessionActivity.handleRequest(updateSessionRequest);
        //THEN
        verify(sessionDao).writeObject(newSession);
        assertEquals(newSession, result.getSession(), "Expected returned object to contain new value");
    }

    @Test
    public void handleRequest_objectNameDifferent_returnsResult() {
        //GIVEN
        newSession.setObjectName("different");
        doReturn(oldSession).when(sessionDao).getSingle(newSession.getUserEmail(), newSession.getObjectId());
        doReturn(false).when(sessionDao).objectNameExists(newSession.getUserEmail(), newSession.getObjectName());
        //WHEN
        UpdateSessionResult result = updateSessionActivity.handleRequest(updateSessionRequest);
        //THEN
        verify(sessionDao).writeObject(newSession);
        assertEquals(newSession, result.getSession(), "Expected returned object to contain new value");
    }

    @Test
    public void handleRequest_objectNameNotUnique_throwsError() {
        //GIVEN
        newSession.setObjectName("different");
        doReturn(oldSession).when(sessionDao).getSingle(newSession.getUserEmail(), newSession.getObjectId());
        doReturn(true).when(sessionDao).objectNameExists(newSession.getUserEmail(), newSession.getObjectName());
        //WHEN
        //THEN
        assertThrows(DuplicateResourceException.class, () -> updateSessionActivity.handleRequest(updateSessionRequest));
    }

    @Test
    public void handleRequest_noOldObjectFound_throwsError() {
        //GIVEN
        newSession.setObjectName("different");
        doReturn(null).when(sessionDao).getSingle(newSession.getUserEmail(), newSession.getObjectId());
        //WHEN
        //THEN
        assertThrows(MissingResourceException.class, () -> updateSessionActivity.handleRequest(updateSessionRequest));
    }

}