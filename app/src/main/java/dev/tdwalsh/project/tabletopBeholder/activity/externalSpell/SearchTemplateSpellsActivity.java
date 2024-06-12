package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell;

import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request.SearchTemplateSpellsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result.SearchTemplateSpellsResult;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetAllSpellsResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateSpellDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

import java.util.List;
import javax.inject.Inject;

/**
 * GetSpellActivity handles negotiation with {@link TemplateSpellDao} to retrieve a list of {@link TemplateSpell}.
 */
public class SearchTemplateSpellsActivity {
    private final SpellDao spellDao;
    private final TemplateSpellDao templateSpellDao;

    /**
     * Instantiates a new activity object.
     *
     * @param spellDao DAO object necessary for this activity object to carry out its function.
     * @param templateSpellDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public SearchTemplateSpellsActivity(SpellDao spellDao, TemplateSpellDao templateSpellDao) {
        this.spellDao = spellDao;
        this.templateSpellDao = templateSpellDao;
    };

    /**
     * This method handles the incoming request by retrieving a list of {@link TemplateSpell} from the database, if any exist.
     * <p>
     * It then returns the retrieved object, or an empty list if none is found.
     *
     * @param getMultipleTemplateSpellsRequest request object containing DAO search parameters
     * @return {@link GetAllSpellsResult} result object containing the retrieved {@link TemplateSpell} list
     */

    public SearchTemplateSpellsResult handleRequest(SearchTemplateSpellsRequest getMultipleTemplateSpellsRequest) {
        List<TemplateSpell> templateSpellList = templateSpellDao.getMultiple(getMultipleTemplateSpellsRequest.getSearchTerms());
        templateSpellList
                .forEach(templateSpell -> {
                    if (spellDao.objectNameExists(getMultipleTemplateSpellsRequest.getUserEmail(), templateSpell.getName())) {
                        templateSpell.setResourceExists(true);
                    }
                });
        return SearchTemplateSpellsResult.builder()
                .withTemplateSpellList(templateSpellList)
                .build();
    }
}
