package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell;

import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request.GetTemplateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result.GetTemplateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.resource.template.TemplateSpellHelper;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateSpellDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetTemplateSpellActivityTest {
    @InjectMocks
    private GetTemplateSpellActivity getTemplateSpellActivity;
    @Mock
    private SpellDao spellDao;

    @Mock
    private TemplateSpellDao templateSpellDao;

    private GetTemplateSpellRequest getTemplateSpellRequest;
    private String  email;
    private String slug;
    private TemplateSpell templateSpell;


    @BeforeEach
    public void setup() {
        openMocks(this);
        templateSpell = TemplateSpellHelper.provideTemplateSpell(1);
        email = "testEmail";
        slug = "testslug";
        getTemplateSpellRequest = GetTemplateSpellRequest.builder()
                .withUserEmail(email)
                .withSlug(slug)
                .build();
    }

    @Test
    public void handleRequest_templateNameExists_returnsResult() {
        //GIVEN
        doReturn(templateSpell).when(templateSpellDao).getSingle(slug);
        doReturn(true).when(spellDao).objectNameExists(email, templateSpell.getName());
        //WHEN
        GetTemplateSpellResult result = getTemplateSpellActivity.handleRequest(getTemplateSpellRequest);

        //THEN
        assertEquals(templateSpell, result.getTemplateSpell(), "Expected output to contain template spell result");
        assertTrue(result.getTemplateSpell().getResourceExists(), "Expected resource to be marked as existing");
    }

    @Test
    public void handleRequest_templateNameDoesNotExist_returnsResult() {
        //GIVEN
        doReturn(templateSpell).when(templateSpellDao).getSingle(slug);
        doReturn(false).when(spellDao).objectNameExists(email, templateSpell.getName());
        //WHEN
        GetTemplateSpellResult result = getTemplateSpellActivity.handleRequest(getTemplateSpellRequest);

        //THEN
        assertEquals(templateSpell, result.getTemplateSpell(), "Expected output to contain template spell result");
        assertFalse(result.getTemplateSpell().getResourceExists(), "Expected resource to be marked as existing");
    }

}