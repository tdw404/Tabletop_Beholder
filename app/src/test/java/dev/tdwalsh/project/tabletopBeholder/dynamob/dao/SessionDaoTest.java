package dev.tdwalsh.project.tabletopBeholder.dynamob.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SessionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;
import dev.tdwalsh.project.tabletopBeholder.resource.SessionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class SessionDaoTest {
    @InjectMocks
    private SessionDao dao;
    @Mock
    private DynamoDBMapper mapper;
    private String userEmail;
    private String objectId;
    private Session session;
    private String objectName;
    @Mock
    private PaginatedQueryList<Session> paginatedQueryList;

    @BeforeEach
    public void setup() {
        openMocks(this);
        session = SessionHelper.provideSession(1);
        userEmail = session.getUserEmail();
        objectId = session.getObjectId();
        objectName = session.getObjectName();
    }

    @Test
    public void getSingle_sessionExists_returnsSession() {
        //GIVEN
        doReturn(session).when(mapper).load(Session.class, userEmail, objectId);

        //WHEN
        Session result = dao.getSingle(userEmail, objectId);

        //THEN
        assertEquals(session, result, "Expected dao to return single result");
    }

    @Test
    public void getSingle_sessionDoesNotExist_returnsNull() {
        //GIVEN
        doReturn(null).when(mapper).load(Session.class, userEmail, objectId);

        //WHEN
        Session result = dao.getSingle(userEmail, objectId);

        //THEN
        assertEquals(null, result, "Expected dao to return null result");
    }

    @Test
    public void getMultiple_userExists_returnsListofSessions() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression<Session>> argumentCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Session.class), any(DynamoDBQueryExpression.class));

        //WHEN
        List<Session> result = (List<Session>) dao.getMultiple(userEmail);
        verify(mapper).query(eq(Session.class), argumentCaptor.capture());

        //THEN
        assertEquals(PaginatedQueryList.class, result.getClass(), "Expected dao to return list");
        assertEquals(userEmail, argumentCaptor.getValue().getHashKeyValues().getUserEmail());
    }

    @Test
    public void writeObject_withSession_callsDynamoDBSave() {
        //GIVEN
        ArgumentCaptor<Session> argumentCaptor = ArgumentCaptor.forClass(Session.class);

        //WHEN
        dao.writeObject(session);

        //THEN
        verify(mapper, times(1)).save(argumentCaptor.capture());
        assertEquals(session.getObjectId(), argumentCaptor.getValue().getObjectId());
        assertEquals(session.getUserEmail(), argumentCaptor.getValue().getUserEmail());
    }

    @Test
    public void deleteObject_withObjectIdAndUserEmail_callsDynamoDBDelete() {
        //GIVEN
        ArgumentCaptor<Session> argumentCaptor = ArgumentCaptor.forClass(Session.class);

        //WHEN
        dao.deleteObject(userEmail, objectId);

        //THEN
        verify(mapper, times(1)).delete(argumentCaptor.capture());
        assertEquals(session.getObjectId(), argumentCaptor.getValue().getObjectId());
        assertEquals(session.getUserEmail(), argumentCaptor.getValue().getUserEmail());
    }

    @Test
    public void objectNameExists_nameExists_returnsTrue() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression> queryCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Session.class), any(DynamoDBQueryExpression.class));
        doReturn(false).when(paginatedQueryList).isEmpty();

        //WHEN
        //THEN
        assertTrue(dao.objectNameExists(userEmail, objectName), "Expected method to return true if a matching name was found");
        verify(mapper, times(1)).query(eq(Session.class), queryCaptor.capture());
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":objectName").equals(new AttributeValue(objectName)));
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":userEmail").equals(new AttributeValue(userEmail)));
    }

    @Test
    public void objectNameExists_nameDoesNotExist_returnsFalse() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression> queryCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Session.class), any(DynamoDBQueryExpression.class));
        doReturn(true).when(paginatedQueryList).isEmpty();

        //WHEN

        //THEN
        assertFalse(dao.objectNameExists(userEmail, objectName), "Expected method to return true if a matching name was found");
        verify(mapper, times(1)).query(eq(Session.class), queryCaptor.capture());
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":objectName").equals(new AttributeValue(objectName)));
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":userEmail").equals(new AttributeValue(userEmail)));
    }
}
