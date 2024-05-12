package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

public interface BeholderObject {

    public String getObjectId();
    public String getObjectUserEmail();
    public String getObjectName();
    public void setObjectId(String objectId);
}
