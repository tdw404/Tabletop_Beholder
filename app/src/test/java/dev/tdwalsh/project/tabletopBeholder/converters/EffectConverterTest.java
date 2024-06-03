package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EffectConverterTest {
    private EffectConverter converter;
    private Map<String, Effect> objectMap;
    private Effect object1;
    private Effect object2;
    private String serial;

    @BeforeEach
    public void setup() {
        converter = new EffectConverter();
        object1 = new Effect();
        object1.setObjectId("id1");
        object1.setEffectName("name1");
        object1.setBlameSource("source1");
        object2 = new Effect();
        object2.setObjectId("id2");
        object2.setEffectName("name2");
        object2.setBlameSource("source2");
        objectMap = new HashMap<>();
        objectMap.put(object1.getObjectId(), object1);
        objectMap.put(object2.getObjectId(), object2);
        serial = "{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"name2\",\"turnDuration\":null,\"blameSource\":\"source2\",\"blameCreatureId\":null,\"blameConcentration\":false,\"saveType\":null,\"saveDC\":null,\"saveOn\":null,\"endOn\":null,\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"name1\",\"turnDuration\":null,\"blameSource\":\"source1\",\"blameCreatureId\":null,\"blameConcentration\":false,\"saveType\":null,\"saveDC\":null,\"saveOn\":null,\"endOn\":null,\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}}";
    }

    @Test
    public void convert_listOfObjects_resultIsAString() {
        //GIVEN
        //WHEN
        String result = converter.convert(objectMap);
        System.out.println(result);

        //THEN
        assertEquals(String.class, result.getClass(), "Expected result to be a string");
        assertTrue(result.contains(object1.getEffectName()) && result.contains(object2.getEffectName()), "Expected result to contain serialized elements");
    }

    @Test
    public void unconvert_serilalizedList_resultIsObjectType() {
        //GIVEN
        //WHEN
        Map<String, Effect> result = converter.unconvert(serial);

        //THEN
        assertEquals(Effect.class, new ArrayList<>(result.values()).get(0).getClass(), "Expected result class to match original object");
        assertTrue(new ArrayList<>(result.values()).get(0).getEffectName().equals("name1") || new ArrayList<>(result.values()).get(0).getEffectName().equals("name2"), "Expected result to contain elements of serialized string");
    }

    @Test
    public void unconvert_badList_throwsError() {
        //GIVEN
        //WHEN
        //THEN
        assertThrows(SerializationException.class, () -> converter.unconvert("fail"));
    }

}
