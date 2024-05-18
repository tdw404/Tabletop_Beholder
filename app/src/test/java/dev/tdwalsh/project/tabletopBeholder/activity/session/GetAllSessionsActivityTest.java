package dev.tdwalsh.project.tabletopBeholder.activity.session;

import dev.tdwalsh.project.tabletopBeholder.activity.session.GetAllSessionsActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.session.request.GetAllSessionsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.GetAllSessionsResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SessionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;
import dev.tdwalsh.project.tabletopBeholder.resource.SessionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetAllSessionsActivityTest {
    @InjectMocks
    private GetAllSessionsActivity getAllSessionsActivity;
    @Mock
    private SessionDao sessionDao;
    private GetAllSessionsRequest getAllSessionsRequest;
    private Session sessionOne;
    private Session sessionTwo;
    private List<Session> sessionList;

    @BeforeEach
    public void setup() {
        openMocks(this);
        sessionOne = SessionHelper.provideSession(1);
        sessionTwo = SessionHelper.provideSession(2);
        sessionList = new ArrayList<>();
        sessionList.add(sessionOne);
        sessionList.add(sessionTwo);
        getAllSessionsRequest = GetAllSessionsRequest.builder()
                .withUserEmail(sessionOne.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_withEmail_returnsResult() {
        //GIVEN
        doReturn(sessionList).when(sessionDao).getMultiple(sessionOne.getUserEmail());

        //WHEN
        GetAllSessionsResult result = getAllSessionsActivity.handleRequest(getAllSessionsRequest);

        //THEN
        assertEquals(sessionList, result.getSessionList(), "Expected returned object to contain value sent from DAO");
    }
}