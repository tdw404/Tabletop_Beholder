package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

public class ActionConverter implements StringAttributeConverter<Map<String, Action>> {
    @Override
    public EnhancedType<Map<String, Action>> type() {
        return new EnhancedType<Map<String, Action>>() { };
    }

    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);
    @Override
    public String convert(Map<String, Action> actionList) {
        try {
            return objectMapper.writeValueAsString(actionList);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing action list", e);
        }
    }

    @Override
    public Map<String, Action> unconvert(String actionString) {
        try {
            return objectMapper.readValue(actionString, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing action list", e);
        }
    }
}
