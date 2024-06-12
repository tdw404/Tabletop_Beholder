package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell;

import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request.CreateTemplateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result.CreateTemplateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.activity.helpers.CreateObjectHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.helpers.NameHelper;
import dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators.TemplateSpellTranslator;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateSpellDao;

import javax.inject.Inject;

/**
 * GetTemplateSpellActivity handles negotiation with {@link TemplateSpellDao}
 * to create a {@link dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell}.
 */
public class CreateTemplateSpellActivity {

    private final TemplateSpellDao templateSpellDao;
    private final SpellDao spellDao;

    /**
     * Constructor.
     * @param templateSpellDao DAO to process template spells
     * @param spellDao DAO to process Beholder objects
     */
    @Inject
    public CreateTemplateSpellActivity(TemplateSpellDao templateSpellDao, SpellDao spellDao) {
        this.templateSpellDao = templateSpellDao;
        this.spellDao = spellDao;
    }

    /**
     * This method retrieves a template spell, then converts it to a beholder object.
     * @param createTemplateSpellRequest request containing spell details
     * @return CreateTemplateSpellResult - result containing new beholder object
     */
    public CreateTemplateSpellResult handleRequest(CreateTemplateSpellRequest createTemplateSpellRequest) {
        //First, retrieves template object from external API
        //Then, converts template object to Beholder object
        //Then, checks for name uniqueness and throws error if not unique
        //Finally, writes the new object to DynamoDB and returns the result

        Spell spell = TemplateSpellTranslator.translate(templateSpellDao.getSingle(createTemplateSpellRequest.getSlug()));
        spell.setUserEmail(createTemplateSpellRequest.getUserEmail());
        NameHelper.objectNameUniqueness(spellDao, spell);
        return CreateTemplateSpellResult.builder()
                .withSpell((Spell) CreateObjectHelper.createObject(spellDao, spell))
                .build();
    }


}
