package threads;

import common.GlobalContext;
import common.TasksCreator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class ArrayEvenCounter {

    private ArrayEvenCounter() {
    }

    public static void main(final String[] args) throws InterruptedException {
        for (int j = 0; j < GlobalContext.NUM_TRIES; j++) {
            final int[] arrayOfTasks = TasksCreator.createArrayOfTasks(GlobalContext.NUM_TASKS);
            final AtomicInteger evenCounter = new AtomicInteger();
            final ExecutorService threadPool = Executors.newFixedThreadPool(GlobalContext.NUM_THREAD);
            for (int i = 0; i < GlobalContext.NUM_THREAD; i++) {
                final int threadId = i;
                threadPool.execute(() -> {
                    for (int k = threadId; k < arrayOfTasks.length; k += GlobalContext.NUM_THREAD) {
                        if (arrayOfTasks[k] % 2 == 0) {
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
