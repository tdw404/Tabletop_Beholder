package dev.tdwalsh.project.tabletopBeholder.activity.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.DeleteSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.resource.SpellHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class DeleteSpellActivityTest {
    @InjectMocks
    private DeleteSpellActivity deleteSpellActivity;
    @Mock
    private SpellDao spellDao;
    private DeleteSpellRequest deleteSpellRequest;
    private Spell spell;

    @BeforeEach
    public void setup() {
        openMocks(this);
        spell = SpellHelper.provideSpell(1);
        deleteSpellRequest = DeleteSpellRequest.builder()
                .withObjectId(spell.getObjectId())
                .withUserEmail(spell.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_validSpellProvided_callsDaoDeleteMethod() {
        //GIVEN
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        //WHEN
        deleteSpellActivity.handleRequest(deleteSpellRequest);

        //THEN
        verify(spellDao, times(1)).deleteObject(emailCaptor.capture(), idCaptor.capture());
        assertEquals(spell.getUserEmail(), emailCaptor.getValue(), "Expected activity class to make delete call with provided values");
        assertEquals(spell.getObjectId(), idCaptor.getValue(), "Expected activity class to make delete call with provided values");
    }
}