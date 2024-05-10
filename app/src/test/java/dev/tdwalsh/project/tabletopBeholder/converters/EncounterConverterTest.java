package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.EffectSerializationException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.EncounterSerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EncounterConverterTest {
    private EncounterConverter converter;
    private List<Encounter> objectList;
    private Encounter object1;
    private Encounter object2;
    private String serial;

    @BeforeEach
    public void setup() {
        converter = new EncounterConverter();
        object1 = new Encounter();
        object1.setEncounterId("id1");
        object1.setUserEmail("email1");
        object2 = new Encounter();
        object2.setEncounterId("id2");
        object2.setUserEmail("email2");
        objectList = new ArrayList<>();
        objectList.add(object1);
        objectList.add(object2);
        serial = "[{\"encounterId\":\"id1\",\"userEmail\":\"email1\"},{\"encounterId\":\"id2\",\"userEmail\":\"email2\"}]";
    }

    @Test
    public void convert_listOfObjects_resultIsAString() {
        //GIVEN
        //WHEN
        String result = converter.convert(objectList);

        //THEN
        assertEquals(String.class, result.getClass(), "Expected result to be a string");
        assertTrue(result.contains(object1.getEncounterId()) && result.contains(object2.getEncounterId()), "Expected result to contain serialized elements");
    }

    @Test
    public void unconvert_serilalizedList_resultIsObjectType() {
        //GIVEN
        //WHEN
        List<Encounter> result = converter.unconvert(serial);

        //THEN
        assertEquals(Encounter.class, result.get(0).getClass(), "Expected result class to match original object");
        assertTrue(result.get(0).getEncounterId().equals("id1") || result.get(0).getEncounterId().equals("id2"), "Expected result to contain elements of serialized string");
    }

    @Test
    public void unconvert_badList_throwsError() {
        //GIVEN
        //WHEN
        //THEN
        assertThrows(EncounterSerializationException.class, () -> converter.unconvert("fail"));
    }

}
