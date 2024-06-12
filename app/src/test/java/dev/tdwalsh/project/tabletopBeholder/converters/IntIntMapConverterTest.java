package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class IntIntMapConverterTest {
    private IntIntMapConverter converter;
    private Map<Integer,Integer> objectMap;
    private String serial;

    @BeforeEach
    public void setup() {
        converter = new IntIntMapConverter();
        objectMap = new HashMap<>();
        objectMap.put(1,1);
        objectMap.put(2,2);
        serial = "{\"1\":1,\"2\":2}";
    }

    @Test
    public void convert_mapOfObjects_resultIsAString() {
        //GIVEN
        //WHEN
        String result = converter.convert(objectMap);

        //THEN
        assertEquals(String.class, result.getClass(), "Expected result to be a string");
        assertTrue(result.equals(serial), "Expected result to contain serialized elements");
    }

    @Test
    public void unconvert_serilalizedList_resultIsObjectType() {
        //GIVEN
        //WHEN
        Map<Integer,Integer> result = converter.unconvert(serial);

        //THEN
        assertEquals(Integer.class, result.keySet().toArray()[0].getClass(), "Expected result class to match original object");
        assertEquals(Integer.class, result.values().toArray()[0].getClass(), "Expected result class to match original object");
        assertEquals(1, result.get(1), "Expected result to contain elements of serialized string");
    }

    @Test
    public void unconvert_badList_throwsError() {
        //GIVEN
        //WHEN
        //THEN
        assertThrows(SerializationException.class, () -> converter.unconvert("fail"));
    }

}
