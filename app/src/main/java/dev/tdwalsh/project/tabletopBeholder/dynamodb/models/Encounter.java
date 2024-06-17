package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import dev.tdwalsh.project.tabletopBeholder.converters.StringCreaturesMapConverter;
import dev.tdwalsh.project.tabletopBeholder.converters.ZonedDateTimeConverter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import org.apache.commons.text.WordUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

@DynamoDBTable(tableName = "TabletopBeholder_EncounterTable")
public class Encounter implements BeholderObject {
    private String userEmail;
    private String objectId;
    private String objectName;
    private Map<String, Creature> creatureMap;
    private Queue<Creature> turnQueue;
    private Integer encounterTurn;
    private List<String> turnOrder;
    private String topOfOrder;
    private String sessionId;
    private ZonedDateTime createDateTime;
    private ZonedDateTime editDateTime;

    @Override
    @DynamoDBHashKey(attributeName = "userEmail")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "EncountersSortBySessionIndex")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    @DynamoDBRangeKey(attributeName = "objectId")
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    @DynamoDBAttribute(attributeName = "objectName")
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = WordUtils.capitalizeFully(objectName);
    }

    @DynamoDBAttribute(attributeName = "creatureMap")
    @DynamoDBTypeConverted(converter = StringCreaturesMapConverter.class)
    public Map<String, Creature> getCreatureMap() {
        return this.creatureMap;
    }

    public void setCreatureMap(Map<String, Creature> creatureMap) {
        this.creatureMap = creatureMap;
    }

    @DynamoDBAttribute(attributeName = "createDateTime")
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    @Override
    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    @DynamoDBAttribute(attributeName = "editDateTime")
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getEditDateTime() {
        return editDateTime;
    }

    @Override
    public void setEditDateTime(ZonedDateTime editDateTime) {
        this.editDateTime = editDateTime;
    }

    @DynamoDBAttribute(attributeName = "turnQueue")
    public Queue<Creature> getTurnQueue() {
        return this.turnQueue;
    }

    public void setTurnQueue(Queue<Creature> turnQueue) {
        this.turnQueue = turnQueue;
    }

    @DynamoDBAttribute(attributeName = "encounterTurn")
    public Integer getEncounterTurn() {
        return encounterTurn;
    }

    public void setEncounterTurn(Integer encounterTurn) {
        this.encounterTurn = encounterTurn;
    }

    @DynamoDBAttribute(attributeName = "topOfOrder")
    public String getTopOfOrder() {
        return topOfOrder;
    }

    public void setTopOfOrder(String topOfOrder) {
        this.topOfOrder = topOfOrder;
    }

    @DynamoDBAttribute(attributeName = "turnOrder")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.SS)
    public List<String> getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(List<String> turnOrder) {
        this.turnOrder = turnOrder;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "EncountersSortBySessionIndex", attributeName = "sessionId")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userEmail + this.objectId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Encounter other = (Encounter) o;
        return (this.userEmail.equals(other.userEmail)) && (this.objectId.equals(other.objectId));
    }
}
