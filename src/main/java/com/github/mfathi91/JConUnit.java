package com.github.mfathi91;

import java.time.Duration;
import java.util.Objects;

public final class JConUnit {

    /**
     * Ensure non-instantiabilty.
     */
    private JConUnit() {
        throw new AssertionError();
    }

    /**
     * Executes the parameter {@code executable} based on its configuration.
     * If one or more exceptions were thrown by one of threads during running
     * {@code runnable} of {@code executable}, one of the thrown exceptions
     * will be randonly thrown.
     *
     * @param executable instance of {@link Executable}, not null
     * @throws NullPointerException if parameter {@code executable} is null
     */
    public static void concurrentExecute(Executable executable) {
        Objects.requireNonNull(executable, "executable");
        JConUnitCore.concurrentExecute(
                executable.getRunnable(),
                executable.getNumThreads());
    }

    /**
     * Executes {@code executable} based on its configuration. If one or
     * more exceptions were thrown by threads during running the runnable of
     * {@code executable}, then one of them will be randomly thrown.
     *
     * <p>If no exception were thrown by any threads, then elapsed time for running
     * runnable of all threads will be compared to the parameter {@code duration}.
     * If elapsed time for running all runnables is more than parameter
     * {@code duration}, an {@link AssertionError} with a suitable message will be
     * thrown.
     *
     * <p>precision of timeout calculating is milliseconds.
     *
     * @param duration           reference timeout to check, not null
     * @param executable instance of {@link Executable}, not null
     */
    public static void assertTimeout(Duration duration, Executable executable) {
        Objects.requireNonNull(duration, "duration");
        Objects.requireNonNull(executable, "executable");
        Duration actualDuration = JConUnitCore.concurrentExecute(
                executable.getRunnable(),
                executable.getNumThreads());
        if (duration.compareTo(actualDuration) < 0) {
            throw new AssertionError("execution exceeded timeout of " + duration.toMillis()
                    + " ms by " + actualDuration.toMillis() + " ms");
        }
    }
}
