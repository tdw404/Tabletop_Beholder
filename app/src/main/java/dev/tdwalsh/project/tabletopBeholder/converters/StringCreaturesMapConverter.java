package dev.tdwalsh.project.tabletopBeholder.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import java.util.Map;

public class StringCreaturesMapConverter implements DynamoDBTypeConverter<String, Map<String, Map<String, Creature>>>{
    private ObjectMapper objectMapper =new ObjectMapper();
    @Override
    public String convert(Map<String, Map<String, Creature>> stringCreaturesMap) {
        try {
            return objectMapper.writeValueAsString(stringCreaturesMap);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing map", e);
        }
    }

    @Override
    public Map<String, Map<String, Creature>> unconvert(String serialString) {
        try {
            return objectMapper.readValue(serialString, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing map values", e);
        }
    }
}