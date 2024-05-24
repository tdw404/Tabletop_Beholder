package dev.tdwalsh.project.tabletopBeholder.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import java.util.List;
import java.util.Map;

public class StringActionsMapConverter implements DynamoDBTypeConverter<String, Map<String, List<Action>>>{
    private ObjectMapper objectMapper =new ObjectMapper();
    @Override
    public String convert(Map<String, List<Action>> integerActionsMap) {
        try {
            return objectMapper.writeValueAsString(integerActionsMap);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing map", e);
        }
    }

    @Override
    public Map<String, List<Action>> unconvert(String serialString) {
        try {
            return objectMapper.readValue(serialString, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing map values", e);
        }
    }
}