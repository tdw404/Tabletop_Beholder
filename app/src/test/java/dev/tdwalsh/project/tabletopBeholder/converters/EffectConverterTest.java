package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EffectConverterTest {
    private EffectConverter converter;
    private List<Effect> objectList;
    private Effect object1;
    private Effect object2;
    private String serial;

    @BeforeEach
    public void setup() {
        converter = new EffectConverter();
        object1 = new Effect();
        object1.setEffectName("name1");
        object1.setBlameSource("source1");
        object2 = new Effect();
        object2.setEffectName("name2");
        object2.setBlameSource("source2");
        objectList = new ArrayList<>();
        objectList.add(object1);
        objectList.add(object2);
        serial = "[{\"effectName\":\"name1\",\"blameSource\":\"source1\"},{\"effectName\":\"name2\",\"blameSource\":\"source2\"}]";
    }

    @Test
    public void convert_listOfObjects_resultIsAString() {
        //GIVEN
        //WHEN
        String result = converter.convert(objectList);

        //THEN
        assertEquals(String.class, result.getClass(), "Expected result to be a string");
        assertTrue(result.contains(object1.getEffectName()) && result.contains(object2.getEffectName()), "Expected result to contain serialized elements");
    }

    @Test
    public void unconvert_serilalizedList_resultIsObjectType() {
        //GIVEN
        //WHEN
        List<Effect> result = converter.unconvert(serial);

        //THEN
        assertEquals(Effect.class, result.get(0).getClass(), "Expected result class to match original object");
        assertTrue(result.get(0).getEffectName().equals("name1") || result.get(0).getEffectName().equals("name2"), "Expected result to contain elements of serialized string");
    }

    @Test
    public void unconvert_badList_throwsError() {
        //GIVEN
        //WHEN
        //THEN
        assertThrows(SerializationException.class, () -> converter.unconvert("fail"));
    }

}
