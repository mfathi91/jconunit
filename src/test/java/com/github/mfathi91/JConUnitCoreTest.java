package com.github.mfathi91;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JConUnitCoreTest {

    private Runnable mockedRunnableSimple() {
        return mock(Runnable.class);
    }

    @Test
    void concurrentExecute_task_null() {
        assertThrows(NullPointerException.class, () ->
                JConUnitCore.concurrentExecute(null, 1));
    }

    @Test
    void concurrentExecute_numThreads_nonpositive() {
        assertThrows(IllegalArgumentException.class, () ->
                JConUnitCore.concurrentExecute(mockedRunnableSimple(), 0));
    }

    @Test
    void concurrentExecute_throwAnException() {
        Runnable task = () -> {
            throw new IllegalArgumentException();
        };
        assertThrows(IllegalArgumentException.class, () ->
                JConUnitCore.concurrentExecute(task, 10));
    }

    @Test
    void concurrentExecute_noException() {
        Runnable task = () -> {};
        JConUnitCore.concurrentExecute(task, 10);
    }

}