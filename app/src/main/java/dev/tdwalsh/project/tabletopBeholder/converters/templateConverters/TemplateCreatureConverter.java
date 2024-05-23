package dev.tdwalsh.project.tabletopBeholder.converters.templateConverters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions.TemplateCreatureSerializationException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions.TemplateSpellSerializationException;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

public class TemplateCreatureConverter implements DynamoDBTypeConverter<String, TemplateCreature>{
    private JavaTimeModule javaTimeModule = new JavaTimeModule();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);
    @Override
    public String convert(TemplateCreature templateCreature) {
        try {
            return objectMapper.writeValueAsString(templateCreature);
        } catch (JsonProcessingException e) {
            throw new TemplateSpellSerializationException("Error serializing template", e);
        }
    }

    @Override
    public TemplateCreature unconvert(String templateString) {
        try {
            return objectMapper.readValue(templateString, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new TemplateCreatureSerializationException("Error deserializing template", e);
        }
    }
}