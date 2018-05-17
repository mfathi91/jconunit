package com.github.mfathi91;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Provides static package-priavate methods for other classes.
 *
 * <p>This class is stateless and threa-safe.
 */
final class JConUnitCore {

    /**
     * Ensure non-instantiability.
     */
    private JConUnitCore() {
        throw new AssertionError();
    }

    /**
     * Executes parameter {@code task} with {@code numThreads} separated threads
     * and returns an instance of {@link Duration} as elapsed time for running
     * runnable in all the threads.
     *
     * <p>If one or more subtype of {@link Exception} occurs during running
     * {@code task}, one of them will be randomly thrown. It is noteworty that the
     * thrown exception will be in the main thread that this method is called.
     *
     * @param task       instance of {@link Runnable} to run in {@code numThreads}
     *                   separated threads, not null
     * @param numThreads number of threads to create and run {@code task}
     * @return an instance of {@link Duration} that is elapsed time for running
     * runnable in all the threads
     * @throws NullPointerException     if parameter {@code concurrentExecutable} is null
     * @throws IllegalArgumentException if parameter {@code numThreads} less than 1
     */
    static Duration concurrentExecute(Runnable task, int numThreads) {
        Objects.requireNonNull(task, "task");
        if (numThreads < 1) {
            throw new IllegalArgumentException(
                    "number of threads cannot be nonpositive: " + numThreads);
        }
        List<Exception> exceptions = new CopyOnWriteArrayList<>();
        CountDownLatch ready = new CountDownLatch(numThreads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(numThreads);
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                ready.countDown();
                try {
                    start.await();
                } catch (InterruptedException e) {
                    throw new UnknownConcurrentExecutionException(e);
                }
                try {
                    task.run();
                } catch (Exception exception) {
                    exceptions.add(exception);
                } finally {
                    done.countDown();
                }
            });
        }

        try {
            ready.await();
            long startNanos = System.nanoTime();
            start.countDown();
            done.await();
            long endNanos = System.nanoTime();
            if (!exceptions.isEmpty())
                throwAsUncheckedException(exceptions.get(0));
            return Duration.ofNanos(endNanos - startNanos);
        } catch (InterruptedException e) {
            throw new UnknownConcurrentExecutionException(e);
        }
    }

    /**
     * This method is used to trick the compiler to eliminate the necessity of
     * catching checked exceptions.
     *
     * @param ex exception to throw as an unchecked exception.
     */
    private static void throwAsUncheckedException(Exception ex) {
        Objects.requireNonNull(ex, "exception");
        throwAs(ex);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Exception> void throwAs(Exception ex) throws T {
        throw (T) ex;
    }

}
