package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class IntIntMapConverter implements StringAttributeConverter<Map<Integer, Integer>> {
    @Override
    public EnhancedType<Map<Integer, Integer>> type() {
        return new EnhancedType<Map<Integer, Integer>>() { };
    }

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convert(Map<Integer, Integer> integerIntegerMap) {
        try {
            return objectMapper.writeValueAsString(integerIntegerMap);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing map", e);
        }
    }

    @Override
    public Map<Integer, Integer> unconvert(String serialString) {
        try {
            return objectMapper.readValue(serialString, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing map values", e);
        }
    }
}
