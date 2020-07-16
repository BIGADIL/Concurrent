package threads;

import common.GlobalContext;
import common.TasksCreator;

import java.util.Arrays;
import java.util.Queue;

public class SyncMethodEvenCounter extends Thread {

    int evensCounter = 0;

    private final Queue<Integer> tasksQueue;

    public SyncMethodEvenCounter(final Queue<Integer> tasksQueue) {
        this.tasksQueue = tasksQueue;
    }

    private static synchronized Integer getTask(final Queue<Integer> tasksQueue) {
        return tasksQueue.poll();
    }

    @Override
    public void run() {
        while (!tasksQueue.isEmpty()) {
            final Integer task = getTask(tasksQueue);
            if (task != null && task % 2 == 0) {
                evensCounter++;
            }
        }
    }

    public static void main(final String[] args) throws InterruptedException {
        for (int j = 0; j < GlobalContext.NUM_TRIES; j++) {
            final Queue<Integer> unsafeTasksQueue = TasksCreator.createUnsafeTasksQueue(GlobalContext.NUM_TASKS);
            final SyncMethodEvenCounter[] workers = new SyncMethodEvenCounter[GlobalContext.NUM_THREAD];
            for (int i = 0; i < workers.length; i++) {
                workers[i] = new SyncMethodEvenCounter(unsafeTasksQueue);
                workers[i].start();
            }
            for (final SyncMethodEvenCounter worker : workers) {
                worker.join();
            }
            final int totalNumOfEvents = Arrays.stream(workers).mapToInt(w -> w.evensCounter).sum();
            GlobalContext.catchException(totalNumOfEvents);
        }
    }
}
