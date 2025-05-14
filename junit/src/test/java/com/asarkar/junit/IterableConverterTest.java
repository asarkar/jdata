package com.asarkar.junit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class IterableConverterTest {
  @Test
  void testConvertToArray() {
    var converter = new IterableConverter();
    assertThat(converter.convert("[]", int[].class)).isEqualTo(new int[0]);
    assertThat(converter.convert("[1]", int[].class)).isEqualTo(new int[] {1});
    assertThat(converter.convert("[1, 2]", Integer[].class)).isEqualTo(new Integer[] {1, 2});
    assertThat(converter.convert("[1, null, 2]", Integer[].class))
        .isEqualTo(new Integer[] {1, null, 2});
  }

  @Test
  void testConvertToList() {
    var converter = new IterableConverter();
    assertThat(converter.convert("[]", List.class)).isEqualTo(List.of());
    assertThat(converter.convert("[1]", List.class)).isEqualTo(List.of(1));
    assertThat(converter.convert("[1, 2]", List.class)).isEqualTo(List.of(1, 2));
    var expected = new ArrayList<Integer>();
    expected.add(1);
    expected.add(null);
    expected.add(2);
    assertThat(converter.convert("[1, null, 2]", List.class)).isEqualTo(expected);
  }

  @Test
  void testConvertTo2DArray() {
    var converter = new IterableConverter();
    assertThat(converter.convert("[[1], [2]]", int[][].class)).isEqualTo(new int[][] {{1}, {2}});
    assertThat(converter.convert("[[1], [2]]", Integer[][].class))
        .isEqualTo(new Integer[][] {{1}, {2}});
  }

  @Test
  void testConvertToListOfLists() {
    var converter = new IterableConverter();
    assertThat(converter.convert("[[1], [2]]", List.class))
        .isEqualTo(List.of(List.of(1), List.of(2)));
  }

  @Test
  void testNoConvert() {
    var converter = new IterableConverter();
    assertThat(converter.convert(List.of(1), List.class)).isEqualTo(List.of(1));
  }
}
