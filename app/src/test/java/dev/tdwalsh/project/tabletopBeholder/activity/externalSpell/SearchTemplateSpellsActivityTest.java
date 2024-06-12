package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.UpdateCreatureActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.UpdateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.UpdateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request.SearchTemplateSpellsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result.SearchTemplateSpellsResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.CreatureHelper;
import dev.tdwalsh.project.tabletopBeholder.resource.template.TemplateSpellHelper;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateSpellDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class SearchTemplateSpellsActivityTest {
    @InjectMocks
    private SearchTemplateSpellsActivity searchTemplateSpellsActivity;
    @Mock
    private SpellDao spellDao;

    @Mock
    private TemplateSpellDao templateSpellDao;

    private SearchTemplateSpellsRequest searchTemplateSpellsRequest;
    private List<TemplateSpell> templateSpellList;
    private String  email;
    private String searchTerms;
    private TemplateSpell templateSpell;


    @BeforeEach
    public void setup() {
        openMocks(this);
        templateSpell = TemplateSpellHelper.provideTemplateSpell(1);
        email = "testEmail";
        searchTerms = "testTerms";
        searchTemplateSpellsRequest = SearchTemplateSpellsRequest.builder()
                .withUserEmail(email)
                .withSearchTerms(searchTerms)
                .build();
        templateSpellList = new ArrayList<>();
        templateSpellList.add(templateSpell);
    }

    @Test
    public void handleRequest_templateNameExists_returnsResult() {
        //GIVEN
        doReturn(templateSpellList).when(templateSpellDao).getMultiple(searchTerms);
        doReturn(true).when(spellDao).objectNameExists(email, templateSpell.getName());
        //WHEN
        SearchTemplateSpellsResult result = searchTemplateSpellsActivity.handleRequest(searchTemplateSpellsRequest);

        //THEN
        assertEquals(templateSpell, result.getTemplateSpellList().get(0), "Expected output to contain template spell result");
        assertTrue(result.getTemplateSpellList().get(0).getResourceExists(), "Expected resource to be marked as existing");
    }

    @Test
    public void handleRequest_templateNameDoesNotExist_returnsResult() {
        //GIVEN
        doReturn(templateSpellList).when(templateSpellDao).getMultiple(searchTerms);
        doReturn(false).when(spellDao).objectNameExists(email, templateSpell.getName());
        //WHEN
        SearchTemplateSpellsResult result = searchTemplateSpellsActivity.handleRequest(searchTemplateSpellsRequest);

        //THEN
        assertEquals(templateSpell, result.getTemplateSpellList().get(0), "Expected output to contain template spell result");
        assertFalse(result.getTemplateSpellList().get(0).getResourceExists(), "Expected resource to be marked as not existing");
    }

}