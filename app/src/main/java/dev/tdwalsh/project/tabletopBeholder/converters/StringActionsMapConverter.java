package dev.tdwalsh.project.tabletopBeholder.converters;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class StringActionsMapConverter implements DynamoDBTypeConverter<String, Map<String, Map<String, Action>>> {
    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);
    @Override
    public String convert(Map<String, Map<String, Action>> stringActionsMap) {
        try {
            return objectMapper.writeValueAsString(stringActionsMap);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing map", e);
        }
    }

    @Override
    public Map<String, Map<String, Action>> unconvert(String serialString) {
        try {
            return objectMapper.readValue(serialString, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing map values", e);
        }
    }
}
