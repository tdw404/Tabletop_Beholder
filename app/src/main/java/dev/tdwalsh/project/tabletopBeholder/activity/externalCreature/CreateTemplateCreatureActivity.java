package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature;

import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.request.CreateTemplateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result.CreateTemplateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.activity.helpers.CreateObjectHelper;
import dev.tdwalsh.project.tabletopBeholder.activity.helpers.NameHelper;
import dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators.TemplateCreatureTranslator;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateCreatureDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;

import javax.inject.Inject;

/**
 * GetTemplateCreatureActivity handles negotiation with {@link TemplateCreatureDao} to create a {@link TemplateCreature}.
 */
public class CreateTemplateCreatureActivity {

    private final TemplateCreatureDao templateCreatureDao;
    private final CreatureDao creatureDao;
    private final TemplateCreatureTranslator templateCreatureTranslator;

    /**
     * Instantiates a new activity object.
     *
     * @param templateCreatureDao DAO object necessary for this activity object to carry out its function.
     * @param creatureDao  DAO object necessary for this activity object to carry out its function.
     * @param templateCreatureTranslator Translator helper class
     */
    @Inject
    public CreateTemplateCreatureActivity(TemplateCreatureDao templateCreatureDao, CreatureDao creatureDao,
                                          TemplateCreatureTranslator templateCreatureTranslator) {
        this.templateCreatureDao = templateCreatureDao;
        this.creatureDao = creatureDao;
        this.templateCreatureTranslator = templateCreatureTranslator;
    }

    /**
     * This method retrieves an external Creature model by slug, translates it to a Beholder object
     * And then writes that object to the database.
     * @param createTemplateCreatureRequest - Request containing the target slug and user's identity.
     * @return CreateTemplateCreatureResult - Result continaing the translated beholder model.
     */
    public CreateTemplateCreatureResult handleRequest(CreateTemplateCreatureRequest createTemplateCreatureRequest) {
        //First, retrieves template object from external API
        //Then, converts template object to Beholder object
        //Then, checks for name uniqueness and throws error if not unique
        //Then, iterates over all spells - if spell does not exist, creates spell. Otherwise, adds a link to that spell.
        // Finally, writes the new object to DynamoDB and returns the result
        Creature creature = templateCreatureTranslator.translate(templateCreatureDao.getSingle(createTemplateCreatureRequest.getSlug()),
                createTemplateCreatureRequest.getUserEmail());
        creature.setUserEmail(createTemplateCreatureRequest.getUserEmail());
        NameHelper.objectNameUniqueness(creatureDao, creature);

        return CreateTemplateCreatureResult.builder()
                .withCreature((Creature) CreateObjectHelper.createObject(creatureDao, creature))
                .build();
    }


}
