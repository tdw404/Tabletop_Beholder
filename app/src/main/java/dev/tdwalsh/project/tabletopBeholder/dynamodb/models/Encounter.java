package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import dev.tdwalsh.project.tabletopBeholder.converters.CreatureConverter;
import dev.tdwalsh.project.tabletopBeholder.converters.ZonedDateTimeConverter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "TabletopBeholder_EncounterTable")
public class Encounter implements BeholderObject {
    private String userEmail;
    private String encounterId;
    private String encounterName;
    private List<Creature> creatureList;
    private ZonedDateTime createDateTime;
    private ZonedDateTime editDateTime;

    @DynamoDBHashKey(attributeName = "userEmail")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @DynamoDBRangeKey(attributeName = "encounterId")
    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
    }

    @DynamoDBAttribute(attributeName = "encounterName")
    public String getEncounterName() {
        return encounterName;
    }

    public void setEncounterName(String encounterName) {
        this.encounterName = encounterName;
    }

    @DynamoDBAttribute(attributeName = "creatureList")
    @DynamoDBTypeConverted(converter = CreatureConverter.class)
    public List<Creature> getCreatureList() {
        return creatureList;
    }

    public void setCreatureList(List<Creature> creatureList) {
        this.creatureList = creatureList;
    }

    @DynamoDBAttribute(attributeName = "createDateTime")
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    @DynamoDBAttribute(attributeName = "editDateTime")
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getEditDateTime() {
        return editDateTime;
    }

    public void setEditDateTime(ZonedDateTime editDateTime) {
        this.editDateTime = editDateTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userEmail = this.encounterId);
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
        return (this.userEmail.equals(other.userEmail)) && (this.encounterId.equals(other.encounterId));
    }

    @Override
    public String getObjectId() {
        return this.encounterId;
    }

    @Override
    public String getObjectUserEmail() {
        return this.userEmail;
    }

    @Override
    public String getObjectName() {
        return this.encounterName;
    }

    @Override
    public void setObjectId(String objectId) {
        this.setEncounterId(objectId);
    }
}