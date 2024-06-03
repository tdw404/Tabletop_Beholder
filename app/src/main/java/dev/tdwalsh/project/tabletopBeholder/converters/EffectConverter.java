package dev.tdwalsh.project.tabletopBeholder.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;


import java.util.List;
import java.util.Map;

public class EffectConverter implements DynamoDBTypeConverter<String, Map<String, Effect>>{
    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper =new ObjectMapper().registerModule(javaTimeModule);
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
            return objectMapper.readValue(effectString, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing effect list", e);
        }
    }
}