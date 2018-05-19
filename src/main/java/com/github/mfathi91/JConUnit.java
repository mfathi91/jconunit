package com.github.mfathi91;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * This class provides static methods to provide concurrent execution
 * utilities.
 *
 * <p>This class is stateless and thread-safe.
 */
public final class JConUnit {

    /**
     * Ensure non-instantiabilty.
     */
    private JConUnit() {
        throw new AssertionError();
    }

    /**
     * Asserts that execution of each element of the parameter {@code runnables}
     * in multiple threads does not any exception. Each element of {@code runnables}
     * will be run in a distinct thread.
     *
     * <p>If one or more exceptions were thrown by one of the created threads during
     * running one of the {@code runnables}, one of the thrown exceptions will be
     * randonly thrown.
     *
     * <p>As JUnit notifies user about thrown exceptions, using this method seems
     * nonfunctional, but it provides a syntactical assertion and user can
     * immediately understand purpose of unit test even in the lack of documentation.
     *
     * @param runnables list of {@link Runnable}, not null
     * @throws NullPointerException if parameter {@code runnables} is null
     * @throws NullPointerException if parameter {@code runnables} contains null
     */
    public static void assertDoesNotThrowException(List<Runnable> runnables) {
        JConUnitCore.concurrentExecute(runnables);
    }

    /**
     * Asserts that execution of elements (runnables) of parameter
     * {@code runnables} will throw an exception of the {@code expectedType}.
     *
     * <p>If no exception is thrown, or if an exception of a different type is
     * thrown, this method will fail.
     *
     * @throws AssertionError       if the thrown exception by {@code executable}
     *                              was different with {@code expectedType}
     * @throws AssertionError       if no exception was thrown by {@code executable}
     * @throws NullPointerException if parameter {@code expectedType} is null
     * @throws NullPointerException if parameter {@code executable} is null
     */
    public static void assertThrows(Class<? extends Exception> expectedType, List<Runnable> runnables) {
        Objects.requireNonNull(expectedType, "expectedType");
        checkRunnables(runnables);
        try {
            JConUnitCore.concurrentExecute(runnables);
        } catch (Exception actualType) {
            if (expectedType == actualType.getClass()) {
                return;
            } else {
                throw new AssertionError("Expected " + expectedType +
                        ", but " + actualType.getClass() + " was thrown");
            }
        }
        throw new AssertionError("Expected " + expectedType + " to be thrown but nothing was thrown");
    }

    /**
     * Asserts that time of execution all of the elements (runnables) of parameter
     * {@code runnables} will not exceed than parameter {@code duration}.
     *
     * <p>If one or more exceptions were thrown by runnables of parameter
     * {@code runnables}, then one of them will be randomly thrown. If no exception
     * were thrown by any threads, then elapsed time for running runnable of all
     * threads will be compared to the parameter {@code duration}. If elapsed time
     * for running all runnables is more than parameter {@code duration}, an
     * {@link AssertionError} with a suitable message will be thrown.
     *
     * <p>Precision of timeout calculating is based on milliseconds.
     *
     * @param duration   reference timeout to check, not null
     * @param runnables list of {@link Runnable}, not null
     * @throws AssertionError       if actual elapsed time for running executable by
     *                              all the created threads was more than
     *                              parameter {@code duration}
     * @throws NullPointerException if parameter {@code duration} is null
     * @throws NullPointerException if parameter {@code runnables} is null
     * @throws NullPointerException if parameter {@code runnables} contains null
     */
    public static void assertTimeout(Duration duration, List<Runnable> runnables) {
        Objects.requireNonNull(duration, "duration");
        checkRunnables(runnables);
        Duration actualDuration = JConUnitCore.concurrentExecute(runnables);
        if (duration.compareTo(actualDuration) < 0) {
            throw new AssertionError("execution exceeded timeout of " + duration.toMillis()
                    + " ms by " + actualDuration.toMillis() + " ms");
        }
    }

    private static void checkRunnables(List<Runnable> runnables) {
        Objects.requireNonNull(runnables, "runnables");
        if (runnables.contains(null)) {
            throw new NullPointerException("runnables can not contain null");
        }
    }
}
