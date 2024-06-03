package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;
import dev.tdwalsh.project.tabletopBeholder.resource.SpellHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SpellConverterTest {
    private SpellConverter converter;
    private Map<String, Spell> objectMap;
    private Spell object1;
    private Spell object2;
    private String serial;

    @BeforeEach
    public void setup() {
        converter = new SpellConverter();
        object1 = SpellHelper.provideSpell(1);
        object2 = SpellHelper.provideSpell(2);
        objectMap = new HashMap<>();
        objectMap.put(object1.getObjectId(), object1);
        objectMap.put(object2.getObjectId(), object2);
        serial = "{\"objectId1\":{\"userEmail\":\"userEmail1\",\"objectId\":\"objectId1\",\"objectName\":\"Objectname1\",\"spellDescription\":\"spellDescription1\",\"spellHigherLevel\":\"spellHigherLevel1\",\"spellRange\":\"spellRange1\",\"spellComponents\":\"spellComponents1\",\"spellMaterial\":\"spellMaterial1\",\"reaction\":\"yes\",\"ritualCast\":true,\"castingTime\":\"castingTime1\",\"castingTurns\":1,\"spellLevel\":1,\"spellSchool\":\"spellSchool1\",\"innateCasts\":1,\"appliesEffects\":{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"effectName2\",\"turnDuration\":2,\"blameSource\":\"blameSource2\",\"blameCreatureId\":\"blameCreatureId2\",\"blameConcentration\":true,\"saveType\":\"saveType2\",\"saveDC\":2,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"effectName1\",\"turnDuration\":1,\"blameSource\":\"blameSource1\",\"blameCreatureId\":\"blameCreatureId1\",\"blameConcentration\":true,\"saveType\":\"saveType1\",\"saveDC\":1,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}},\"createDateTime\":1717439016.007732000,\"editDateTime\":1717439016.007758000},\"objectId2\":{\"userEmail\":\"userEmail2\",\"objectId\":\"objectId2\",\"objectName\":\"Objectname2\",\"spellDescription\":\"spellDescription2\",\"spellHigherLevel\":\"spellHigherLevel2\",\"spellRange\":\"spellRange2\",\"spellComponents\":\"spellComponents2\",\"spellMaterial\":\"spellMaterial2\",\"reaction\":\"yes\",\"ritualCast\":true,\"castingTime\":\"castingTime2\",\"castingTurns\":2,\"spellLevel\":2,\"spellSchool\":\"spellSchool2\",\"innateCasts\":2,\"appliesEffects\":{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"effectName2\",\"turnDuration\":2,\"blameSource\":\"blameSource2\",\"blameCreatureId\":\"blameCreatureId2\",\"blameConcentration\":true,\"saveType\":\"saveType2\",\"saveDC\":2,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"effectName1\",\"turnDuration\":1,\"blameSource\":\"blameSource1\",\"blameCreatureId\":\"blameCreatureId1\",\"blameConcentration\":true,\"saveType\":\"saveType1\",\"saveDC\":1,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}},\"createDateTime\":1717439016.008401000,\"editDateTime\":1717439016.008417000}}";
    }

    @Test
    public void convert_listOfObjects_resultIsAString() {
        //GIVEN
        //WHEN
        String result = converter.convert(objectMap);
        System.out.println(result);
        //THEN
        assertEquals(String.class, result.getClass(), "Expected result to be a string");
        assertTrue(result.contains(object1.getObjectId()) && result.contains(object2.getObjectId()), "Expected result to contain serialized elements");
    }

    @Test
    public void unconvert_serilalizedList_resultIsObjectType() {
        //GIVEN
        //WHEN
        Map<String, Spell> result = converter.unconvert(serial);

        //THEN
        assertEquals(Spell.class, new ArrayList<>(result.values()).get(0).getClass(), "Expected result class to match original object");
        assertTrue(new ArrayList<>(result.values()).get(0).getObjectId().equals("objectId1") || new ArrayList<>(result.values()).get(0).getObjectId().equals("objectId2"), "Expected result to contain elements of serialized string");
    }

    @Test
    public void unconvert_badList_throwsError() {
        //GIVEN
        //WHEN
        //THEN
        assertThrows(SerializationException.class, () -> converter.unconvert("fail"));
    }

}
