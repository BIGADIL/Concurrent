package threads;

import common.GlobalContext;
import common.TasksCreator;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class ThreadWithRunnableEvenCounter {

    private ThreadWithRunnableEvenCounter() {
    }

    public static void main(final String[] args) throws InterruptedException {
        for (int j = 0; j < GlobalContext.NUM_TRIES; j++) {
            final Queue<Integer> safeTasksQueue = TasksCreator.createSafeTasksQueue(GlobalContext.NUM_TASKS);
            final Thread[] workers = new Thread[GlobalContext.NUM_THREAD];
            final Queue<Integer> evensList = new ConcurrentLinkedQueue<>();
            for (int i = 0; i < workers.length; i++) {
                workers[i] = new Thread(() -> {
                    while (!safeTasksQueue.isEmpty()) {
                        final Integer task = safeTasksQueue.poll();
                        if (task != null && task % 2 == 0) {
                            evensList.add(task);
                        }
                    }
                });
                workers[i].start();
            }
            for (final Thread worker : workers) {
                worker.join();
            }
            GlobalContext.catchException(evensList.size());
        }
    }
}
