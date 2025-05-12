# Java Data Structures
Java package containing simple data structures and JUnit 5 utilities used in coding questions 
found on online platform such as LeetCode. This package can be used while working on such
questions locally.

Meant to be used in personal projects only, and makes absolutely no guarantees.

[![Sponsor](https://img.shields.io/badge/Sponsor-%E2%9D%A4-black?style=for-the-badge&logo=github&color=%23fe8e86)](https://github.com/sponsors/asarkar)
[![CI](<https://img.shields.io/github/actions/workflow/status/asarkar/jdata/ci.yml?branch=main&style=for-the-badge&logo=github>)](https://github.com/asarkar/grpc-test/actions?query=workflow%3A%22CI%22)
[![JitPack](https://img.shields.io/jitpack/version/com.asarkar/jdata?style=for-the-badge&logo=jitpack&color=blue)](https://jitpack.io/#com.asarkar/jdata)
[![License](https://img.shields.io/github/license/asarkar/jdata?style=for-the-badge&logo=apache&color=brightgreen)](https://www.apache.org/licenses/LICENSE-2.0)
[![JVM](https://img.shields.io/badge/dynamic/regex?style=for-the-badge&logo=openjdk&color=blue&label=JVM&url=https%3A%2F%2Fraw.githubusercontent.com%2Fasarkar%2Fjdata%2Frefs%2Fheads%2Fmain%2F.java-version&search=%5Cd%2B)](https://github.com/asarkar/jdata/blob/main/.java-version)

## Development

1. Add the JitPack repository to your Maven or Gradle build:
   https://jitpack.io

2. Dependency group id = `com.asarkar.jdata`. There are two artifacts under this group,
   `jdata` and `junit`, choose one or both according to your need.

3. For development version, use a [commit hash](https://github.com/asarkar/jdata/commits/).
   For release version, use [tag](https://github.com/asarkar/jdata/tags).

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

* [FenwickTree](jdata/src/main/java/com/asarkar/data/FenwickTree.java)

```
int[] arr = new int[] {1, 2, 3, 4, 5, 6};
FenwickTree tree = new FenwickTree(arr);
tree.queryRange(1, 2);  // =3

tree = new FenwickTree(6);
for (int i = 1; i <= 6; i++) {
   tree.add(i, i);
}
tree.queryRange(1, 2);  // =3
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
