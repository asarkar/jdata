package com.asarkar.data;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Binary tree node.
 * @param <T> the type of values stored in this tree
 */
@SuppressFBWarnings(
        value = "PA_PUBLIC_PRIMITIVE_ATTRIBUTE",
        justification = "Consumer needs to be able to set left and right")
public class TreeNode<T> {
    private static final Pattern RTRIM = Pattern.compile("\\s+$");

    public final T val;
    public TreeNode<T> left;
    public TreeNode<T> right;

    /**
     * Constructs a {@code TreeNode} with the given value.
     *
     * @param val this node value
     */
    @SuppressWarnings("PMD.CommentRequired")
    public TreeNode(T val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    /**
     * Builds a binary tree in level order from the given {@code values}.
     * The list may contain null values to indicate absence of specific subtrees.
     *
     * @param values the node values
     * @return the root of the tree, or null if {@code values} were empty
     * @param <T> the type of values stored in this tree
     */
    @SuppressWarnings({"PMD.SystemPrintln", "PMD.AvoidInstantiatingObjectsInLoops"})
    public static <T> TreeNode<T> fromList(List<T> values) {
        Deque<TreeNode<T>> queue = new ArrayDeque<>();
        TreeNode<T> root = null;
        if (!values.isEmpty() && values.get(0) != null) {
            root = new TreeNode<>(values.get(0));
            queue.add(root);
        }
        int i = 1;
        while (i < values.size()) {
            TreeNode<T> node = queue.removeFirst();
            if (values.get(i) != null) {
                node.left = new TreeNode<>(values.get(i));
                queue.add(node.left);
            }
            i++;
            if (i < values.size() && values.get(i) != null) {
                node.right = new TreeNode<>(values.get(i));
                queue.add(node.right);
            }
            i++;
        }
        System.out.println(values);
        prettyPrint(root);
        return root;
    }

    /**
     * Does a level order traversal of this tree.
     *
     * @param root the node to start from
     * @return a list containing the level order traversal of its nodes' values
     * @param <T> the type of values stored in this tree, and the type of the output list
     */
    @SuppressWarnings("JdkObsolete")
    public static <T> List<T> levelOrder(TreeNode<T> root) {
        if (root == null) {
            return List.of();
        }
        Deque<TreeNode<T>> lvl = new LinkedList<>();
        Deque<TreeNode<T>> nxtLvl = new LinkedList<>();
        int lvlCnt = 1;
        int nxtLvlCnt = 0;
        List<T> vals = new ArrayList<>();
        lvl.add(root);

        while (lvlCnt != 0 || nxtLvlCnt != 0) {
            TreeNode<T> node = lvl.removeFirst();
            if (node != null) {
                vals.add(node.val);
                lvlCnt--;
                nxtLvlCnt += node.left == null ? 0 : 1;
                nxtLvlCnt += node.right == null ? 0 : 1;
                nxtLvl.add(node.left);
                nxtLvl.add(node.right);
            } else {
                vals.add(null);
            }
            if (lvl.isEmpty()) {
                Deque<TreeNode<T>> tmp = nxtLvl;
                nxtLvl = lvl;
                lvl = tmp;
                lvlCnt = nxtLvlCnt;
                nxtLvlCnt = 0;
            }
        }
        return Collections.unmodifiableList(vals);
    }

    /**
     * Prints the given tree to the console.
     *
     * @param node the root node
     * @param <T> the type of values stored in this tree
     */
    @SuppressWarnings("PMD.SystemPrintln")
    public static <T> void prettyPrint(TreeNode<T> node) {
        List<String> box = prettyString(node, 0).box;
        String str = box.stream()
                .map(s -> RTRIM.matcher(s).replaceAll(""))
                .collect(Collectors.joining(System.lineSeparator()));
        System.out.printf("%n%s", str);
    }

    private record Data(List<String> box, int boxWidth, int rootStart, int rootEnd) {}

    private static <T> Data prettyString(TreeNode<T> node, int currIdx) {
        if (node == null) {
            return new Data(List.of(), 0, 0, 0);
        }

        String nodeStr = node.toString();

        int newRootWidth = nodeStr.length();
        int gapSize = newRootWidth;
        List<String> line1 = new ArrayList<>();
        List<String> line2 = new ArrayList<>();

        Data l = prettyString(node.left, 2 * currIdx + 1);

        int newRootStart = 0;
        if (l.boxWidth > 0) {
            int root = (l.rootStart + l.rootEnd) / 2 + 1;
            line1.add(" ".repeat(root + 1));
            line1.add("_".repeat(l.boxWidth - root));
            line2.add(" ".repeat(root) + "/");
            line2.add(" ".repeat(l.boxWidth - root));
            newRootStart = l.boxWidth + 1;
            gapSize++;
        }

        line1.add(nodeStr);
        line2.add(" ".repeat(newRootWidth));

        Data r = prettyString(node.right, 2 * currIdx + 2);
        if (r.boxWidth > 0) {
            int root = (r.rootStart + r.rootEnd) / 2;
            line1.add("_".repeat(root));
            line1.add(" ".repeat(r.boxWidth - root + 1));
            line2.add(" ".repeat(root) + "\\");
            line2.add(" ".repeat(r.boxWidth - root));
            gapSize++;
        }

        String gap = " ".repeat(gapSize);
        List<String> newBox = new ArrayList<>();
        newBox.add(String.join("", line1));
        newBox.add(String.join("", line2));
        for (int i = 0; i < Math.max(l.box().size(), r.box().size()); i++) {
            String line3 = i < l.box().size() ? l.box().get(i) : " ".repeat(l.boxWidth);
            String line4 = i < r.box().size() ? r.box().get(i) : " ".repeat(r.boxWidth);
            newBox.add(line3 + gap + line4);
        }

        int newRootEnd = newRootStart + newRootWidth - 1;
        return new Data(Collections.unmodifiableList(newBox), newBox.get(0).length(), newRootStart, newRootEnd);
    }
}
