package com.asarkar.data;

import static org.assertj.core.api.Assertions.assertThat;

import com.asarkar.junit.IterableConverter;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

class ListNodeTest {
  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      textBlock = """
    []
    [1]
    [1, 2]
    [1, 2, 3, 4, 5]
      """)
  void testListNode(@ConvertWith(IterableConverter.class) List<Integer> nums) {
    ListNode<Integer> head = ListNode.fromIterable(nums);
    if (nums.isEmpty()) {
      assertThat(head).isNull();
    } else {
      assertThat(head).isNotNull();
      List<Integer> actual = ListNode.toList(head);
      assertThat(actual).isEqualTo(nums);
    }
  }
}
