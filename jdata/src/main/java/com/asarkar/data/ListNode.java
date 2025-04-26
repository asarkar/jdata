package com.asarkar.data;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Linked list node.
 *
 * @param <T> the type of values stored in this list
 */
@SuppressFBWarnings(
    value = "PA_PUBLIC_PRIMITIVE_ATTRIBUTE",
    justification = "Consumer needs to be able to set next")
public class ListNode<T> {
  public T val;
  public ListNode<T> next;

  /**
   * Constructs a {@code ListNode} with the given value.
   *
   * @param val this node value
   */
  public ListNode(T val) {
    this.val = val;
  }

  /**
   * Constructs a {@code ListNode} with the given value, and the next node.
   *
   * @param val this node value
   * @param next this node's successor
   */
  public ListNode(T val, ListNode<T> next) {
    this.val = val;
    this.next = next;
  }

  /**
   * Builds a linked list from the given {@code values}.
   * The values are used until the first null value.
   *
   * @param values the node values
   * @return the head of the list, or null if {@code values} were empty
   * @param <T> the type of values stored in this list
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public static <T> ListNode<T> fromIterable(Iterable<T> values) {
    ListNode<T> head = null;
    ListNode<T> prev = null;
    for (T t : values) {
      if (prev == null) {
        prev = new ListNode<>(t);
        head = prev;
      } else {
        prev.next = new ListNode<>(t);
        prev = prev.next;
      }
    }
    return head;
  }

  /**
   * Traverses the linked list and returns a list containing the node values.
   *
   * @param head the node to start from
   * @return a list containing the node values
   * @param <T> the type of values stored in this list
   */
  public static <T> List<T> toList(ListNode<T> head) {
    List<T> list = new ArrayList<>();
    ListNode<T> node = head;
    while (node != null) {
      list.add(node.val);
      node = node.next;
    }
    return Collections.unmodifiableList(list);
  }
}
