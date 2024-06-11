package dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateAction;

import java.util.UUID;

public class TemplateActionTranslator {

    /**
     * Converts a text-block action from Open5E creatures to the model used by Beholder.
     * @param templateAction - Text block action to convert.
     * @param actionType - ActionType to assign to this action.
     * @return - Beholder Action model.
     */
    public static Action translate(TemplateAction templateAction, String actionType) {
        Action action = new Action();
        action.setObjectId(UUID.randomUUID().toString());
        action.setObjectName(templateAction.getName());
        action.setActionType(actionType);
        action.setActionDescription(templateAction.getDesc());
        return action;
    }
}

