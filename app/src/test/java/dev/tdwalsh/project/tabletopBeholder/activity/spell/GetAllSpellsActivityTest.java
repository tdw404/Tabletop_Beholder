package dev.tdwalsh.project.tabletopBeholder.activity.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetAllSpellsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetAllSpellsResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.SpellNotFoundException;
import dev.tdwalsh.project.tabletopBeholder.resource.SpellHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetAllSpellsActivityTest {
    @InjectMocks
    private GetAllSpellsActivity getAllSpellsActivity;
    @Mock
    private SpellDao spellDao;
    private GetAllSpellsRequest getAllSpellsRequest;
    private Spell spellOne;
    private Spell spellTwo;
    private List<Spell> spellList;

    @BeforeEach
    public void setup() {
        openMocks(this);
        spellOne = SpellHelper.provideSpell(1);
        spellTwo = SpellHelper.provideSpell(2);
        spellList = new ArrayList<>();
        spellList.add(spellOne);
        spellList.add(spellTwo);
        getAllSpellsRequest = GetAllSpellsRequest.builder()
                .withUserEmail(spellOne.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_withEmail_returnsResult() {
        //GIVEN
        doReturn(spellList).when(spellDao).getMultiple(spellOne.getUserEmail());

        //WHEN
        GetAllSpellsResult result = getAllSpellsActivity.handleRequest(getAllSpellsRequest);

        //THEN
        assertEquals(spellList, result.getSpellList(), "Expected returned object to contain value sent from DAO");
    }
}