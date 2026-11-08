package dev.tdwalsh.project.tabletopBeholder.activity.helpers;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.BeholderDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * This is a helper class to allow multiple activities to create a {@link BeholderObject} using the same method.
 */
public class CreateObjectHelper {
    private static final int MAX_CREATE_ATTEMPTS = 3;

    /**
     * This method takes in a {@link BeholderObject} object, gives it a unique ID, then writes it to the database.
     * <p>
     * It then returns the created object.
     *
     * @param dao the DAO to use in the static method
     * @param beholderObject the {@link BeholderObject} object to process and write
     * @return {@link BeholderObject} the updated created object
     */
    public static BeholderObject createObject(BeholderDao dao, BeholderObject beholderObject) {
        ZonedDateTime now = ZonedDateTime.now();
        beholderObject.setCreateDateTime(now);
        beholderObject.setEditDateTime(now);
        int attempt = 1;
        while (true) {
            beholderObject.setObjectId(UUID.randomUUID().toString());
            try {
                dao.createIfAbsent(beholderObject);
                return beholderObject;
            } catch (ConditionalCheckFailedException collision) {
                if (attempt == MAX_CREATE_ATTEMPTS) {
                    throw collision;
                }
                attempt++;
            }
        }
    }
}
