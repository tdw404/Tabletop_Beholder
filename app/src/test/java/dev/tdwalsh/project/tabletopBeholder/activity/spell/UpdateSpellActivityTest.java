package dev.tdwalsh.project.tabletopBeholder.activity.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.UpdateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.UpdateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.UpdateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.UpdateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.DuplicateResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.SpellHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdateSpellActivityTest {
    @InjectMocks
    private UpdateSpellActivity updateSpellActivity;
    @Mock
    private SpellDao spellDao;
    private UpdateSpellRequest updateSpellRequest;
    private Spell oldSpell;
    private Spell newSpell;

    @BeforeEach
    public void setup() {
        openMocks(this);
        oldSpell = SpellHelper.provideSpell(1);
        newSpell = SpellHelper.provideSpell(2);
        newSpell.setObjectId(oldSpell.getObjectId());
        newSpell.setUserEmail(oldSpell.getUserEmail());
        updateSpellRequest = UpdateSpellRequest.builder()
                .withSpell(newSpell)
                .withUserEmail(newSpell.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_objectNameSame_returnsResult() {
        //GIVEN
        doReturn(oldSpell).when(spellDao).getSingle(newSpell.getUserEmail(), newSpell.getObjectId());

        //WHEN
        UpdateSpellResult result = updateSpellActivity.handleRequest(updateSpellRequest);
        //THEN
        verify(spellDao).writeObject(newSpell);
        assertEquals(newSpell, result.getSpell(), "Expected returned object to contain new value");
    }

    @Test
    public void handleRequest_objectNameDifferent_returnsResult() {
        //GIVEN
        newSpell.setObjectName("different");
        doReturn(oldSpell).when(spellDao).getSingle(newSpell.getUserEmail(), newSpell.getObjectId());
        doReturn(false).when(spellDao).objectNameExists(newSpell.getUserEmail(), newSpell.getObjectName());
        //WHEN
        UpdateSpellResult result = updateSpellActivity.handleRequest(updateSpellRequest);
        //THEN
        verify(spellDao).writeObject(newSpell);
        assertEquals(newSpell, result.getSpell(), "Expected returned object to contain new value");
    }

    @Test
    public void handleRequest_objectNameNotUnique_throwsError() {
        //GIVEN
        newSpell.setObjectName("different");
        doReturn(oldSpell).when(spellDao).getSingle(newSpell.getUserEmail(), newSpell.getObjectId());
        doReturn(true).when(spellDao).objectNameExists(newSpell.getUserEmail(), newSpell.getObjectName());
        //WHEN
        //THEN
        assertThrows(DuplicateResourceException.class, () -> updateSpellActivity.handleRequest(updateSpellRequest));
    }

}