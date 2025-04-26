package com.asarkar.data;

import static org.assertj.core.api.Assertions.assertThat;

import com.asarkar.junit.IterableConverter;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

class TreeNodeTests {
    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock =
                    """
    []
    [1]
    [1, 2]
    [1, 2, 3]
    [1, null, 2]
    [1, 2, null, 3]
    [4, 2, 5, 1, 3]
    [3, 1, 4, null, 2]
    [1, 3, 2, 5, 3, null, 9]
    [10, 5, 15, 3, 7, null, 18]
    [7, 3, 15, null, null, 9, 20]
    [1, 2, 3, null, null, 4, 5, 6, 7]
    [10, 5, 15, 3, 7, 13, 18, 1, null, 6]
    [3, 5, 1, 6, 2, 0, 8, null, null, 7, 4]
      """)
    void testTreeNode(@ConvertWith(IterableConverter.class) List<Integer> nums) {
        TreeNode<Integer> root = TreeNode.fromList(nums);
        if (nums.isEmpty()) {
            assertThat(root).isNull();
        } else {
            assertThat(root).isNotNull();
            List<Integer> actual = TreeNode.levelOrder(root);
            assertThat(actual).isEqualTo(nums);
        }
    }
}
