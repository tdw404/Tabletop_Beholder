package dev.tdwalsh.project.tabletopBeholder.activity.helpers;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.BeholderDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;

import java.util.UUID;

/**
 * This is a helper class to allow multiple activities to create a {@link BeholderObject} using the same method.
 */
public class CreateObjectHelper {

    /**
     * This method takes in a {@link BeholderObject} object, gives it a unique ID, then writes it to the database.
     * <p>
     * It then returns the created object.
     *
     * @param dao the DAO to use in the static method
     * @param beholderObject the {@link Spell} object to process and write
     * @return {@link Spell} the updated created object
     */
    public static BeholderObject createObject(BeholderDao dao, BeholderObject beholderObject) {
        beholderObject.setObjectId(UUID.randomUUID().toString());
        while (dao.getSingle(beholderObject.getUserEmail(), beholderObject.getObjectId()) != null) {
            beholderObject.setObjectId(UUID.randomUUID().toString());
        }
        dao.writeObject(beholderObject);
        return beholderObject;
    }
}
