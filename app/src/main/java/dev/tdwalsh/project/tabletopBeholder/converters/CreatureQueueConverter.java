package dev.tdwalsh.project.tabletopBeholder.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import java.util.List;
import java.util.Queue;

public class CreatureQueueConverter implements DynamoDBTypeConverter<String, Queue<String>> {
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
