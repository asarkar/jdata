# Java Data Structures
Java package containing simple data structures and JUnit 5 utilities used in coding questions 
found on online platform such as LeetCode. This package can be used while working on such
questions locally.

Meant to be used in personal projects only, and makes absolutely no guarantees.

[![](https://github.com/asarkar/jdata/workflows/CI/badge.svg)](https://github.com/asarkar/jdata/actions)

## Development

1. Add the JitPack Maven repository to your Maven or Gradle build:
   https://jitpack.io

2. Dependency group id = `com.asarkar.jdata`. There are two artifacts under this group, 
   `jdata` and `junit`, choose one or both according to your need.

3. For version, use a 10-character commit hash. For release version, use release tag.

## Usage

### jdata

* [ListNode](jdata/src/main/java/com/asarkar/data/ListNode.java)

```
List<Integer> nums = List.of(1, 2, 3);
ListNode<Integer> head = ListNode.fromIterable(nums);
if (nums.isEmpty()) {
    assertThat(head).isNull();
} else {
    assertThat(head).isNotNull();
    List<Integer> actual = ListNode.toList(head);
    assertThat(actual).isEqualTo(nums);
}
```
* [TreeNode](jdata/src/main/java/com/asarkar/data/TreeNode.java)

```
List<Integer> nums = List.of(1, 2, 3);
TreeNode<Integer> root = TreeNode.fromList(nums);
if (nums.isEmpty()) {
    assertThat(root).isNull();
} else {
    assertThat(root).isNotNull();
    List<Integer> actual = TreeNode.levelOrder(root);
    assertThat(actual).isEqualTo(nums);
}

TreeNode.prettyPrint(root);
  1
 / \
2   3
```

### junit

* [IterableConverter](junit/src/main/java/com/asarkar/junit/IterableConverter.java)

```
class MyTests {
  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      textBlock =
          """
    [3, 2, 3] | 3
      """)
  public void testSomething(@ConvertWith(IterableConverter.class) int[] nums, int expected) {
    assertThat(new ClassUnderTest().method(nums)).isEqualTo(expected);
  }
}
```

* [FileSource](junit/src/main/java/com/asarkar/junit/FileSource.java)

```
class MyTests {
  @ParameterizedTest
  @FileSource("/test-file.txt")
  public void testSomething(@ConvertWith(IterableConverter.class) int[] nums, int expected) {
    assertThat(new ClassUnderTest().method(nums)).isEqualTo(expected);
  }
}
```

> src/test/resources/test-file.txt

```
[3, 2, 3]
3
[1]
2
```

`@FileSource` may be used along with other JUnit 5 source of arguments, such as `@CsvSource`.
The value may contain multiple filenames, and the annotation may be repeated using `@FileSources`.
It can convert any valid JSON array to a compatible Java container, even 2D arrays or lists of lists.
See Javadoc for more details.

## Build and Lint
```
./gradlew clean build
```

## License

Released under [Apache License v2.0](LICENSE).
