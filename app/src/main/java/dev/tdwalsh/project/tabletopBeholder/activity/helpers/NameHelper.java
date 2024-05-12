package dev.tdwalsh.project.tabletopBeholder.activity.helpers;


import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.BeholderDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;

/**
 * This is a helper class to allow multiple activities to create a {@link dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject} using the same method.
 */
public class NameHelper {

    /**
     * This method checks that a {@link dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject} object has a unique name for the current user.
     * <p>
     * It returns nothing, or throws an error if the name is not unique.
     *
     * @param dao the DAO to use in the static method
     * @param beholderObject to check
     */
    public static void objectNameUniqueness(BeholderDao dao, BeholderObject beholderObject) {
        if (dao.objectNameExists(beholderObject.getUserEmail(), beholderObject.getObjectName())) {
            throw new DuplicateResourceException(String.format("Resource with name [%s] already exists", beholderObject.getObjectName()));
        }
    }
}
