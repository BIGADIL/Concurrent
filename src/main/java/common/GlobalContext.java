package common;

public final class GlobalContext {
    public static final int NUM_THREAD = 4;

    public static final int NUM_TASKS = 1000;

    public static final int NUM_TRIES = 100;

    private GlobalContext() {
    }

    public static void catchException(final int totalNumOfEvents) {
        if (totalNumOfEvents != NUM_TASKS / 2) {
            throw new IllegalStateException(
                    String.format("Expect %d evens, actual %d evens",
                    NUM_TASKS / 2, totalNumOfEvents));
        }
    }
}
