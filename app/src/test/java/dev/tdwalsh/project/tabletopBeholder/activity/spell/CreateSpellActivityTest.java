package dev.tdwalsh.project.tabletopBeholder.activity.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.CreateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.CreateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.SpellNotFoundException;
import dev.tdwalsh.project.tabletopBeholder.resource.SpellHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreateSpellActivityTest {
    @InjectMocks
    private CreateSpellActivity createSpellActivity;
    @Mock
    private SpellDao spellDao;
    private CreateSpellRequest createSpellRequest;
    private CreateSpellResult createSpellResult;
    private Spell spell;


    @BeforeEach
    public void setup() {
        openMocks(this);
        spell = SpellHelper.provideSpell(1);
        createSpellRequest = CreateSpellRequest.builder()
                .withSpell(spell)
                .withUserEmail(spell.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_objectNameUnique_returnsResult() {
        //GIVEN
        String orgSpellId = spell.getObjectId();
        //WHEN
        CreateSpellResult result = createSpellActivity.handleRequest(createSpellRequest);

        //THEN
        assertEquals(spell, result.getSpell(), "Expected returned object to contain value sent from DAO");
        assertNotEquals(orgSpellId, result.getSpell().getObjectId(), "Expected spell to have been given a new ID");
    }

    @Test
    public void handleRequest_objectNameNotUnique_throwsError() {
        //GIVEN
        doReturn(true).when(spellDao).objectNameExists(anyString(), anyString());
        //WHEN
        //THEN
        assertThrows(DuplicateResourceException.class, () -> createSpellActivity.handleRequest(createSpellRequest));
    }
}