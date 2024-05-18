package dev.tdwalsh.project.tabletopBeholder.dynamob.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.resource.EncounterHelper;
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

public class EncounterDaoTest {
    @InjectMocks
    private EncounterDao dao;
    @Mock
    private DynamoDBMapper mapper;
    private String userEmail;
    private String objectId;
    private Encounter encounter;
    private String objectName;
    @Mock
    private PaginatedQueryList<Encounter> paginatedQueryList;

    @BeforeEach
    public void setup() {
        openMocks(this);
        encounter = EncounterHelper.provideEncounter(1);
        userEmail = encounter.getUserEmail();
        objectId = encounter.getObjectId();
        objectName = encounter.getObjectName();
    }

    @Test
    public void getSingle_encounterExists_returnsEncounter() {
        //GIVEN
        doReturn(encounter).when(mapper).load(Encounter.class, userEmail, objectId);

        //WHEN
        Encounter result = dao.getSingle(userEmail, objectId);

        //THEN
        assertEquals(encounter, result, "Expected dao to return single result");
    }

    @Test
    public void getSingle_encounterDoesNotExist_returnsNull() {
        //GIVEN
        doReturn(null).when(mapper).load(Encounter.class, userEmail, objectId);

        //WHEN
        Encounter result = dao.getSingle(userEmail, objectId);

        //THEN
        assertEquals(null, result, "Expected dao to return null result");
    }

    @Test
    public void getMultiple_userExists_returnsListofEncounters() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression<Encounter>> argumentCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Encounter.class), any(DynamoDBQueryExpression.class));

        //WHEN
        List<Encounter> result = (List<Encounter>) dao.getMultiple(userEmail);
        verify(mapper).query(eq(Encounter.class), argumentCaptor.capture());

        //THEN
        assertEquals(PaginatedQueryList.class, result.getClass(), "Expected dao to return list");
        assertEquals(userEmail, argumentCaptor.getValue().getHashKeyValues().getUserEmail());
    }

    @Test
    public void writeObject_withEncounter_callsDynamoDBSave() {
        //GIVEN
        ArgumentCaptor<Encounter> argumentCaptor = ArgumentCaptor.forClass(Encounter.class);

        //WHEN
        dao.writeObject(encounter);

        //THEN
        verify(mapper, times(1)).save(argumentCaptor.capture());
        assertEquals(encounter.getObjectId(), argumentCaptor.getValue().getObjectId());
        assertEquals(encounter.getUserEmail(), argumentCaptor.getValue().getUserEmail());
    }

    @Test
    public void deleteObject_withObjectIdAndUserEmail_callsDynamoDBDelete() {
        //GIVEN
        ArgumentCaptor<Encounter> argumentCaptor = ArgumentCaptor.forClass(Encounter.class);

        //WHEN
        dao.deleteObject(userEmail, objectId);

        //THEN
        verify(mapper, times(1)).delete(argumentCaptor.capture());
        assertEquals(encounter.getObjectId(), argumentCaptor.getValue().getObjectId());
        assertEquals(encounter.getUserEmail(), argumentCaptor.getValue().getUserEmail());
    }

    @Test
    public void objectNameExists_nameExists_returnsTrue() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression> queryCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Encounter.class), any(DynamoDBQueryExpression.class));
        doReturn(false).when(paginatedQueryList).isEmpty();

        //WHEN
        //THEN
        assertTrue(dao.objectNameExists(userEmail, objectName), "Expected method to return true if a matching name was found");
        verify(mapper, times(1)).query(eq(Encounter.class), queryCaptor.capture());
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":objectName").equals(new AttributeValue(objectName)));
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":userEmail").equals(new AttributeValue(userEmail)));
    }

    @Test
    public void objectNameExists_nameDoesNotExist_returnsFalse() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression> queryCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Encounter.class), any(DynamoDBQueryExpression.class));
        doReturn(true).when(paginatedQueryList).isEmpty();

        //WHEN

        //THEN
        assertFalse(dao.objectNameExists(userEmail, objectName), "Expected method to return true if a matching name was found");
        verify(mapper, times(1)).query(eq(Encounter.class), queryCaptor.capture());
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":objectName").equals(new AttributeValue(objectName)));
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":userEmail").equals(new AttributeValue(userEmail)));
    }
}
