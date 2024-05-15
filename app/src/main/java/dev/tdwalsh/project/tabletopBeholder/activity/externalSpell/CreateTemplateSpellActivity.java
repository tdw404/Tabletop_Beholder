package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell;


import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request.CreateTemplateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result.CreateTemplateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateSpellDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

import javax.inject.Inject;

/**
 * GetTemplateSpellActivity handles negotiation with {@link TemplateSpellDao} to create a {@link dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell}.
 */
public class CreateTemplateSpellActivity {

    private final TemplateSpellDao templateSpellDao;

    @Inject
    public CreateTemplateSpellActivity(TemplateSpellDao templateSpellDao) {
        this.templateSpellDao = templateSpellDao;
    }

    public CreateTemplateSpellResult handleRequest(CreateTemplateSpellRequest createTemplateSpellRequest) {
        TemplateSpell templateSpell = templateSpellDao.getSingle(createTemplateSpellRequest.getSlug());


        return CreateTemplateSpellResult.builder().build();
    }


}
