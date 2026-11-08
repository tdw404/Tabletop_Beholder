package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

public class EffectConverter implements StringAttributeConverter<Map<String, Effect>> {
    @Override
    public EnhancedType<Map<String, Effect>> type() {
        return new EnhancedType<Map<String, Effect>>() { };
    }

    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);
    @Override
    public String convert(Map<String, Effect> effectList) {
        try {
            return objectMapper.writeValueAsString(effectList);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing effect list", e);
        }
    }

    @Override
    public Map<String, Effect> unconvert(String effectString) {
        try {
            return objectMapper.readValue(effectString, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing effect list", e);
        }
    }
}
