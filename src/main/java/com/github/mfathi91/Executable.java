package com.github.mfathi91;

import java.time.Duration;
import java.util.Objects;

/**
 * A data representer class. The main purpose of this class is to aggregate
 * separated fields that are used for concurrent execution. To use methods of
 * JConUnit class like {@link JConUnit#assertTimeout(Duration, Executable)},
 * creating an instance of this class is mandatory.
 *
 * <p>Instances of this class are immutable and threa-safe.
 */
public final class Executable {

    /**
     * Instance of runnable that is able to be executed.
     */
    private final Runnable runnable;

    /**
     * Number of threads to create for concurrent executing {@link #runnable}.
     * Setting this field is optional.
     */
    private final int numThreads;

    /**
     * Returns an instance of {@link Executable}.
     *
     * @param runnable   an instance of {@link Runnable}, not null
     * @param numThreads number of threads, greater or equal to 1
     * @throws NullPointerException     if parameter {@code runnable} is null
     * @throws IllegalArgumentException if parameter {@code numThreads} is less
     *                                  than 1
     */
    public static Executable of(Runnable runnable, int numThreads) {
        return new Executable(runnable, numThreads);
    }

    /**
     * Constructor.
     *
     * @param runnable   an instance of {@link Runnable}, not null
     * @param numThreads number of threads, greater or equal to 1
     * @throws NullPointerException     if parameter {@code runnable} is null
     * @throws IllegalArgumentException if parameter {@code numThreads} is less
     *                                  than 1
     */
    private Executable(Runnable runnable, int numThreads) {
        this.runnable = Objects.requireNonNull(runnable, "runnable");
        if (numThreads < 1) {
            throw new IllegalArgumentException(
                    "numThread cannot be nonpositive: " + numThreads);
        }
        this.numThreads = numThreads;
    }

    /**
     * Returns the {@code runnable} instance.
     */
    public Runnable getRunnable() {
        return runnable;
    }

    /**
     * Returns number of threads of this {@code Executable}.
     */
    public int getNumThreads() {
        return numThreads;
    }
}
