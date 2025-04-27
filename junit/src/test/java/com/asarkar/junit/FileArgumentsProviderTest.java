package com.asarkar.junit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mockito;

class FileArgumentsProviderTest {
  private ExtensionContext ctx;

  @BeforeEach
  void init() throws ReflectiveOperationException {
    ctx = Mockito.mock(ExtensionContext.class);
    Class<?> testClass = getClass();
    var testMethod = testClass.getDeclaredMethod("mockTestMethod", String.class, String.class);
    Mockito.when(ctx.getRequiredTestMethod()).thenReturn(testMethod);
    Mockito.doReturn(testClass).when(ctx).getRequiredTestClass();
  }

  @Test
  void testBasic() {
    var fs = Basic.class.getAnnotation(FileSource.class);
    var actual = new FileArgumentsProviderImpl()
        .provideArguments(ctx, fs)
        .map(Arguments::get)
        .toList();
    assertThat(actual).containsExactly(new String[] {"1", "2"}, new String[] {"a", "b"});
  }

  @Test
  void testComment() {
    var fs = Comment.class.getAnnotation(FileSource.class);
    var actual = new FileArgumentsProviderImpl()
        .provideArguments(ctx, fs)
        .map(Arguments::get)
        .toList();
    assertThat(actual).containsExactly(new String[] {"1", "2"}, new String[] {"a", "b"});
  }

  @Test
  void testNullValues() {
    var fs = NullValues.class.getAnnotation(FileSource.class);
    var actual = new FileArgumentsProviderImpl()
        .provideArguments(ctx, fs)
        .map(Arguments::get)
        .toList();
    assertThat(actual).containsExactly(new String[] {"1", null}, new String[] {"a", null});
  }

  @Test
  void testIgnoreWhitespaces() {
    var fs = IgnoreWhitespaces.class.getAnnotation(FileSource.class);
    var actual = new FileArgumentsProviderImpl()
        .provideArguments(ctx, fs)
        .map(Arguments::get)
        .toList();
    assertThat(actual).containsExactly(new String[] {"1", "2"}, new String[] {"a", "b"});
  }

  @Test
  void testRetainWhitespaces() {
    var fs = RetainWhitespaces.class.getAnnotation(FileSource.class);
    var actual = new FileArgumentsProviderImpl()
        .provideArguments(ctx, fs)
        .map(Arguments::get)
        .toList();
    assertThat(actual).containsExactly(new String[] {"    1", "    2"}, new String[] {"a", " b"});
  }

  void mockTestMethod(String a, String b) {
    // PMD made me do it!
  }
}

class FileArgumentsProviderImpl extends FileArgumentsProvider {}

@FileSource("/basic.txt")
class Basic {}

@FileSource("/comment.txt")
class Comment {}

@FileSource(
    value = "/null.txt",
    nullValues = {"nil", "n/a"})
class NullValues {}

@FileSource("/whitespace.txt")
class IgnoreWhitespaces {}

@FileSource(value = "/whitespace.txt", ignoreLeadingAndTrailingWhitespace = false)
class RetainWhitespaces {}
