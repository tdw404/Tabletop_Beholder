package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;
import dev.tdwalsh.project.tabletopBeholder.resource.ActionHelper;
import dev.tdwalsh.project.tabletopBeholder.resource.SpellHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class ActionConverterTest {
    private ActionConverter converter;
    private Map<String, Action> objectMap;
    private Action object1;
    private Action object2;
    private String serial;


    @BeforeEach
    public void setup() {
        converter = new ActionConverter();
        object1 = ActionHelper.provideAction(1);
        object2 = ActionHelper.provideAction(2);
        objectMap = new HashMap<>();
        objectMap.put(object1.getObjectId(), object1);
        objectMap.put(object2.getObjectId(), object2);
        serial = "{\"objectId1\":{\"userEmail\":\"userEmail1\",\"objectId\":\"objectId1\",\"objectName\":\"Objectname1\",\"actionType\":\"actionType1\",\"actionDescription\":\"actionDesction1\",\"uses\":1,\"rechargeOn\":1,\"appliesEffects\":{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"effectName2\",\"turnDuration\":2,\"blameSource\":\"blameSource2\",\"blameCreatureId\":\"blameCreatureId2\",\"blameConcentration\":true,\"saveType\":\"saveType2\",\"saveDC\":2,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"effectName1\",\"turnDuration\":1,\"blameSource\":\"blameSource1\",\"blameCreatureId\":\"blameCreatureId1\",\"blameConcentration\":true,\"saveType\":\"saveType1\",\"saveDC\":1,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}},\"createDateTime\":1717436904.911999000,\"editDateTime\":1717436904.912024000},\"objectId2\":{\"userEmail\":\"userEmail2\",\"objectId\":\"objectId2\",\"objectName\":\"Objectname2\",\"actionType\":\"actionType2\",\"actionDescription\":\"actionDesction2\",\"uses\":2,\"rechargeOn\":2,\"appliesEffects\":{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"effectName2\",\"turnDuration\":2,\"blameSource\":\"blameSource2\",\"blameCreatureId\":\"blameCreatureId2\",\"blameConcentration\":true,\"saveType\":\"saveType2\",\"saveDC\":2,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"effectName1\",\"turnDuration\":1,\"blameSource\":\"blameSource1\",\"blameCreatureId\":\"blameCreatureId1\",\"blameConcentration\":true,\"saveType\":\"saveType1\",\"saveDC\":1,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}},\"createDateTime\":1717436904.912108000,\"editDateTime\":1717436904.912119000}}";
    }

    @Test
    public void convert_listOfObjects_resultIsAString() {
        //GIVEN
        //WHEN
        String result = converter.convert(objectMap);
        //THEN
        assertEquals(String.class, result.getClass(), "Expected result to be a string");
        assertTrue(result.contains(object1.getObjectId()) && result.contains(object2.getObjectId()), "Expected result to contain serialized elements");
    }

    @Test
    public void unconvert_serilalizedList_resultIsObjectType() {
        //GIVEN
        //WHEN
        Map<String, Action> result = converter.unconvert(serial);

        //THEN
        assertEquals(Action.class, new ArrayList<>(result.values()).get(0).getClass(), "Expected result class to match original object");
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
