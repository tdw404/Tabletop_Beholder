package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature;

import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.request.GetTemplateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result.GetTemplateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.resource.template.TemplateCreatureHelper;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateCreatureDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetTemplateCreatureActivityTest {
    @InjectMocks
    private GetTemplateCreatureActivity getTemplateCreatureActivity;
    @Mock
    private CreatureDao creatureDao;

    @Mock
    private TemplateCreatureDao templateCreatureDao;

    private GetTemplateCreatureRequest getTemplateCreatureRequest;
    private String  email;
    private String slug;
    private TemplateCreature templateCreature;


    @BeforeEach
    public void setup() {
        openMocks(this);
        templateCreature = TemplateCreatureHelper.provideTemplateCreature(1);
        email = "testEmail";
        slug = "testslug";
        getTemplateCreatureRequest = GetTemplateCreatureRequest.builder()
                .withUserEmail(email)
                .withSlug(slug)
                .build();
    }

    @Test
    public void handleRequest_templateNameExists_returnsResult() {
        //GIVEN
        doReturn(templateCreature).when(templateCreatureDao).getSingle(slug);
        doReturn(true).when(creatureDao).objectNameExists(email, templateCreature.getName());
        //WHEN
        GetTemplateCreatureResult result = getTemplateCreatureActivity.handleRequest(getTemplateCreatureRequest);

        //THEN
        assertEquals(templateCreature, result.getTemplateCreature(), "Expected output to contain template creature result");
        assertTrue(result.getTemplateCreature().getResourceExists(), "Expected resource to be marked as existing");
    }

    @Test
    public void handleRequest_templateNameDoesNotExist_returnsResult() {
        //GIVEN
        doReturn(templateCreature).when(templateCreatureDao).getSingle(slug);
        doReturn(false).when(creatureDao).objectNameExists(email, templateCreature.getName());
        //WHEN
        GetTemplateCreatureResult result = getTemplateCreatureActivity.handleRequest(getTemplateCreatureRequest);

        //THEN
        assertEquals(templateCreature, result.getTemplateCreature(), "Expected output to contain template creature result");
        assertFalse(result.getTemplateCreature().getResourceExists(), "Expected resource to be marked as existing");
    }

}