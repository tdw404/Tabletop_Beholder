package dev.tdwalsh.project.tabletopBeholder.converters.templateConverters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions.TemplateSpellSerializationException;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

import java.util.List;

public class TemplateSpellConverter implements DynamoDBTypeConverter<String, TemplateSpell>{
    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);
    @Override
    public String convert(TemplateSpell templateSpell) {
        try {
            return objectMapper.writeValueAsString(templateSpell);
        } catch (JsonProcessingException e) {
            throw new TemplateSpellSerializationException("Error serializing template", e);
        }
    }

    @Override
    public TemplateSpell unconvert(String templateString) {
        try {
            return objectMapper.readValue(templateString, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new TemplateSpellSerializationException("Error deserializing template", e);
        }
    }
}