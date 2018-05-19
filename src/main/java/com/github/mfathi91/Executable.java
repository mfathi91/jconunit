package com.github.mfathi91;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
    private final List<Runnable> runnables;

    /**
     * Returns an instance of {@link Executable}. This executable means that the
     * parameter {@code runnable} is intended to be run in {@code numThreads}
     * distinct threads.
     *
     * @param runnable   an instance of {@link Runnable}, not null
     * @param numThreads number of threads, greater or equal to 1
     * @throws NullPointerException     if parameter {@code runnable} is null
     * @throws IllegalArgumentException if parameter {@code numThreads} is less
     *                                  than 1
     */
    public static Executable of(Runnable runnable, int numThreads) {
        Objects.requireNonNull(runnable, "runnable");
        if (numThreads < 1) {
            throw new IllegalArgumentException(
                    "numThread cannot be nonpositive: " + numThreads);
        }
        List<Runnable> runnables = new ArrayList<>(numThreads);
        for (int i = 0; i < numThreads; i++) runnables.add(runnable);
        return new Executable(runnables);
    }

    /**
     * Constructor.
     *
     * @param runnables  list of all runnables that are intended to be run in
     *                   separated threads. Each runnable will be run in a distinct
     *                   thread
     * @throws NullPointerException     if parameter {@code runnable} is null
     * @throws IllegalArgumentException if parameter {@code numThreads} is less
     *                                  than 1
     */
    private Executable(List<Runnable> runnables) {
        Objects.requireNonNull(runnables, "runnable");
        runnables.removeIf(Objects::isNull);
        this.runnables = new ArrayList<>(runnables);
    }

    /**
     * Returns the {@code runnable} instance.
     */
    List<Runnable> getRunnables() {
        return runnables;
    }
}
