package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import java.time.ZonedDateTime;

public interface BeholderObject {

    public String getObjectId();
    public String getUserEmail();
    public String getObjectName();
    public void setObjectId(String objectId);
    public void setCreateDateTime(ZonedDateTime createDateTime);
    public void setEditDateTime(ZonedDateTime editDateTime);
}
