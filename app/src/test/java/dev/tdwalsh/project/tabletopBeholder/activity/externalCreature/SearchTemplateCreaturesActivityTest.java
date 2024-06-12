package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature;

import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.SearchTemplateCreaturesActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.request.SearchTemplateCreaturesRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result.SearchTemplateCreaturesResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.resource.template.TemplateCreatureHelper;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateCreatureDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class SearchTemplateCreaturesActivityTest {
    @InjectMocks
    private SearchTemplateCreaturesActivity searchTemplateCreaturesActivity;
    @Mock
    private CreatureDao creatureDao;

    @Mock
    private TemplateCreatureDao templateCreatureDao;

    private SearchTemplateCreaturesRequest searchTemplateCreaturesRequest;
    private List<TemplateCreature> templateCreatureList;
    private String  email;
    private String searchTerms;
    private TemplateCreature templateCreature;


    @BeforeEach
    public void setup() {
        openMocks(this);
        templateCreature = TemplateCreatureHelper.provideTemplateCreature(1);
        email = "testEmail";
        searchTerms = "testTerms";
        searchTemplateCreaturesRequest = SearchTemplateCreaturesRequest.builder()
                .withUserEmail(email)
                .withSearchTerms(searchTerms)
                .build();
        templateCreatureList = new ArrayList<>();
        templateCreatureList.add(templateCreature);
    }

    @Test
    public void handleRequest_templateNameExists_returnsResult() {
        //GIVEN
        doReturn(templateCreatureList).when(templateCreatureDao).getMultiple(searchTerms);
        doReturn(true).when(creatureDao).objectNameExists(email, templateCreature.getName());
        //WHEN
        SearchTemplateCreaturesResult result = searchTemplateCreaturesActivity.handleRequest(searchTemplateCreaturesRequest);

        //THEN
        assertEquals(templateCreature, result.getTemplateCreatureList().get(0), "Expected output to contain template creature result");
        assertTrue(result.getTemplateCreatureList().get(0).getResourceExists(), "Expected resource to be marked as existing");
    }

    @Test
    public void handleRequest_templateNameDoesNotExist_returnsResult() {
        //GIVEN
        doReturn(templateCreatureList).when(templateCreatureDao).getMultiple(searchTerms);
        doReturn(false).when(creatureDao).objectNameExists(email, templateCreature.getName());
        //WHEN
        SearchTemplateCreaturesResult result = searchTemplateCreaturesActivity.handleRequest(searchTemplateCreaturesRequest);

        //THEN
        assertEquals(templateCreature, result.getTemplateCreatureList().get(0), "Expected output to contain template creature result");
        assertFalse(result.getTemplateCreatureList().get(0).getResourceExists(), "Expected resource to be marked as not existing");
    }

}