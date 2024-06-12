package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature;

import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.request.CreateTemplateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result.CreateTemplateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators.TemplateCreatureTranslator;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.resource.CreatureHelper;
import dev.tdwalsh.project.tabletopBeholder.resource.template.TemplateCreatureHelper;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateCreatureDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreateTemplateCreatureActivityTest {
    @InjectMocks
    private CreateTemplateCreatureActivity createTemplateCreatureActivity;
    @Mock
    private CreatureDao creatureDao;

    @Mock
    private TemplateCreatureDao templateCreatureDao;

    @Mock
    private TemplateCreatureTranslator templateCreatureTranslator;

    private CreateTemplateCreatureRequest createTemplateCreatureRequest;
    private String  email;
    private String slug;
    private TemplateCreature templateCreature;
    private Creature creature;


    @BeforeEach
    public void setup() {
        openMocks(this);
        templateCreature = TemplateCreatureHelper.provideTemplateCreature(1);
        creature = CreatureHelper.provideCreature(1);
        email = "testEmail";
        slug = "testslug";
        createTemplateCreatureRequest = CreateTemplateCreatureRequest.builder()
                .withUserEmail(email)
                .withSlug(slug)
                .build();
    }

    @Test
    public void handleRequest_nameUnique_returnsResult() {
        //GIVEN
        doReturn(templateCreature).when(templateCreatureDao).getSingle(slug);
        doReturn(creature).when(templateCreatureTranslator).translate(templateCreature, email);
        doReturn(false).when(creatureDao).objectNameExists(email, templateCreature.getName());

        //WHEN
        CreateTemplateCreatureResult result = createTemplateCreatureActivity.handleRequest(createTemplateCreatureRequest);

        //THEN
        verify(creatureDao, times(1)).writeObject(any(Creature.class));
        assertEquals(creature.getObjectName().toLowerCase(), result.getCreature().getObjectName().toLowerCase(), "Expected details of created object to match template");
    }

}