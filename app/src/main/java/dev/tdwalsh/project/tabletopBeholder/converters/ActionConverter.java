package dev.tdwalsh.project.tabletopBeholder.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions.ActionSerializationException;

import java.util.List;

public class ActionConverter implements DynamoDBTypeConverter<String, List<Action>>{
    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper =new ObjectMapper().registerModule(javaTimeModule);
    @Override
    public String convert(List<Action> actionList) {
        try {
            return objectMapper.writeValueAsString(actionList);
        } catch (JsonProcessingException e) {
            throw new ActionSerializationException("Error serializing action list", e);
        }
    }

    @Override
    public List<Action> unconvert(String actionString) {
        try {
            return objectMapper.readValue(actionString, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new ActionSerializationException("Error deserializing action list", e);
        }
    }
}