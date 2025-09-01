package com.asarkar.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.logging.Logger;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

/**
 * Converts a JSON array string to the given iterable type.
 */
public class IterableConverter extends SimpleArgumentConverter {
  private static final ObjectMapper MAPPER = JsonMapper.builder()
      .enable(
          StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION,
          StreamReadFeature.USE_FAST_BIG_NUMBER_PARSER,
          StreamReadFeature.USE_FAST_DOUBLE_PARSER)
      .build();
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
