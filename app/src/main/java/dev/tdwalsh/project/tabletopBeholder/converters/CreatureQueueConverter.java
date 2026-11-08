package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Queue;

public class CreatureQueueConverter implements StringAttributeConverter<Queue<String>> {
    @Override
    public EnhancedType<Queue<String>> type() {
        return new EnhancedType<Queue<String>>() { };
    }

    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);
    @Override
    public String convert(Queue<String> creatureQueue) {
        try {
            return objectMapper.writeValueAsString(creatureQueue);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing effect list", e);
        }
    }

    @Override
    public Queue<String> unconvert(String creatureString) {
        try {
            return objectMapper.readValue(creatureString, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing effect list", e);
        }
    }
}
