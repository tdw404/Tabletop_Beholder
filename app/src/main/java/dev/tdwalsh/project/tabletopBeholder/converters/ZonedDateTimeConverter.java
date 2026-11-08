package dev.tdwalsh.project.tabletopBeholder.converters;

import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeConverter implements StringAttributeConverter<ZonedDateTime> {
    @Override
    public EnhancedType<ZonedDateTime> type() {
        return EnhancedType.of(ZonedDateTime.class);
    }


    @Override
    public String convert(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public ZonedDateTime unconvert(String dateTimeString) {
        return ZonedDateTime.parse(dateTimeString);
    }
}
