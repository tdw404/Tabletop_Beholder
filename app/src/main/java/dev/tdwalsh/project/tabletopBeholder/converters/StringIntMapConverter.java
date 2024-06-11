package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class StringIntMapConverter implements DynamoDBTypeConverter<String, Map<String, Integer>> {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convert(Map<String, Integer> stringIntegerMap) {
        try {
            return objectMapper.writeValueAsString(stringIntegerMap);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing map", e);
        }
    }

    @Override
    public Map<String, Integer> unconvert(String serialString) {
        try {
            return objectMapper.readValue(serialString, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing map values", e);
        }
    }
}
