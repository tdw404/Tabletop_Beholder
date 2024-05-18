package dev.tdwalsh.project.tabletopBeholder.dynamob.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.ActionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.resource.ActionHelper;
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

public class ActionDaoTest {
    @InjectMocks
    private ActionDao dao;
    @Mock
    private DynamoDBMapper mapper;
    private String userEmail;
    private String objectId;
    private Action action;
    private String objectName;
    @Mock
    private PaginatedQueryList<Action> paginatedQueryList;

    @BeforeEach
    public void setup() {
        openMocks(this);
        action = ActionHelper.provideAction(1);
        userEmail = action.getUserEmail();
        objectId = action.getObjectId();
        objectName = action.getObjectName();
    }

    @Test
    public void getSingle_actionExists_returnsAction() {
        //GIVEN
        doReturn(action).when(mapper).load(Action.class, userEmail, objectId);

        //WHEN
        Action result = dao.getSingle(userEmail, objectId);

        //THEN
        assertEquals(action, result, "Expected dao to return single result");
    }

    @Test
    public void getSingle_actionDoesNotExist_returnsNull() {
        //GIVEN
        doReturn(null).when(mapper).load(Action.class, userEmail, objectId);

        //WHEN
        Action result = dao.getSingle(userEmail, objectId);

        //THEN
        assertEquals(null, result, "Expected dao to return null result");
    }

    @Test
    public void getMultiple_userExists_returnsListofActions() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression<Action>> argumentCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Action.class), any(DynamoDBQueryExpression.class));

        //WHEN
        List<Action> result = (List<Action>) dao.getMultiple(userEmail);
        verify(mapper).query(eq(Action.class), argumentCaptor.capture());

        //THEN
        assertEquals(PaginatedQueryList.class, result.getClass(), "Expected dao to return list");
        assertEquals(userEmail, argumentCaptor.getValue().getHashKeyValues().getUserEmail());
    }

    @Test
    public void writeObject_withAction_callsDynamoDBSave() {
        //GIVEN
        ArgumentCaptor<Action> argumentCaptor = ArgumentCaptor.forClass(Action.class);

        //WHEN
        dao.writeObject(action);

        //THEN
        verify(mapper, times(1)).save(argumentCaptor.capture());
        assertEquals(action.getObjectId(), argumentCaptor.getValue().getObjectId());
        assertEquals(action.getUserEmail(), argumentCaptor.getValue().getUserEmail());
    }

    @Test
    public void deleteObject_withObjectIdAndUserEmail_callsDynamoDBDelete() {
        //GIVEN
        ArgumentCaptor<Action> argumentCaptor = ArgumentCaptor.forClass(Action.class);

        //WHEN
        dao.deleteObject(userEmail, objectId);

        //THEN
        verify(mapper, times(1)).delete(argumentCaptor.capture());
        assertEquals(action.getObjectId(), argumentCaptor.getValue().getObjectId());
        assertEquals(action.getUserEmail(), argumentCaptor.getValue().getUserEmail());
    }

    @Test
    public void objectNameExists_nameExists_returnsTrue() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression> queryCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Action.class), any(DynamoDBQueryExpression.class));
        doReturn(false).when(paginatedQueryList).isEmpty();

        //WHEN
        //THEN
        assertTrue(dao.objectNameExists(userEmail, objectName), "Expected method to return true if a matching name was found");
        verify(mapper, times(1)).query(eq(Action.class), queryCaptor.capture());
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":objectName").equals(new AttributeValue(objectName)));
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":userEmail").equals(new AttributeValue(userEmail)));
    }

    @Test
    public void objectNameExists_nameDoesNotExist_returnsFalse() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression> queryCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Action.class), any(DynamoDBQueryExpression.class));
        doReturn(true).when(paginatedQueryList).isEmpty();

        //WHEN

        //THEN
        assertFalse(dao.objectNameExists(userEmail, objectName), "Expected method to return true if a matching name was found");
        verify(mapper, times(1)).query(eq(Action.class), queryCaptor.capture());
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":objectName").equals(new AttributeValue(objectName)));
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":userEmail").equals(new AttributeValue(userEmail)));
    }
}
