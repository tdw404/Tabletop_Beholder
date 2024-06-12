package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.GetAllCreaturesResult;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.request.SearchTemplateCreaturesRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result.SearchTemplateCreaturesResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateCreatureDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;

import java.util.List;
import javax.inject.Inject;

/**
 * GetCreatureActivity handles negotiation with {@link TemplateCreatureDao} to retrieve a list of {@link TemplateCreature}.
 */
public class SearchTemplateCreaturesActivity {
    private final CreatureDao creatureDao;
    private final TemplateCreatureDao templateCreatureDao;

    /**
     * Instantiates a new activity object.
     *
     * @param creatureDao DAO object necessary for this activity object to carry out its function.
     * @param templateCreatureDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public SearchTemplateCreaturesActivity(CreatureDao creatureDao, TemplateCreatureDao templateCreatureDao) {
        this.creatureDao = creatureDao;
        this.templateCreatureDao = templateCreatureDao;
    };

    /**
     * This method handles the incoming request by retrieving a list of {@link TemplateCreature} from the database, if any exist.
     * <p>
     * It then returns the retrieved object, or an empty list if none is found.
     *
     * @param getMultipleTemplateCreaturesRequest request object containing DAO search parameters
     * @return {@link GetAllCreaturesResult} result object containing the retrieved {@link TemplateCreature} list
     */

    public SearchTemplateCreaturesResult handleRequest(SearchTemplateCreaturesRequest getMultipleTemplateCreaturesRequest) {
        List<TemplateCreature> templateCreatureList = templateCreatureDao.getMultiple(getMultipleTemplateCreaturesRequest.getSearchTerms());
        templateCreatureList
                .forEach(templateCreature -> {
                    if (creatureDao.objectNameExists(getMultipleTemplateCreaturesRequest.getUserEmail(), templateCreature.getName())) {
                        templateCreature.setResourceExists(true);
                    }
                });
        return SearchTemplateCreaturesResult.builder()
                .withTemplateCreatureList(templateCreatureList)
                .build();
    }
}
