package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.ActionSerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ActionConverterTest {
    private ActionConverter converter;
    private List<Action> objectList;
    private Action object1;
    private Action object2;
    private String serial;

    @BeforeEach
    public void setup() {
        converter = new ActionConverter();
        object1 = new Action();
        object1.setActionId("id1");
        object1.setUserEmail("email1");
        object2 = new Action();
        object2.setActionId("id2");
        object2.setUserEmail("email2");
        objectList = new ArrayList<>();
        objectList.add(object1);
        objectList.add(object2);
        serial = "[{\"actionId\":\"id1\",\"userEmail\":\"email1\"},{\"actionId\":\"id2\",\"userEmail\":\"email2\"}]";
    }

    @Test
    public void convert_listOfObjects_resultIsAString() {
        //GIVEN
        //WHEN
        String result = converter.convert(objectList);

        //THEN
        assertEquals(String.class, result.getClass(), "Expected result to be a string");
        assertTrue(result.contains(object1.getActionId()) && result.contains(object2.getActionId()), "Expected result to contain serialized elements");
    }

    @Test
    public void unconvert_serilalizedList_resultIsObjectType() {
        //GIVEN
        //WHEN
        List<Action> result = converter.unconvert(serial);

        //THEN
        assertEquals(Action.class, result.get(0).getClass(), "Expected result class to match original object");
        assertTrue(result.get(0).getActionId().equals("id1") || result.get(0).getActionId().equals("id2"), "Expected result to contain elements of serialized string");
    }

    @Test
    public void unconvert_badList_throwsError() {
        //GIVEN
        //WHEN
        //THEN
        assertThrows(ActionSerializationException.class, () -> converter.unconvert("fail"));
    }

}
