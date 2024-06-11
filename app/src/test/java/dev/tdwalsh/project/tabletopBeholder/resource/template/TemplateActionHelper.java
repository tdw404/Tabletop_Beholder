package dev.tdwalsh.project.tabletopBeholder.resource.template;

import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateAction;

public class TemplateActionHelper {

    public static TemplateAction provideTemplateAction(Integer mod) {
        TemplateAction templateAction = new TemplateAction();
        templateAction.setName("name" + mod);
        templateAction.setDesc("desc" + mod);
        templateAction.setAttack_bonus(mod);
        templateAction.setDamage_dice("damage_dice" + mod);
        return templateAction;
    }
}
