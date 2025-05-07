package com.asarkar.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.asarkar.junit.IterableConverter;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("PMD.TooManyMethods")
class FenwickTreeTest {
  private static final int LOOP_LIMIT = 1000;

  private final Random rand = new Random();

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      textBlock =
          """
            [1, 2, 3, 4, 5, 6]
            [-1, -2, -3, -4, -5, -6]
            [-76871, -164790]
        """)
  void testRangeQueries(@ConvertWith(IterableConverter.class) int[] arr) {
    FenwickTree tree = new FenwickTree(arr);
    int[] sums = prefixSums(arr);

    for (int[] p : pairs(arr.length)) {
      int actual = tree.queryRange(p[0], p[1]);
      int expected = sums[p[1] - 1] - sums[p[0] - 1] + arr[p[0] - 1];
      assertThat(actual).isEqualTo(expected);
    }
  }

  // Generates all 2-tuples in the closed range [1, n].
  private int[][] pairs(int n) {
    // There are n pairs starting with 1, n-1 pairs starting with 2, and so on.
    // The sum of the series (n + n-1 + ... + 1) is given by n(n+1)/2.
    int[][] ans = new int[(n * n + n) / 2][2];
    int k = 0;
    for (int i = 0; i < n; i++) {
      for (int j = i; j < n; j++) {
        ans[k][0] = i + 1;
        ans[k][1] = j + 1;
        k++;
      }
    }
    return ans;
  }

  private int[] prefixSums(int[] arr) {
    if (arr == null || arr.length == 0) {
      return arr;
    }
    int[] ans = new int[arr.length];
    ans[0] = arr[0];
    return IntStream.range(1, arr.length)
        .collect(() -> ans, (sum, i) -> sum[i] = sum[i - 1] + arr[i], (x, y) -> {});
  }

  @SuppressWarnings({"PMD.UnitTestShouldIncludeAssert"})
  @RepeatedTest(value = LOOP_LIMIT, failureThreshold = 1)
  void testRandomRangeQueries(RepetitionInfo repInfo) {
    int[] rands = randList(repInfo.getCurrentRepetition());
    FenwickTree tree = new FenwickTree(rands);

    for (int j = 0; j < LOOP_LIMIT / 10; j++) {
      doRandomRangeQuery(rands, tree);
    }
  }

  /*
  Adds a random value to a random index in the Fenwick Tree,
  and then does random range queries.
   */
  @RepeatedTest(value = LOOP_LIMIT, failureThreshold = 1)
  @SuppressWarnings({"PMD.UnitTestShouldIncludeAssert"})
  void testRandomAdd(RepetitionInfo repInfo) {
    int[] rands = randList(repInfo.getCurrentRepetition());
    FenwickTree tree = new FenwickTree(rands);

    for (int j = 0; j < LOOP_LIMIT / 10; j++) {
      int idx = rand.nextInt(1, rands.length + 1);
      int val = randInt();

      rands[idx - 1] += val;
      tree.add(idx, val);

      doRandomRangeQuery(rands, tree);
    }
  }

  /*
  Verifies that the values in an existing Fenwick Tree can be
  overwritten by repeatedly calling set with a random value.
   */
  @SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
  @Test
  void testReusability() {
    int n = 1000;
    FenwickTree tree = new FenwickTree(n);
    int[] arr = new int[n];

    for (int loop = 0; loop < LOOP_LIMIT; loop++) {
      for (int i = 1; i <= n; i++) {
        int val = randInt();
        tree.set(i, val);
        arr[i - 1] = val;
      }
      doRandomRangeQuery(arr, tree);
    }
  }

  @Test
  void testIllegalConstructionNullArray() {
    assertThatIllegalArgumentException().isThrownBy(() -> new FenwickTree(null));
  }

  @Test
  void testIllegalConstructionNegativeSize() {
    assertThatIllegalArgumentException().isThrownBy(() -> new FenwickTree(-1));
  }

  @Test
  void testInvalidIndexQuery() {
    FenwickTree tree = new FenwickTree(1);
    assertThatIllegalArgumentException().isThrownBy(() -> tree.query(0));
    assertThatIllegalArgumentException().isThrownBy(() -> tree.query(-1));
  }

  @Test
  void testInvalidIndexQueryRange() {
    FenwickTree tree = new FenwickTree(1);
    assertThatIllegalArgumentException().isThrownBy(() -> tree.queryRange(2, 1));
    assertThatIllegalArgumentException().isThrownBy(() -> tree.queryRange(0, 1));
    assertThatIllegalArgumentException().isThrownBy(() -> tree.queryRange(-1, 1));
  }

  @Test
  void testInvalidIndexAdd() {
    FenwickTree tree = new FenwickTree(1);
    assertThatIllegalArgumentException().isThrownBy(() -> tree.add(0, 1));
    assertThatIllegalArgumentException().isThrownBy(() -> tree.add(-1, 1));
  }

  @Test
  void testInvalidIndexSet() {
    FenwickTree tree = new FenwickTree(1);
    assertThatIllegalArgumentException().isThrownBy(() -> tree.set(0, 1));
    assertThatIllegalArgumentException().isThrownBy(() -> tree.set(-1, 1));
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      textBlock =
          """
        [12, 1, 2, 3, 0, 11, 4] | [6, 1, 1, 1, 0, 1, 0]
        [5, 4, 3, 2, 1] | [4, 3, 2, 1, 0]
        [1, 2, 3, 4, 5] | [0, 0, 0, 0, 0]
        [3, 7, 5, 6] | [0, 2, 0, 0]
      """)
  void testCountSmallerRight(
      @ConvertWith(IterableConverter.class) int[] arr,
      @ConvertWith(IterableConverter.class) int[] expected) {
    int n = arr.length;
    int[] cntSmallerRight = new int[n];
    FenwickTree tree = new FenwickTree(n);
    int[] ranks = ranks(arr);
    for (int i = n - 1; i >= 0; i--) {
      /*
      query() returns the sum of the closed interval [1, ranks[i]].
      Since ranks[i] is not yet inserted into the tree, we get the
      count of the values smaller than ranks[i], which are the values
      to the right of ranks[i] (because we're iterating in reverse).
       */
      cntSmallerRight[i] = tree.query(ranks[i]);
      tree.add(ranks[i], 1);
    }
    assertThat(cntSmallerRight).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      textBlock =
          """
        [12, 1, 2, 3, 0, 11, 4] | [0, 1, 1, 1, 4, 1, 2]
        [5, 4, 3, 2, 1] | [0, 1, 2, 3, 4]
        [1, 2, 3, 4, 5] | [0, 0, 0, 0, 0]
        [3, 7, 5, 6] | [0, 0, 1, 1]
      """)
  void testCountGreaterLeft(
      @ConvertWith(IterableConverter.class) int[] arr,
      @ConvertWith(IterableConverter.class) int[] expected) {
    int n = arr.length;
    int[] cntGreaterLeft = new int[n];
    FenwickTree tree = new FenwickTree(n);
    int[] ranks = ranks(arr);
    for (int i = 0; i < n; i++) {
      /*
      query() returns the sum of the closed interval [1, ranks[i]].
      Since ranks[i] is not yet inserted into the tree, we get the
      count of the values smaller than ranks[i], which are the values
      to the left of ranks[i]. The total number of elements to the
      left of ranks[i] is 'i', so, i - ranks[i] gives us the number
      of elements on the left of ranks[i] and greater than it.
       */
      cntGreaterLeft[i] = i - tree.query(ranks[i]);
      tree.add(ranks[i], 1);
    }
    assertThat(cntGreaterLeft).isEqualTo(expected);
  }

  /*
  Returns an array of positive numbers 1 through n, where n is the length of the input array,
  such that there's a bijection between the input and the output arrays, and the relative order
  between the elements of the input array is preserved.

  Example: input=[10, -4, 5], output=[3, 1, 2].
   */
  private int[] ranks(int[] arr) {
    int[] sortedArr = Arrays.stream(arr).sorted().toArray();
    return Arrays.stream(arr).map(i -> Arrays.binarySearch(sortedArr, i) + 1).toArray();
  }

  // Verifies that the Fenwick tree correctly computes the sum of a subarray of random size.
  private void doRandomRangeQuery(int[] arr, FenwickTree tree) {
    int n = arr.length;

    // A random integer in the closed range [1, n].
    int lo = rand.nextInt(1, n + 1);
    // A random integer in the closed range [lo, n].
    int hi = rand.nextInt(lo, n + 1);

    int actual = tree.queryRange(lo, hi);
    int expected = IntStream.rangeClosed(lo, hi).map(i -> arr[i - 1]).sum();
    assertThat(actual).isEqualTo(expected);
  }

  // Returns an array of random numbers of length 'size'.
  private int[] randList(int size) {
    return IntStream.iterate(0, i -> i < size, i -> randInt()).toArray();
  }

  private int randInt() {
    return rand.nextInt(-1000, 3000);
  }
}
