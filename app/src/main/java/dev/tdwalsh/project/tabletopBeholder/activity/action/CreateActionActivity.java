package dev.tdwalsh.project.tabletopBeholder.activity.action;

import dev.tdwalsh.project.tabletopBeholder.activity.action.request.CreateActionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.CreateActionResult;
import dev.tdwalsh.project.tabletopBeholder.activity.helpers.CreateObjectHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.helpers.NameHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.CreateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.CreateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.ActionDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

import javax.inject.Inject;

/**
 * GetActionActivity handles negotiation with {@link ActionDao} to create a {@link Action}.
 */
public class CreateActionActivity {
    private final ActionDao actionDao;

    /**
     * Instantiates a new activity object.
     *
     * @param actionDAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public CreateActionActivity(ActionDao actionDao) {
        this.actionDao = actionDao;
    };

    /**
     * This method handles the incoming request by creating a new {@link Action} on the database.
     * <p>
     * It then returns the created object.
     *
     * @param createActionRequest request object containing object attributes
     * @return {@link CreateActionResult} result object containing the created {@link Action}
     */

    public CreateActionResult handleRequest(CreateActionRequest createActionRequest) {
        //First, assigned contained object to new variable
        //Then, pulls userEmail from the authenticated email field in the request
        //Then, checks name for uniqueness
        //Then, uses the create helper to finish building the object
        //Finally, returns the newly created object
        Action action = createActionRequest.getAction();
        action.setUserEmail(createActionRequest.getUserEmail());
        NameHelper.objectNameUniqueness(actionDao, action);

        return CreateActionResult.builder()
                .withAction((Action) CreateObjectHelper.createObject(actionDao, action))
                .build();
    }
}
