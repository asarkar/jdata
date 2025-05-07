package com.asarkar.data;

import java.util.Arrays;

/**
 * A Fenwick Tree implementation that supports point updates and sum range queries.
 * The idea is based on the fact that all positive integers can be represented as the
 * sum of powers of 2. For example, 19 can be represented as 16 + 2 + 1. Every node of
 * the Fenwick Tree stores the sum of n elements where n is a power of 2.
 * <p>
 * The number of bits in the binary representation of a number n is {@code O(log n)}.
 * Therefore, all operations run in {@code O(log n)} time.
 * Construction from a given array runs in linear time.
 */
public final class FenwickTree {

  // The size of Fenwick tree, i.e. the number of nodes in it.
  final int size;

  private final int[] tree;

  /**
   * Creates an empty Fenwick Tree of size {@code size + 1}.
   *
   * @param size size of the tree, zero-based
   * @throws IllegalArgumentException if {@code size} is negative
   */
  public FenwickTree(int size) {
    if (size < 0) {
      throw new IllegalArgumentException("size must not be negative");
    }
    this.size = size + 1;
    this.tree = new int[this.size];
  }

  /**
   * Constructs a Fenwick tree with an initial set of values. The size of the tree
   * is one greater than the length of the array; the 0-th element is unused.
   *
   * @param values initial values for building a Fenwick tree
   * @throws IllegalArgumentException if the given array is null
   */
  public FenwickTree(int[] values) {
    if (values == null) {
      throw new IllegalArgumentException("values array must not be null");
    }

    size = values.length + 1;
    tree = new int[size];
    System.arraycopy(values, 0, tree, 1, values.length);

    for (int i = 1; i < size; i++) {
      int parent = i + Integer.lowestOneBit(i);
      if (parent < size) {
        tree[parent] += tree[i];
      }
    }
  }

  /**
   * Returns the sum of the closed interval {@code [1, i]}.
   *
   * @param i end index of the interval, inclusive
   * @return the sum of the closed interval {@code [1, i]}
   * @throws IllegalArgumentException if the index {@code i} is smaller than 1
   */
  @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
  public int query(int i) {
    if (i < 1) {
      throw new IllegalArgumentException("index must be positive");
    }
    return sum(i);
  }

  private int sum(int i) {
    int sum = 0;
    int j = i;
    while (j > 0) {
      sum += tree[j];
      j -= Integer.lowestOneBit(j);
    }
    return sum;
  }

  /**
   * Returns the sum of the closed interval {@code [left, right]}.
   *
   * @param left start index of the interval, inclusive
   * @param right end index of the interval, inclusive
   * @return the sum of the interval {@code [left, right]}
   * @throws IllegalArgumentException if {@code left} is greater than {@code right}
   * @throws IllegalArgumentException if the index {@code left} is smaller than 1
   */
  @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
  public int queryRange(int left, int right) {
    if (left > right) {
      throw new IllegalArgumentException("invalid range, right must not be smaller than left");
    }
    if (left < 1) {
      throw new IllegalArgumentException("left must be positive");
    }
    return sum(right) - sum(left - 1);
  }

  /**
   * Adds {@code val} to the value at index {@code i}.
   *
   * @param i the index
   * @param val value to add to the value at index {@code i}
   * @throws IllegalArgumentException if the index {@code left} is smaller than 1
   */
  @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
  public void add(int i, int val) {
    if (i < 1) {
      throw new IllegalArgumentException("index must be positive");
    }
    int j = i;
    while (j < size) {
      tree[j] += val;
      j += Integer.lowestOneBit(j);
    }
  }

  /**
   * Sets the value at index {@code i}.
   *
   * @param i the index
   * @param val value to set at index {@code i}
   */
  public void set(int i, int val) {
    add(i, val - queryRange(i, i));
  }

  @Override
  public String toString() {
    return Arrays.toString(tree);
  }
}
