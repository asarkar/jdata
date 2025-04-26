package com.asarkar.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

/**
 * Converts a JSON array string to the given iterable type.
 */
public class IterableConverter extends SimpleArgumentConverter {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected Object convert(Object source, Class<?> targetType) {
        if (source instanceof String s) {
            try {
                return MAPPER.readValue(s, targetType);
            } catch (JsonProcessingException jpe) {
                throw new ArgumentConversionException(jpe.getMessage(), jpe);
            }
        } else {
            throw new ArgumentConversionException("Can only convert from String");
        }
    }
}
