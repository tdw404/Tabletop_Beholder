package dev.tdwalsh.project.tabletopBeholder.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions.SpellSerializationException;

import java.util.List;

public class SpellConverter implements DynamoDBTypeConverter<String, List<Spell>>{
    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper =new ObjectMapper().registerModule(javaTimeModule);
    @Override
    public String convert(List<Spell> spellList) {
        try {
            return objectMapper.writeValueAsString(spellList);
        } catch (JsonProcessingException e) {
            throw new SpellSerializationException("Error spell encounter list", e);
        }
    }

    @Override
    public List<Spell> unconvert(String spellString) {
        try {
            return objectMapper.readValue(spellString, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new SpellSerializationException("Error deserializing spell list", e);
        }
    }
}