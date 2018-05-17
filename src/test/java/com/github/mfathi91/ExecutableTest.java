package com.github.mfathi91;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ExecutableTest {

    private Runnable mockedRunnableSimple() {
        return mock(Runnable.class);
    }

    @Test
    void of_runnable_null() {
        assertThrows(NullPointerException.class, () -> Executable.of(null, 1));
    }

    @Test
    void of_numThreads_nonPositive() {
        assertThrows(IllegalArgumentException.class, () ->
                Executable.of(mockedRunnableSimple(), 0));
    }

    @Test
    void of() {
        Executable.of(mockedRunnableSimple(), 10);
    }

    //--------------------------------------------------------------------

    @Test
    void getRunnable() {
        Runnable runnable = mockedRunnableSimple();
        Executable executable = Executable.of(runnable, 1);
        assertEquals(runnable, executable.getRunnable());
    }

    @Test
    void getNumThreads() {
        Executable executable = Executable.of(mockedRunnableSimple(), 61);
        assertEquals(61, executable.getNumThreads());
    }

}