package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

public class StringCreaturesMapConverter implements DynamoDBTypeConverter<String, Map<String, Creature>> {
    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);
    @Override
    public String convert(Map<String, Creature> stringCreaturesMap) {
        try {
            return objectMapper.writeValueAsString(stringCreaturesMap);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing map", e);
        }
    }

    @Override
    public Map<String, Creature> unconvert(String serialString) {
        try {
            return objectMapper.readValue(serialString, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing map values", e);
        }
    }
}
