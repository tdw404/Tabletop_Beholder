package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature;


import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.request.GetTemplateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result.GetTemplateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.TemplateCreatureNotFoundException;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateCreatureDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;

import javax.inject.Inject;
import java.util.Optional;

/**
 * GetTemplateCreatureActivity handles negotiation with {@link TemplateCreatureDao} to retrieve a single {@link TemplateCreature}.
 */
public class GetTemplateCreatureActivity {
    private final TemplateCreatureDao templateCreatureDao;
    private final CreatureDao creatureDao;

    /**
     * Instantiates a new activity object.
     *
     * @param templateCreatureDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetTemplateCreatureActivity(TemplateCreatureDao templateCreatureDao, CreatureDao creatureDao) {
        this.templateCreatureDao = templateCreatureDao;
        this.creatureDao = creatureDao;
    };

    /**
     * This method handles the incoming request by retrieving a {@link TemplateCreature} from the database, if it exists.
     * <p>
     * It then returns the retrieved object, or throws a {@link TemplateCreatureNotFoundException} if none is found.
     *
     * @param getTemplateCreatureRequest request object containing DAO search parameters
     * @return {@link GetTemplateCreatureResult} result object containing the retrieved {@link TemplateCreature}
     */

    public GetTemplateCreatureResult handleRequest(GetTemplateCreatureRequest getTemplateCreatureRequest) {
        TemplateCreature templateCreature = Optional.ofNullable(templateCreatureDao.getSingle(getTemplateCreatureRequest.getSlug()))
                .orElseThrow(() -> new TemplateCreatureNotFoundException(String.format("Could not find a templateCreature for [%s] with slug [%s]", getTemplateCreatureRequest.getUserEmail(), getTemplateCreatureRequest.getSlug())));
        if(creatureDao.objectNameExists(getTemplateCreatureRequest.getUserEmail(), templateCreature.getName())) {
            templateCreature.setResourceExists(true);
        }
        return GetTemplateCreatureResult.builder()
                .withTemplateCreature(templateCreature)
                .build();
    }
}
