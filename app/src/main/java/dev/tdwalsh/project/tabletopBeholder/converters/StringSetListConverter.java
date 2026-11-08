package dev.tdwalsh.project.tabletopBeholder.converters;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/** Keeps the existing string-set storage for Encounter.turnOrder. */
public class StringSetListConverter implements AttributeConverter<List<String>> {
    @Override
    public AttributeValue transformFrom(List<String> value) {
        return value.isEmpty() ? AttributeValue.fromNul(true) : AttributeValue.fromSs(new ArrayList<>(new LinkedHashSet<>(value)));
    }

    @Override
    public List<String> transformTo(AttributeValue value) {
        return new ArrayList<>(value.ss());
    }

    @Override
    public EnhancedType<List<String>> type() {
        return EnhancedType.listOf(String.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.SS;
    }
}
