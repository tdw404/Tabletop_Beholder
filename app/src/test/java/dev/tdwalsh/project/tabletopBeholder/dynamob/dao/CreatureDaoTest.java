package dev.tdwalsh.project.tabletopBeholder.dynamob.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.resource.CreatureHelper;
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

public class CreatureDaoTest {
    @InjectMocks
    private CreatureDao dao;
    @Mock
    private DynamoDBMapper mapper;
    private String userEmail;
    private String objectId;
    private Creature creature;
    private String objectName;
    @Mock
    private PaginatedQueryList<Creature> paginatedQueryList;

    @BeforeEach
    public void setup() {
        openMocks(this);
        creature = CreatureHelper.provideCreature(1);
        userEmail = creature.getUserEmail();
        objectId = creature.getObjectId();
        objectName = creature.getObjectName();
    }

    @Test
    public void getSingle_creatureExists_returnsCreature() {
        //GIVEN
        doReturn(creature).when(mapper).load(Creature.class, userEmail, objectId);

        //WHEN
        Creature result = dao.getSingle(userEmail, objectId);

        //THEN
        assertEquals(creature, result, "Expected dao to return single result");
    }

    @Test
    public void getSingle_creatureDoesNotExist_returnsNull() {
        //GIVEN
        doReturn(null).when(mapper).load(Creature.class, userEmail, objectId);

        //WHEN
        Creature result = dao.getSingle(userEmail, objectId);

        //THEN
        assertEquals(null, result, "Expected dao to return null result");
    }

    @Test
    public void getMultiple_userExists_returnsListofCreatures() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression<Creature>> argumentCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Creature.class), any(DynamoDBQueryExpression.class));

        //WHEN
        List<Creature> result = (List<Creature>) dao.getMultiple(userEmail);
        verify(mapper).query(eq(Creature.class), argumentCaptor.capture());

        //THEN
        assertEquals(PaginatedQueryList.class, result.getClass(), "Expected dao to return list");
        assertEquals(userEmail, argumentCaptor.getValue().getHashKeyValues().getUserEmail());
    }

    @Test
    public void writeObject_withCreature_callsDynamoDBSave() {
        //GIVEN
        ArgumentCaptor<Creature> argumentCaptor = ArgumentCaptor.forClass(Creature.class);

        //WHEN
        dao.writeObject(creature);

        //THEN
        verify(mapper, times(1)).save(argumentCaptor.capture());
        assertEquals(creature.getObjectId(), argumentCaptor.getValue().getObjectId());
        assertEquals(creature.getUserEmail(), argumentCaptor.getValue().getUserEmail());
    }

    @Test
    public void deleteObject_withObjectIdAndUserEmail_callsDynamoDBDelete() {
        //GIVEN
        ArgumentCaptor<Creature> argumentCaptor = ArgumentCaptor.forClass(Creature.class);

        //WHEN
        dao.deleteObject(userEmail, objectId);

        //THEN
        verify(mapper, times(1)).delete(argumentCaptor.capture());
        assertEquals(creature.getObjectId(), argumentCaptor.getValue().getObjectId());
        assertEquals(creature.getUserEmail(), argumentCaptor.getValue().getUserEmail());
    }

    @Test
    public void objectNameExists_nameExists_returnsTrue() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression> queryCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Creature.class), any(DynamoDBQueryExpression.class));
        doReturn(false).when(paginatedQueryList).isEmpty();

        //WHEN
        //THEN
        assertTrue(dao.objectNameExists(userEmail, objectName), "Expected method to return true if a matching name was found");
        verify(mapper, times(1)).query(eq(Creature.class), queryCaptor.capture());
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":objectName").equals(new AttributeValue(objectName)));
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":userEmail").equals(new AttributeValue(userEmail)));
    }

    @Test
    public void objectNameExists_nameDoesNotExist_returnsFalse() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression> queryCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Creature.class), any(DynamoDBQueryExpression.class));
        doReturn(true).when(paginatedQueryList).isEmpty();

        //WHEN

        //THEN
        assertFalse(dao.objectNameExists(userEmail, objectName), "Expected method to return true if a matching name was found");
        verify(mapper, times(1)).query(eq(Creature.class), queryCaptor.capture());
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":objectName").equals(new AttributeValue(objectName)));
        System.out.println(queryCaptor.getValue().getExpressionAttributeValues().get(":userEmail").equals(new AttributeValue(userEmail)));
    }
}
