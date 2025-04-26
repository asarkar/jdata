package com.asarkar.junit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * {@code @FileSource} is a {@linkplain Repeatable repeatable}
 * {@link ArgumentsSource} which is used to load
 * files from one or more classpath {@link #value}.
 *
 * <p>Each line in the file will correspond to a positional test method argument.
 * If the test method has n parameters, the first n lines (ignoring comments)
 * will make up the n arguments for the first execution, the next n lines will
 * be given to the second execution, and so on.
 * Incomplete batches that fall short of n arguments will be ignored.
 *
 * <p>The records parsed from these resources and files will be provided as
 * arguments to the annotated
 * {@link org.junit.jupiter.params.ParameterizedTest @ParameterizedTest}.
 *
 * @see org.junit.jupiter.params.provider.ArgumentsSource
 * @see org.junit.jupiter.params.ParameterizedTest
 * @see <a href="https://github.com/junit-team/junit5/blob/main/junit-jupiter-params/src/main/java/org/junit/jupiter/params/provider/CsvFileSource.java">CsvFileSource</a>
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(FileSources.class)
@ArgumentsSource(FileArgumentsProvider.class)
public @interface FileSource {

  /**
   * The classpath resources to use as the sources of arguments; must not
   * be empty.
   */
  String[] value() default {};

  /**
   * The encoding to use when reading the CSV files; must be a valid charset.
   *
   * <p>Defaults to {@code "UTF-8"}.
   *
   * @see java.nio.charset.StandardCharsets
   */
  String encoding() default "UTF-8";

  /**
   * Any line beginning with this character will be interpreted as a comment
   * and will be ignored.
   *
   * <p>Defaults to {@code '#'}.
   */
  char comment() default '#';

  /**
   * A list of strings that should be interpreted as {@code null} references.
   *
   * <p>For example, you may wish for certain values such as {@code "N/A"} or
   * {@code "NIL"} to be converted to {@code null} references.
   */
  String[] nullValues() default {};

  /**
   * Controls whether leading and trailing whitespace characters of unquoted
   * CSV columns should be ignored.
   *
   * <p>Defaults to {@code true}.
   */
  boolean ignoreLeadingAndTrailingWhitespace() default true;
}
