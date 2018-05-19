package com.github.mfathi91;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class JConUnitCoreTest {

    private Runnable mockedRunnableSimple() {
        return mock(Runnable.class);
    }

    @Test
    void concurrentExecute_task_null() {
        assertThrows(NullPointerException.class, () ->
                JConUnitCore.concurrentExecute(null));
    }

    @Test
    void concurrentExecute_executable_containsNull() {
        List<Runnable> runnables = new ArrayList<>();
        runnables.addAll(Arrays.asList(mockedRunnableSimple(), null, mockedRunnableSimple()));
        assertThrows(NullPointerException.class, () ->
                JConUnitCore.concurrentExecute(runnables));
    }

    @Test
    void concurrentExecute_throwAnException() {
        Runnable task = () -> {
            throw new IllegalArgumentException();
        };
        List<Runnable> runnables = Collections.nCopies(10, task);
        assertThrows(IllegalArgumentException.class, () ->
                JConUnitCore.concurrentExecute(runnables));
    }

    @Test
    void concurrentExecute_noException() {
        Runnable task = () -> {
        };
        JConUnitCore.concurrentExecute(Collections.nCopies(10, task));
        List<Runnable> runnables = Arrays.asList(this::run1, this::run2);
    }

    public void run1(){

    }

    public void run2(){}

}