package dev.tdwalsh.project.tabletopBeholder.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;

import java.util.List;
import java.util.Map;

public class SpellConverter implements DynamoDBTypeConverter<String, Map<String, Spell>>{
    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper =new ObjectMapper().registerModule(javaTimeModule);
    @Override
    public String convert(Map<String, Spell> spellList) {
        try {
            return objectMapper.writeValueAsString(spellList);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error spell encounter list", e);
        }
    }

    @Override
    public Map<String, Spell> unconvert(String spellString) {
        try {
            return objectMapper.readValue(spellString, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing spell list", e);
        }
    }
}