package threads;

import common.GlobalContext;
import common.TasksCreator;

import java.util.Arrays;
import java.util.Queue;

public class SyncBlockEvenCounter extends Thread {

    int evensCounter = 0;

    private final Queue<Integer> tasksQueue;

    public SyncBlockEvenCounter(final Queue<Integer> tasksQueue) {
        this.tasksQueue = tasksQueue;
    }

    @Override
    public void run() {
        while (!tasksQueue.isEmpty()) {
            final Integer task;
            synchronized (SyncBlockEvenCounter.class) {
                task = tasksQueue.poll();
            }
            if (task != null && task % 2 == 0) {
                evensCounter++;
            }
        }
    }

    public static void main(final String[] args) throws InterruptedException {
        for (int j = 0; j < GlobalContext.NUM_TRIES; j++) {
            final Queue<Integer> unsafeTasksQueue = TasksCreator.createUnsafeTasksQueue(GlobalContext.NUM_TASKS);
            final SyncBlockEvenCounter[] workers = new SyncBlockEvenCounter[GlobalContext.NUM_THREAD];
            for (int i = 0; i < workers.length; i++) {
                workers[i] = new SyncBlockEvenCounter(unsafeTasksQueue);
            }
            Arrays.stream(workers).forEach(Thread::start);
            for (final Thread worker : workers) {
                worker.join();
            }
            final int totalNumOfEvents = Arrays.stream(workers).mapToInt(w -> w.evensCounter).sum();
            GlobalContext.catchException(totalNumOfEvents);
        }
    }
}
