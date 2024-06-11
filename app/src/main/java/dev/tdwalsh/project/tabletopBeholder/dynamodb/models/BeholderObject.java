package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import java.time.ZonedDateTime;

public interface BeholderObject {

    /**
     * Generic method for getting object's id.
     * @return - Object id.
     */
    String getObjectId();

    /**
     * Generic method for getting user email.
     * @return - user's email.
     */
    String getUserEmail();

    /**
     * Generic method for getting object name.
     * @return - Object's name.
     */
    String getObjectName();

    /**
     * Generic method for setting object Id.
     * @param objectId - Id to set.
     */
    void setObjectId(String objectId);

    /**
     * Generic method for setting create date time.
     * @param createDateTime - Create date time to set
     */
    void setCreateDateTime(ZonedDateTime createDateTime);

    /**
     * Generic method for setting object edit date time.
     * @param editDateTime - Edit date time to set
     */
    void setEditDateTime(ZonedDateTime editDateTime);
}
