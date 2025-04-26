package com.asarkar.junit;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.AnnotationBasedArgumentsProvider;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.platform.commons.JUnitException;
import org.junit.platform.commons.PreconditionViolationException;
import org.junit.platform.commons.util.Preconditions;

/**
 * Reads a file n lines at a time such that the {@link Arguments} for a test
 * execution matches the number of formal parameters in the test method.
 *
 * @see <a href="https://github.com/junit-team/junit5/blob/main/junit-jupiter-params/src/main/java/org/junit/jupiter/params/provider/CsvFileArgumentsProvider.java">CsvFileArgumentsProvider</a>
 */
public class FileArgumentsProvider extends AnnotationBasedArgumentsProvider<FileSource> {

    @Override
    protected Stream<? extends Arguments> provideArguments(ExtensionContext context, FileSource fileSource) {
        int numLines = context.getRequiredTestMethod().getParameterCount();
        return testResources(context.getRequiredTestClass(), fileSource.value())
                .flatMap(file -> toStream(file, numLines, fileSource));
    }

    @SuppressWarnings("PMD.UseVarargs")
    private Stream<Path> testResources(Class<?> testClass, String[] resources) {
        Preconditions.notEmpty(resources, "resources must not be empty");
        return Arrays.stream(resources).map(path -> {
            Preconditions.notBlank(path, () -> "Individual resources must not be null or blank");
            URL resource = testClass.getResource(path);
            if (resource == null) {
                throw new JUnitException("Could not resolve path: " + path);
            }
            try {
                return Paths.get(resource.toURI());
            } catch (URISyntaxException e) {
                throw new PreconditionViolationException(e.getMessage(), e);
            }
        });
    }

    // PMD doesn't know BufferedReader is closed when the stream is consumed.
    @SuppressWarnings("PMD.CloseResource")
    private Stream<Arguments> toStream(Path file, int numLines, FileSource fileSource) {
        try {
            BufferedReader reader = Files.newBufferedReader(file, Charset.forName(fileSource.encoding()));
            FileReader fileReader = new FileReader(reader, numLines, fileSource);
            FileIterator iter = new FileIterator(fileReader);
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iter, Spliterator.ORDERED), false)
                    .onClose(() -> {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            throw new JUnitException(e.getMessage(), e);
                        }
                    });
        } catch (IOException e) {
            throw new JUnitException(e.getMessage(), e);
        }
    }

    /**
     * Reads n lines at a time such that the {@link Arguments} returned by the {@link #next()}
     * method matches the number of formal parameters in the test method.
     */
    private static class FileIterator implements Iterator<Arguments> {
        private final FileReader reader;
        private Arguments nextArguments;

        FileIterator(FileReader reader) {
            this.reader = reader;
            advance();
        }

        @Override
        public boolean hasNext() {
            return this.nextArguments != null;
        }

        @Override
        public Arguments next() {
            Arguments result = this.nextArguments;
            advance();
            return result;
        }

        @SuppressWarnings("PMD.NullAssignment")
        private void advance() {
            String[] lines = reader.readLines();
            this.nextArguments = lines == null ? null : Arguments.of((Object[]) lines);
        }
    }

    /**
     * Reads n lines at a time, where n is the number of formal parameters of the test method.
     */
    private static final class FileReader {
        private final BufferedReader reader;
        private final int numLines;
        private final FileSource fileSource;
        private final Set<String> nullValues;

        private FileReader(BufferedReader reader, int numLines, FileSource fileSource) {
            this.reader = reader;
            this.numLines = numLines;
            this.fileSource = fileSource;
            nullValues = Set.of(fileSource.nullValues());
        }

        String[] readLines() {
            try {
                String[] lines = new String[numLines];
                String line;
                int i = 0;
                while (i < numLines) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (fileSource.ignoreLeadingAndTrailingWhitespace()) {
                        line = line.trim();
                    }
                    if (isComment(line)) {
                        continue;
                    }
                    if (!isNullValue(line)) {
                        lines[i] = line;
                    }
                    i++;
                }
                return i == numLines ? lines : null;
            } catch (IOException e) {
                throw new JUnitException(e.getMessage(), e);
            }
        }

        private boolean isComment(String line) {
            return !line.isEmpty() && line.charAt(0) == fileSource.comment();
        }

        private boolean isNullValue(String line) {
            return nullValues.contains(line);
        }
    }
}
