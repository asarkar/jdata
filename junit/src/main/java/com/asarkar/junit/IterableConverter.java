package com.asarkar.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Logger;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

/**
 * Converts a JSON array string to the given iterable type.
 */
public class IterableConverter extends SimpleArgumentConverter {
  private static final ObjectMapper MAPPER = new ObjectMapper();
  private static final Logger LOGGER = Logger.getLogger(IterableConverter.class.getName());

  @Override
  protected Object convert(Object source, Class<?> targetType) {
    if (source instanceof String s) {
      try {
        return MAPPER.readValue(s, targetType);
      } catch (JsonProcessingException jpe) {
        throw new ArgumentConversionException(jpe.getMessage(), jpe);
      }
    } else {
      LOGGER.warning("Source is not a string; passing it through.");
      return source;
    }
  }
}
