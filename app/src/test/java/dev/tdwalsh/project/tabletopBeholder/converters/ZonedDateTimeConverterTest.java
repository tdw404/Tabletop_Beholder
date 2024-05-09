package dev.tdwalsh.project.tabletopBeholder.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ZonedDateTimeConverterTest {
    private ZonedDateTimeConverter converter;
    private ZonedDateTime object1;
    private String serial;

    @BeforeEach
    public void setup() {
        converter = new ZonedDateTimeConverter();
        object1 = ZonedDateTime.now();
        serial = "2024-05-09T17:51:41.903105-05:00[America/Chicago]";
    }

    @Test
    public void convert_zdt_resultIsAString() {
        //GIVEN
        //WHEN
        System.out.println(ZonedDateTime.now());
        String result = converter.convert(object1);

        //THEN
        assertEquals(String.class, result.getClass(), "Expected result to be a string");
    }

    @Test
    public void unconvert_serilalizedList_resultIsObjectType() {
        //GIVEN
        //WHEN
        ZonedDateTime result = converter.unconvert(serial);

        //THEN
        assertEquals(ZonedDateTime.class, result.getClass(), "Expected result class to match original object");
        assertEquals(2024,result.getYear(), "Expected result to contain elements of serialized string");
    }
}
