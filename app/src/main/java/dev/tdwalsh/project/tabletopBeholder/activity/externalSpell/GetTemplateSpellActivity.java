package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell;

import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request.GetTemplateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result.GetTemplateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.TemplateSpellNotFoundException;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateSpellDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

import java.util.Optional;
import javax.inject.Inject;

/**
 * GetTemplateSpellActivity handles negotiation with {@link TemplateSpellDao} to retrieve a single {@link TemplateSpell}.
 */
public class GetTemplateSpellActivity {
    private final TemplateSpellDao templateSpellDao;
    private final SpellDao spellDao;

    /**
     * Instantiates a new activity object.
     *
     * @param templateSpellDao DAO object necessary for this activity object to carry out its function.
     * @param spellDao DAO object necessary for this activity object to carry out its function.
     */

    @Inject
    public GetTemplateSpellActivity(TemplateSpellDao templateSpellDao, SpellDao spellDao) {
        this.templateSpellDao = templateSpellDao;
        this.spellDao = spellDao;
    };

    /**
     * This method handles the incoming request by retrieving a {@link TemplateSpell} from the database, if it exists.
     * <p>
     * It then returns the retrieved object, or throws a {@link TemplateSpellNotFoundException} if none is found.
     *
     * @param getTemplateSpellRequest request object containing DAO search parameters
     * @return {@link GetTemplateSpellResult} result object containing the retrieved {@link TemplateSpell}
     */

    public GetTemplateSpellResult handleRequest(GetTemplateSpellRequest getTemplateSpellRequest) {
        TemplateSpell templateSpell = Optional.ofNullable(templateSpellDao.getSingle(getTemplateSpellRequest.getSlug()))
                .orElseThrow(() -> new TemplateSpellNotFoundException(String.format("Could not find a templateSpell for [%s] with slug [%s]",
                        getTemplateSpellRequest.getUserEmail(), getTemplateSpellRequest.getSlug())));
        if (spellDao.objectNameExists(getTemplateSpellRequest.getUserEmail(), templateSpell.getName())) {
            templateSpell.setResourceExists(true);
        }
        return GetTemplateSpellResult.builder()
                .withTemplateSpell(templateSpell)
                .build();
    }
}
