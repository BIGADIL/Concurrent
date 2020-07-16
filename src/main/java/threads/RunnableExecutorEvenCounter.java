package threads;

import common.GlobalContext;
import common.TasksCreator;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class RunnableExecutorEvenCounter {

    private RunnableExecutorEvenCounter() {
    }

    public static void main(final String[] args) throws InterruptedException {
        for (int j = 0; j < GlobalContext.NUM_TRIES; j++) {
            final Queue<Integer> safeTasksQueue = TasksCreator.createSafeTasksQueue(GlobalContext.NUM_TASKS);
            final AtomicInteger evenCounter = new AtomicInteger();
            final ExecutorService threadPool = Executors.newFixedThreadPool(GlobalContext.NUM_THREAD);
            for (int i = 0; i < GlobalContext.NUM_THREAD; i++) {
                threadPool.execute(() -> {
                    while (!safeTasksQueue.isEmpty()) {
                        final Integer task = safeTasksQueue.poll();
                        if (task != null && task % 2 == 0) {
                            evenCounter.incrementAndGet();
                        }
                    }
                });
            }
            threadPool.shutdown();
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            GlobalContext.catchException(evenCounter.get());
        }
    }
}
