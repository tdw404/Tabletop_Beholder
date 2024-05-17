package dev.tdwalsh.project.tabletopBeholder.activity.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetSpellResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.SpellNotFoundException;
import dev.tdwalsh.project.tabletopBeholder.resource.SpellHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetSpellActivityTest {
    @InjectMocks
    private GetSpellActivity getSpellActivity;
    @Mock
    private SpellDao spellDao;
    private GetSpellRequest getSpellRequest;
    private GetSpellResult getSpellResult;
    private Spell spell;
    private String userEmail;
    private String objectId;

    @BeforeEach
    public void setup() {
        openMocks(this);
        spell = SpellHelper.provideSpell(1);
        userEmail = spell.getUserEmail();
        objectId = spell.getObjectId();
        getSpellRequest = GetSpellRequest.builder()
                .withObjectId(objectId)
                .withUserEmail(userEmail)
                .build();
    }

    @Test
    public void handleRequest_objectsExistsOnDB_returnsResult() {
        //GIVEN
        doReturn(spell).when(spellDao).getSingle(userEmail, objectId);

        //WHEN
        GetSpellResult result = getSpellActivity.handleRequest(getSpellRequest);

        //THEN
        assertEquals(spell, result.getSpell(), "Expected returned object to contain value sent from DAO");
    }

    @Test
    public void handleRequest_objectsDoesNotExists_throwsError() {
        //GIVEN
        doReturn(null).when(spellDao).getSingle(userEmail, objectId);

        //WHEN
        //THEN
        assertThrows(SpellNotFoundException.class, () -> getSpellActivity.handleRequest(getSpellRequest));
    }
}