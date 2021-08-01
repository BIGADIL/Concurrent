package forkjoin;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public final class FJTree {
    private static final class Node {
        private final List<Node> children;
        private final long value;

        private Node(final long value, final Node... children) {
            this.children = List.of(children);
            this.value = value;
        }

        List<Node> getChildren() {
            return List.copyOf(children);
        }

        long getValue() {
            return value;
        }
    }

    private static final class ValueSumCounter extends RecursiveTask<Long> {
        private final Node node;

        private ValueSumCounter(final Node node) {
            this.node = node;
        }

        @Override
        protected Long compute() {
            long sum = node.getValue();
            final List<ValueSumCounter> subTasks = new LinkedList<>();
            for(final Node child : node.getChildren()) {
                final ValueSumCounter task = new ValueSumCounter(child);
                task.fork();
                subTasks.add(task);
            }
            for(final ValueSumCounter task : subTasks) {
                sum += task.join();
            }
            return sum;
        }
    }

    public static void main(final String[] args) {
        final Node d = new Node(3L);
        final Node c = new Node(5L);
        final Node b = new Node(2L, c, d);
        final Node a = new Node(4L, c);
        final Node root = new Node(1L, a, b);
        final ForkJoinPool pool = new ForkJoinPool(2);
        final long res = pool.invoke(new ValueSumCounter(root));
        System.out.println(res);
    }

    private FJTree() {
    }
}
