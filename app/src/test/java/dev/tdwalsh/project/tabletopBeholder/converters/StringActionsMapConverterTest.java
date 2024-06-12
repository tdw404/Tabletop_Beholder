package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;
import dev.tdwalsh.project.tabletopBeholder.resource.ActionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StringActionsMapConverterTest {
    private StringActionsMapConverter converter;
    private Map<String, Action> objectMap;
    private Map<String, Map<String, Action>> superMap;
    private String serial;

    @BeforeEach
    public void setup() {
        converter = new StringActionsMapConverter();
        objectMap = new HashMap<>();
        objectMap.put("1", ActionHelper.provideAction(1));
        objectMap.put("2", ActionHelper.provideAction(2));
        superMap = new HashMap<>();
        superMap.put("1", objectMap);

        serial = "{\"1\":{\"1\":{\"userEmail\":\"userEmail1\",\"objectId\":\"objectId1\",\"objectName\":\"Objectname1\",\"actionType\":\"actionType1\",\"actionDescription\":\"actionDesction1\",\"uses\":1,\"rechargeOn\":1,\"appliesEffects\":{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"effectName2\",\"turnDuration\":2,\"blameSource\":\"blameSource2\",\"blameCreatureId\":\"blameCreatureId2\",\"blameConcentration\":true,\"saveType\":\"saveType2\",\"saveDC\":2,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"effectName1\",\"turnDuration\":1,\"blameSource\":\"blameSource1\",\"blameCreatureId\":\"blameCreatureId1\",\"blameConcentration\":true,\"saveType\":\"saveType1\",\"saveDC\":1,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}},\"createDateTime\":1718141024.429323000,\"editDateTime\":1718141024.430973000},\"2\":{\"userEmail\":\"userEmail2\",\"objectId\":\"objectId2\",\"objectName\":\"Objectname2\",\"actionType\":\"actionType2\",\"actionDescription\":\"actionDesction2\",\"uses\":2,\"rechargeOn\":2,\"appliesEffects\":{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"effectName2\",\"turnDuration\":2,\"blameSource\":\"blameSource2\",\"blameCreatureId\":\"blameCreatureId2\",\"blameConcentration\":true,\"saveType\":\"saveType2\",\"saveDC\":2,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"effectName1\",\"turnDuration\":1,\"blameSource\":\"blameSource1\",\"blameCreatureId\":\"blameCreatureId1\",\"blameConcentration\":true,\"saveType\":\"saveType1\",\"saveDC\":1,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}},\"createDateTime\":1718141024.431135000,\"editDateTime\":1718141024.431151000}}}";
    }

    @Test
    public void convert_mapOfObjects_resultIsAString() {
        //GIVEN
        //WHEN
        String result = converter.convert(superMap);

        //THEN
        assertEquals(String.class, result.getClass(), "Expected result to be a string");
        assertTrue(result.contains(superMap.get("1").get("1").getObjectId()), "Expected result to contain serialized elements");
    }

    @Test
    public void unconvert_serilalizedList_resultIsObjectType() {
        //GIVEN
        //WHEN
        Map<String, Map<String, Action>> result = converter.unconvert(serial);

        //THEN
        assertEquals(String.class, result.keySet().toArray()[0].getClass(), "Expected result class to match original object");
        assertEquals(LinkedHashMap.class, result.values().toArray()[0].getClass(), "Expected result class to match original object");
        assertEquals(superMap.get("1").get("1").getObjectId(), result.get("1").get("1").getObjectId(), "Expected result to contain elements of serialized string");
    }

    @Test
    public void unconvert_badList_throwsError() {
        //GIVEN
        //WHEN
        //THEN
        assertThrows(SerializationException.class, () -> converter.unconvert("fail"));
    }

}
