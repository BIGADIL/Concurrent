package threads;

import common.GlobalContext;
import common.TasksCreator;

import java.util.*;
import java.util.concurrent.*;

public final class CallableExecutorEvenCounter {

    private CallableExecutorEvenCounter() {
    }

    public static void main(final String[] args) throws InterruptedException, ExecutionException {
        for (int j = 0; j < GlobalContext.NUM_TRIES; j++) {
            final Queue<Integer> unsafeTasksQueue = TasksCreator.createUnsafeTasksQueue(GlobalContext.NUM_TASKS);
            final ExecutorService threadPool = Executors.newFixedThreadPool(GlobalContext.NUM_THREAD);
            final List<Future<Integer>> futures = new ArrayList<>();
            for (final Integer queueElem : unsafeTasksQueue) {
                final Callable<Integer> task = () -> queueElem % 2 == 0 ? 1 : 0;
                futures.add(threadPool.submit(task));
            }
            threadPool.shutdown();
            int totalNumOfEvents = 0;
            for (final Future<Integer> future : futures) {
                totalNumOfEvents += future.get();
            }
            GlobalContext.catchException(totalNumOfEvents);
        }
    }
}
