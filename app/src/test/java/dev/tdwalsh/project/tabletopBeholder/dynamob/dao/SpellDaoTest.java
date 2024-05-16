package dev.tdwalsh.project.tabletopBeholder.dynamob.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.resource.SpellHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class SpellDaoTest {
    @InjectMocks
    private SpellDao dao;
    @Mock
    private DynamoDBMapper mapper;
    private String userEmail;
    private String objectId;
    private Spell spell;
    @Mock
    private PaginatedQueryList<Spell> paginatedQueryList;

    @BeforeEach
    public void setup() {
        openMocks(this);
        spell = SpellHelper.provideSpell(1);
        userEmail = spell.getUserEmail();
        objectId = spell.getObjectId();
    }

    @Test
    public void getSingleSpell_spellExists_returnsSpell() {
        //GIVEN
        doReturn(spell).when(mapper).load(Spell.class, userEmail, objectId);

        //WHEN
        Spell result = dao.getSingle(userEmail, objectId);

        //THEN
        assertEquals(spell, result, "Expected dao to return single result");
    }

    @Test
    public void getSingleSpell_spellDoesNotExist_returnsNull() {
        //GIVEN
        doReturn(null).when(mapper).load(Spell.class, userEmail, objectId);

        //WHEN
        Spell result = dao.getSingle(userEmail, objectId);

        //THEN
        assertEquals(null, result, "Expected dao to return null result");
    }

    @Test
    public void getSpellsByUser_userExists_returnsListofSpells() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression<Spell>> argumentCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        doReturn(paginatedQueryList).when(mapper).query(eq(Spell.class), any(DynamoDBQueryExpression.class));

        //WHEN
        List<Spell> result = (List<Spell>) dao.getMultiple(userEmail);
        verify(mapper).query(eq(Spell.class), argumentCaptor.capture());

        //THEN
        assertEquals(PaginatedQueryList.class, result.getClass(), "Expected dao to return list");
        assertEquals(userEmail, argumentCaptor.getValue().getHashKeyValues().getUserEmail());
    }

    @Test
    public void writeSpell_withSpell_callsDynamoDBSave() {
        //GIVEN
        ArgumentCaptor<Spell> argumentCaptor = ArgumentCaptor.forClass(Spell.class);

        //WHEN
        dao.writeObject(spell);

        //THEN
        verify(mapper, times(1)).save(argumentCaptor.capture());
        assertEquals(spell.getObjectId(), argumentCaptor.getValue().getObjectId());
        assertEquals(spell.getUserEmail(), argumentCaptor.getValue().getUserEmail());
    }

    @Test
    public void deleteSpell_withObjectIdAndUserEmail_callsDynamoDBDelete() {
        //GIVEN
        ArgumentCaptor<Spell> argumentCaptor = ArgumentCaptor.forClass(Spell.class);

        //WHEN
        dao.deleteObject(userEmail, objectId);

        //THEN
        verify(mapper, times(1)).delete(argumentCaptor.capture());
        assertEquals(spell.getObjectId(), argumentCaptor.getValue().getObjectId());
        assertEquals(spell.getUserEmail(), argumentCaptor.getValue().getUserEmail());
    }
}
