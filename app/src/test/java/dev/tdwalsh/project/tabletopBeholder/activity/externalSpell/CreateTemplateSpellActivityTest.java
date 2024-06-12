package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell;

import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request.CreateTemplateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result.CreateTemplateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.resource.template.TemplateSpellHelper;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateSpellDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreateTemplateSpellActivityTest {
    @InjectMocks
    private CreateTemplateSpellActivity createTemplateSpellActivity;
    @Mock
    private SpellDao spellDao;

    @Mock
    private TemplateSpellDao templateSpellDao;

    private CreateTemplateSpellRequest createTemplateSpellRequest;
    private String  email;
    private String slug;
    private TemplateSpell templateSpell;
    private Spell spell;


    @BeforeEach
    public void setup() {
        openMocks(this);
        templateSpell = TemplateSpellHelper.provideTemplateSpell(1);

        email = "testEmail";
        slug = "testslug";
        createTemplateSpellRequest = CreateTemplateSpellRequest.builder()
                .withUserEmail(email)
                .withSlug(slug)
                .build();
    }

    @Test
    public void handleRequest_nameUnique_returnsResult() {
        //GIVEN
        doReturn(templateSpell).when(templateSpellDao).getSingle(slug);
        doReturn(false).when(spellDao).objectNameExists(email, templateSpell.getName());

        //WHEN
        CreateTemplateSpellResult result = createTemplateSpellActivity.handleRequest(createTemplateSpellRequest);

        //THEN
        verify(spellDao, times(1)).writeObject(any(Spell.class));
        assertEquals(templateSpell.getName().toLowerCase(), result.getSpell().getObjectName().toLowerCase(), "Expected details of created object to match template");
    }

}