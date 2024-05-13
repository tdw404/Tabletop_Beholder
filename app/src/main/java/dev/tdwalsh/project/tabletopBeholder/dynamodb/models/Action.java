package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import dev.tdwalsh.project.tabletopBeholder.converters.EffectConverter;
import dev.tdwalsh.project.tabletopBeholder.converters.ZonedDateTimeConverter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "TabletopBeholder_ActionTable")
public class Action implements BeholderObject {
    private String userEmail;
    private String objectId;
    private String objectName;
    private String actionType;
    private String actionDescription;
    private Integer uses;
    private Integer rechargeOn;
    private List<Effect> appliesEffects;
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
        this.objectName = objectName;
    }

    @DynamoDBAttribute(attributeName = "actionType")
    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @DynamoDBAttribute(attributeName = "actionDescription")
    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    @DynamoDBAttribute(attributeName = "uses")
    public Integer getUses() {
        return uses;
    }

    public void setUses(Integer uses) {
        this.uses = uses;
    }

    @DynamoDBAttribute(attributeName = "rechargeOn")
    public Integer getRechargeOn() {
        return rechargeOn;
    }

    public void setRechargeOn(Integer rechargeOn) {
        this.rechargeOn = rechargeOn;
    }

    @DynamoDBAttribute(attributeName = "appliesEffects")
    @DynamoDBTypeConverted(converter = EffectConverter.class)
    public List<Effect> getAppliesEffects() {
        return appliesEffects;
    }

    public void setAppliesEffects(List<Effect> appliesEffects) {
        this.appliesEffects = appliesEffects;
    }
    @DynamoDBAttribute(attributeName= "createDateTime")
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
        if(o == null) {
            return false;
        }
        if(this == o) {
            return true;
        }
        if(this.getClass() != o.getClass()) {
            return false;
        }
        Action other = (Action) o;
        return (this.userEmail.equals(other.userEmail)) && (this.objectId.equals(other.objectId));
    }
}