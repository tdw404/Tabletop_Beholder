package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import dev.tdwalsh.project.tabletopBeholder.converters.CreatureConverter;
import dev.tdwalsh.project.tabletopBeholder.converters.ZonedDateTimeConverter;
import org.apache.commons.text.WordUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "TabletopBeholder_EncounterTable")
public class Encounter implements BeholderObject {
    private String userEmail;
    private String objectId;
    private String objectName;
    private List<Creature> creatureList;
    private ZonedDateTime createDateTime;
    private ZonedDateTime editDateTime;

    @Override
    @DynamoDBHashKey(attributeName = "userEmail")
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
        this.objectName = WordUtils.capitalizeFully(objectId);
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