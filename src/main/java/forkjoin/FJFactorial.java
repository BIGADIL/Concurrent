package forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public final class FJFactorial {
    private static final class FactorialTask extends RecursiveTask<Long> {
        private final long n;

        private FactorialTask(final long n) {
            this.n = n;
        }

        @Override
        protected Long compute() {
            if (n < 0L) {
                throw new IllegalArgumentException("Invalid n = " + n);
            }
            if (n == 0L || n == 1L) {
                return 1L;
            }
            final FactorialTask subTask = new FactorialTask(n - 1L);
            return n * subTask.fork().join();
        }
    }

    public static void main(final String[] args) {
        final ForkJoinPool pool = new ForkJoinPool(2);
        final long n = 4L;
        final long f = pool.invoke(new FactorialTask(n));
        System.out.println("n=" + n + ", fact=" + f);
    }


    private FJFactorial() {
    }

}
