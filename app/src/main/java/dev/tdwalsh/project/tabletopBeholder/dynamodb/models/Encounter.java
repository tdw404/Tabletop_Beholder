package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import dev.tdwalsh.project.tabletopBeholder.converters.CreatureQueueConverter;
import dev.tdwalsh.project.tabletopBeholder.converters.StringSetListConverter;
import dev.tdwalsh.project.tabletopBeholder.converters.StringCreaturesMapConverter;
import dev.tdwalsh.project.tabletopBeholder.converters.ZonedDateTimeConverter;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import org.apache.commons.text.WordUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

@DynamoDbBean
public class Encounter implements BeholderObject {
    public static final String TABLE_NAME = "TabletopBeholder_EncounterTable";
    private String userEmail;
    private String objectId;
    private String objectName;
    private Long version;
    private Map<String, Creature> creatureMap;
    private Queue<String> turnQueue;
    private Integer encounterRound;
    private List<String> turnOrder;
    private Queue<String> messageQueue;
    private String topOfOrder;
    private String sessionId;
    private ZonedDateTime createDateTime;
    private ZonedDateTime editDateTime;

    @Override
    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "EncountersSortBySessionIndex")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    @DynamoDbSortKey
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = WordUtils.capitalizeFully(objectName);
    }

    @DynamoDbConvertedBy(StringCreaturesMapConverter.class)
    public Map<String, Creature> getCreatureMap() {
        return this.creatureMap;
    }

    public void setCreatureMap(Map<String, Creature> creatureMap) {
        this.creatureMap = creatureMap;
    }

    @DynamoDbConvertedBy(ZonedDateTimeConverter.class)
    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    @Override
    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    @DynamoDbConvertedBy(ZonedDateTimeConverter.class)
    public ZonedDateTime getEditDateTime() {
        return editDateTime;
    }

    @Override
    public void setEditDateTime(ZonedDateTime editDateTime) {
        this.editDateTime = editDateTime;
    }

    @DynamoDbConvertedBy(CreatureQueueConverter.class)
    public Queue<String> getTurnQueue() {
        return this.turnQueue;
    }

    public void setTurnQueue(Queue<String> turnQueue) {
        this.turnQueue = turnQueue;
    }

    @DynamoDbAttribute("encounterTurn")
    public Integer getEncounterRound() {
        return encounterRound;
    }

    public void setEncounterRound(Integer encounterRound) {
        this.encounterRound = encounterRound;
    }

    public String getTopOfOrder() {
        return topOfOrder;
    }

    public void setTopOfOrder(String topOfOrder) {
        this.topOfOrder = topOfOrder;
    }

    @DynamoDbConvertedBy(StringSetListConverter.class)
    public List<String> getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(List<String> turnOrder) {
        this.turnOrder = turnOrder;
    }

    @DynamoDbConvertedBy(CreatureQueueConverter.class)
    public Queue<String> getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(Queue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }

    @DynamoDbSecondarySortKey(indexNames = "EncountersSortBySessionIndex")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = "".equals(sessionId) ? null : sessionId;
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
