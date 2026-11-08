package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

public class StringActionsMapConverter {
    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);
    public String convert(Map<String, Map<String, Action>> stringActionsMap) {
        try {
            return objectMapper.writeValueAsString(stringActionsMap);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing map", e);
        }
    }

    public Map<String, Map<String, Action>> unconvert(String serialString) {
        try {
            return objectMapper.readValue(serialString, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing map values", e);
        }
    }
}
