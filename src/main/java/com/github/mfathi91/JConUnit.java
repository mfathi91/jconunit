package com.github.mfathi91;

import java.time.Duration;
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
        JConUnitCore.concurrentExecute(executable.getRunnables());
    }

    /**
     * Asserts that execution of the parameter {@code executable} in multiple thread
     * does not any exception.
     *
     * <p>If one or more exceptions were thrown by one of the created threads during
     * running {@code runnable} of {@code executable}, one of the thrown exceptions
     * will be randonly thrown.
     *
     * <p>As JUnit notifies user about thrown exceptions, using this method seems
     * nonfunctional, but it provides a syntactical assertion and user can
     * immediately understand purpose of unit test even in the lack of documentation.
     *
     * @param executable instance of {@link Executable}, not null
     * @throws NullPointerException if parameter {@code executable} is null
     */
    public static void assertDoesNotThrowException(Executable executable) {
        concurrentExecute(executable);
    }

    /**
     * Asserts that execution of the parameter {@code executable} throws
     * an exception of the {@code expectedType} and returns the exception.
     *
     * <p>If no exception is thrown, or if an exception of a different type is
     * thrown, this method will fail.
     *
     * <p>If you do not want to perform additional checks on the exception instance,
     * simply ignore the return value.
     *
     * @throws AssertionError       if the thrown exception by {@code executable}
     *                              was different with {@code expectedType}
     * @throws AssertionError       if no exception was thrown by {@code executable}
     * @throws NullPointerException if parameter {@code expectedType} is null
     * @throws NullPointerException if parameter {@code executable} is null
     */
    public static void assertThrows(Class<? extends Exception> expectedType, Executable executable) {
        Objects.requireNonNull(expectedType, "expectedType");
        Objects.requireNonNull(executable, "executable");
        try {
            concurrentExecute(executable);
        } catch (Exception actualType) {
            if (expectedType == actualType.getClass()) {
                return;
            } else {
                throw new AssertionError("Expected " + expectedType +
                        ", but " + actualType.getClass() + " was thrown");
            }
        }
        throw new AssertionError("Expected " + executable + " to be thrown but nothing was thrown");
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
     * <p>Precision of timeout calculating is based on milliseconds.
     *
     * @param duration   reference timeout to check, not null
     * @param executable instance of {@link Executable}, not null
     * @throws AssertionError       if actual elapsed time for running executable by
     *                              all the created threads was more than
     *                              parameter {@code duration}
     * @throws NullPointerException if parameter {@code duration} is null
     * @throws NullPointerException if parameter {@code executable} is null
     */
    public static void assertTimeout(Duration duration, Executable executable) {
        Objects.requireNonNull(duration, "duration");
        Objects.requireNonNull(executable, "executable");
        Duration actualDuration = JConUnitCore.concurrentExecute(
                executable.getRunnables());
        if (duration.compareTo(actualDuration) < 0) {
            throw new AssertionError("execution exceeded timeout of " + duration.toMillis()
                    + " ms by " + actualDuration.toMillis() + " ms");
        }
    }
}
