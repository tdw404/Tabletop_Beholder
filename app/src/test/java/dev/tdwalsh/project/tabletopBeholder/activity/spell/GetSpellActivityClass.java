package dev.tdwalsh.project.tabletopBeholder.activity.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.SpellNotFoundException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions.ActionSerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetSpellActivityClass {
    @InjectMocks
    private GetSpellActivity getSpellActivity;
    @Mock
    private SpellDao spellDao;
    private GetSpellRequest getSpellRequest;
    private GetSpellResult getSpellResult;
    private Spell spell;
    private String userEmail;
    private String spellId;

    @BeforeEach
    public void setup() {
        openMocks(this);

        userEmail = "testEmail";
        spellId = "testId";

        spell = new Spell();
        spell.setUserEmail(userEmail);
        spell.setObjectId(spellId);
        spell.setObjectName("testName");
        spell.setSpellDescription("testDescription");
        spell.setSpellHigherLevel("testSHL");
        spell.setSpellRange("testRange");
        spell.setSpellComponents("testComponents");
        spell.setSpellMaterial("testMaterial");
        spell.setRitualCast(true);
        spell.setCastingTime(1);
        spell.setSpellLevel(1);
        spell.setSpellSchool("testSchool");
        spell.setInnateCasts(1);
        spell.setAppliesEffects(new ArrayList<>());

        getSpellRequest = GetSpellRequest.builder()
                .withSpellId(spellId)
                .withUserEmail(userEmail)
                .build();
    }

    @Test
    public void handleRequest_objectsExistsOnDB_returnsResult() {
        //GIVEN
        doReturn(spell).when(spellDao).getSingle(userEmail, spellId);

        //WHEN
        GetSpellResult result = getSpellActivity.handleRequest(getSpellRequest);

        //THEN
        assertEquals(spell, result.getSpell(), "Expected returned object to contain value sent from DAO");
    }

    @Test
    public void handleRequest_objectsDoesNotExists_throwsError() {
        //GIVEN
        doReturn(null).when(spellDao).getSingle(userEmail, spellId);

        //WHEN
        //THEN
        assertThrows(SpellNotFoundException.class, () -> getSpellActivity.handleRequest(getSpellRequest));
    }
}