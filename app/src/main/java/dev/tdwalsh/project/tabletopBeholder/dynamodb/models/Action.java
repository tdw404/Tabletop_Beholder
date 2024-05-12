package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import dev.tdwalsh.project.tabletopBeholder.converters.EffectConverter;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "TabletopBeholder_ActionTable")
public class Action implements BeholderObject {
    private String userEmail;
    private String actionId;
    private String actionName;
    private String actionType;
    private String actionDescription;
    private Integer uses;
    private Integer rechargeOn;
    private List<Effect> appliesEffects;

    @DynamoDBHashKey(attributeName = "userEmail")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @DynamoDBRangeKey(attributeName = "actionId")
    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    @DynamoDBAttribute(attributeName = "actionName")
    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
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

    @Override
    public int hashCode() {
        return Objects.hash(this.userEmail + this.actionId);
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
        return (this.userEmail.equals(other.userEmail)) && (this.actionId.equals(other.actionId));
    }

    @Override
    public String getObjectId() {
        return this.actionId;
    }

    @Override
    public String getObjectUserEmail() {
        return this.userEmail;
    }

    @Override
    public String getObjectName() {
        return this.actionName;
    }
}