package dev.tdwalsh.project.tabletopBeholder.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.EncounterSerializationException;

import java.util.List;

public class EncounterConverter implements DynamoDBTypeConverter<String, List<Encounter>>{
    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper =new ObjectMapper().registerModule(javaTimeModule);
    @Override
    public String convert(List<Encounter> encounterList) {
        try {
            return objectMapper.writeValueAsString(encounterList);
        } catch (JsonProcessingException e) {
            throw new EncounterSerializationException("Error serializing effect list", e);
        }
    }

    @Override
    public List<Encounter> unconvert(String encounterString) {
        try {
            return objectMapper.readValue(encounterString, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new EncounterSerializationException("Error deserializing effect list", e);
        }
    }
}