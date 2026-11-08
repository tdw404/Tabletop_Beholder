package dev.tdwalsh.project.tabletopBeholder.dynamob.dao;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;
import software.amazon.awssdk.services.dynamodb.paginators.QueryIterable;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreatureDaoTest {
    private final DynamoDbClient client = mock(DynamoDbClient.class);
    private final CreatureDao dao = new CreatureDao(DynamoDbEnhancedClient.builder().dynamoDbClient(client).build());
    private final Map<String, AttributeValue> key = Map.of(
            "userEmail", AttributeValue.fromS("owner"), "objectId", AttributeValue.fromS("id"));
    private final Map<String, AttributeValue> storedItem = Map.of(
            "userEmail", AttributeValue.fromS("owner"), "objectId", AttributeValue.fromS("id"),
            "objectName", AttributeValue.fromS("Creature Name"));
    private final Creature item = new Creature();

    @BeforeEach
    void setUp() {
        item.setUserEmail("owner");
        item.setObjectId("id");
        item.setObjectName("Creature Name");
        when(client.queryPaginator(any(QueryRequest.class)))
                .thenAnswer(call -> new QueryIterable(client, call.getArgument(0)));
    }

    @Test
    void readsExistingRecord() {
        when(client.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().item(storedItem).build());

        assertEquals("Creature Name", dao.getSingle("owner", "id").getObjectName());

        ArgumentCaptor<GetItemRequest> request = ArgumentCaptor.forClass(GetItemRequest.class);
        verify(client).getItem(request.capture());
        assertEquals(Creature.TABLE_NAME, request.getValue().tableName());
        assertEquals(key, request.getValue().key());
    }

    @Test
    void missingRecordReturnsNull() {
        when(client.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().build());
        assertNull(dao.getSingle("owner", "id"));
    }

    @Test
    void listsRecordsForOwner() {
        when(client.query(any(QueryRequest.class))).thenReturn(QueryResponse.builder().items(storedItem).build());

        assertEquals("id", dao.getMultiple("owner").get(0).getObjectId());

        ArgumentCaptor<QueryRequest> request = ArgumentCaptor.forClass(QueryRequest.class);
        verify(client).query(request.capture());
        assertTrue(request.getValue().expressionAttributeValues().containsValue(AttributeValue.fromS("owner")));
    }

    @Test
    void createsWithoutOverwritingExistingId() {
        when(client.putItem(any(PutItemRequest.class))).thenReturn(PutItemResponse.builder().build());

        dao.createIfAbsent(item);

        ArgumentCaptor<PutItemRequest> request = ArgumentCaptor.forClass(PutItemRequest.class);
        verify(client).putItem(request.capture());
        assertEquals("attribute_not_exists(objectId)", request.getValue().conditionExpression());
        assertEquals(storedItem.get("objectName"), request.getValue().item().get("objectName"));
    }

    @Test
    void updatesExistingRecord() {
        when(client.updateItem(any(UpdateItemRequest.class)))
                .thenReturn(UpdateItemResponse.builder().attributes(storedItem).build());

        dao.writeObject(item);

        ArgumentCaptor<UpdateItemRequest> request = ArgumentCaptor.forClass(UpdateItemRequest.class);
        verify(client).updateItem(request.capture());
        assertEquals(key, request.getValue().key());
        assertEquals(ReturnValue.NONE, request.getValue().returnValues());
    }

    @Test
    void deletesByCompositeKey() {
        when(client.deleteItem(any(DeleteItemRequest.class))).thenReturn(DeleteItemResponse.builder().build());

        dao.deleteObject("owner", "id");

        ArgumentCaptor<DeleteItemRequest> request = ArgumentCaptor.forClass(DeleteItemRequest.class);
        verify(client).deleteItem(request.capture());
        assertEquals(key, request.getValue().key());
    }

    @Test
    void checksForExistingName() {
        when(client.query(any(QueryRequest.class)))
                .thenReturn(QueryResponse.builder().items(storedItem).build(), QueryResponse.builder().build());

        assertTrue(dao.objectNameExists("owner", "Creature Name"));
        assertFalse(dao.objectNameExists("owner", "Missing"));

        ArgumentCaptor<QueryRequest> request = ArgumentCaptor.forClass(QueryRequest.class);
        verify(client, times(2)).query(request.capture());
        assertEquals("CreaturesSortByNameIndex", request.getAllValues().get(0).indexName());
    }
}
