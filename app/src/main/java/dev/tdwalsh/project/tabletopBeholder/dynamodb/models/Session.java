package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import dev.tdwalsh.project.tabletopBeholder.converters.EncounterConverter;
import dev.tdwalsh.project.tabletopBeholder.converters.ZonedDateTimeConverter;

import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "ProjectBeholder_SessionTable")
public class Session implements BeholderObject {
    private String userEmail;
    private String sessionId;
    private String sessionName;
    private List<Encounter> encounterList;
    private ZonedDateTime createDateTime;
    private ZonedDateTime editDateTime;

    @DynamoDBHashKey(attributeName = "userEmail")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @DynamoDBRangeKey(attributeName = "sessionId")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @DynamoDBAttribute(attributeName = "sessionName")
    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    @DynamoDBAttribute(attributeName = "encounterList")
    @DynamoDBTypeConverted(converter = EncounterConverter.class)
    public List<Encounter> getEncounterList() {
        return encounterList;
    }

    public void setEncounterList(List<Encounter> encounterList) {
        this.encounterList = encounterList;
    }

    @DynamoDBAttribute(attributeName= "createDateTime")
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
        return Objects.hash(this.userEmail + this.sessionId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if(this.getClass() != o.getClass()) {
            return false;
        }

        Session other = (Session) o;
        return this.userEmail.equals(other.userEmail) && this.sessionId.equals(other.sessionId);
    }

    @Override
    public String getObjectId() {
        return this.sessionId;
    }

    @Override
    public String getObjectUserEmail() {
        return this.userEmail;
    }

    @Override
    public String getObjectName() {
        return this.sessionName;
    }
}