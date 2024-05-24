package dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateAction;

import java.util.UUID;

public class TemplateActionTranslator {

    public static Action translate(TemplateAction templateAction, String actionType) {
        Action action = new Action();
        action.setObjectId(UUID.randomUUID().toString());
        action.setObjectName(templateAction.getName());
        action.setActionType(actionType);
        action.setActionDescription(templateAction.getDesc());
        //TODO extract more from description
        return action;
    }
}

