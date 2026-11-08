package dev.tdwalsh.project.tabletopBeholder.converters;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/** Preserves the string representation used by existing DynamoDB records. */
interface StringAttributeConverter<T> extends AttributeConverter<T> {
    String convert(T value);
    T unconvert(String value);

    @Override
    default AttributeValue transformFrom(T value) {
        return AttributeValue.fromS(convert(value));
    }

    @Override
    default T transformTo(AttributeValue value) {
        return unconvert(value.s());
    }

    @Override
    default AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
