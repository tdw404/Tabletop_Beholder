package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

public class EncounterConverter {
    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);
    public String convert(List<Encounter> encounterList) {
        try {
            return objectMapper.writeValueAsString(encounterList);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing effect list", e);
        }
    }

    public List<Encounter> unconvert(String encounterString) {
        try {
            return objectMapper.readValue(encounterString, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing effect list", e);
        }
    }
}
