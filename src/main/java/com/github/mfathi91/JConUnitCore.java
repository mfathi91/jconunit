package com.github.mfathi91;

import java.time.Duration;
import java.util.ArrayList;
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
     * Executes each one of {@code runnables} in a separated thread concurrently
     * and returns an instance of {@link Duration} as elapsed time for running
     * all of them.
     *
     * <p>If one or more subtype of {@link Exception} occurs during running
     * any of the {@code runnables}, one of them will be randomly thrown. It is
     * noteworty that the thrown exception will be in the main thread that this
     * method is called.
     *
     * @param runnables list of {@link Runnable}, not null
     * @return an instance of {@link Duration} that is elapsed time for running
     * runnable in all the threads
     * @throws NullPointerException if parameter {@code runnables} is null
     * @throws NullPointerException if parameter {@code runnables} contains null
     */
    static Duration concurrentExecute(List<Runnable> runnables) {
        Objects.requireNonNull(runnables, "runnables");
        if (runnables.contains(null)) {
            throw new NullPointerException("runnables can not contain null");
        }

        List<Exception> exceptions = new CopyOnWriteArrayList<>();
        CountDownLatch ready = new CountDownLatch(runnables.size());
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(runnables.size());
        ExecutorService executor = Executors.newFixedThreadPool(runnables.size());

        for (Runnable runnable : runnables) {
            executor.execute(() -> {
                ready.countDown();
                try {
                    start.await();
                } catch (InterruptedException e) {
                    throwAsUncheckedException(e);
                }
                try {
                    runnable.run();
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
            throwAsUncheckedException(e);
            // This wil be never executed
            return null;
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
