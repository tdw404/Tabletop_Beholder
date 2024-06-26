package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreatureConverterTest {
    private CreatureConverter converter;
    private List<Creature> objectList;
    private Creature object1;
    private Creature object2;
    private String serial;

    @BeforeEach
    public void setup() {
        converter = new CreatureConverter();
        object1 = new Creature();
        object1.setObjectId("id1");
        object1.setUserEmail("email1");
        object2 = new Creature();
        object2.setObjectId("id2");
        object2.setUserEmail("email2");
        objectList = new ArrayList<>();
        objectList.add(object1);
        objectList.add(object2);
        serial = "[{\"objectId\":\"id1\",\"userEmail\":\"email1\"},{\"objectId\":\"id2\",\"userEmail\":\"email2\"}]";
    }

    @Test
    public void convert_listOfObjects_resultIsAString() {
        //GIVEN
        //WHEN
        String result = converter.convert(objectList);

        //THEN
        assertEquals(String.class, result.getClass(), "Expected result to be a string");
        assertTrue(result.contains(object1.getObjectId()) && result.contains(object2.getObjectId()), "Expected result to contain serialized elements");
    }

    @Test
    public void unconvert_serilalizedList_resultIsObjectType() {
        //GIVEN
        //WHEN
        List<Creature> result = converter.unconvert(serial);

        //THEN
        assertEquals(Creature.class, result.get(0).getClass(), "Expected result class to match original object");
        assertTrue(result.get(0).getObjectId().equals("id1") || result.get(0).getObjectId().equals("id2"), "Expected result to contain elements of serialized string");
    }

    @Test
    public void unconvert_badList_throwsError() {
        //GIVEN
        //WHEN
        //THEN
        assertThrows(SerializationException.class, () -> converter.unconvert("fail"));
    }

}
