package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;

import java.util.List;

public interface BeholderDao {
    /**
     * Generic method for retrieving single object.
     * @param userEmail - user's e-mail.
     * @param objectId - object's id.
     * @return A single beholder object.
     */
    BeholderObject getSingle(String userEmail, String objectId);

    /**
     * Generic method for retrieving multiple objects at once.
     * @param userEmail - user's email.
     * @return - List of beholder objects.
     */
    List<? extends BeholderObject> getMultiple(String userEmail);

    /**
     * Generic method for saving object to DB.
     * @param object - object to save.
     */
    void writeObject(BeholderObject object);

    /**
     * Generic method for deleting object.
     * @param userEmail - user's email
     * @param objectId - object's id.
     */
    void deleteObject(String userEmail, String objectId);

    /**
     * Generic method, returns whether name exists.
     * @param userEmail - user's email.
     * @param objectName - object's name.
     * @return - Boolean, whether name exists.
     */
    Boolean objectNameExists(String userEmail, String objectName);
}
