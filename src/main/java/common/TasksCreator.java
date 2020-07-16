package common;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class TasksCreator {
    private TasksCreator() {
    }

    public static Queue<Integer> createUnsafeTasksQueue(final int numTasks) {
        final Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numTasks; i++) {
            queue.add(i);
        }
        return queue;
    }

    public static Queue<Integer> createSafeTasksQueue(final int numTasks) {
        return new ConcurrentLinkedQueue<>(createUnsafeTasksQueue(numTasks));
    }

    public static int[] createArrayOfTasks(final int numTasks) {
        final int[] tasks = new int[numTasks];
        for (int i = 0; i < numTasks; i++) {
            tasks[i] = i;
        }
        return tasks;
    }
}
