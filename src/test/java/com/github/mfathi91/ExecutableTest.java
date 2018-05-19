package com.github.mfathi91;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ExecutableTest {

    private Runnable mockedRunnableSimple() {
        return mock(Runnable.class);
    }

    @Test
    void of1_runnable_null() {
        assertThrows(NullPointerException.class, () -> Executable.of(null, 1));
    }

    @Test
    void of1_numThreads_nonPositive() {
        assertThrows(IllegalArgumentException.class, () ->
                Executable.of(mockedRunnableSimple(), 0));
    }

    @Test
    void of1() {
        Executable.of(mockedRunnableSimple(), 10);
    }

    @Test
    void of2_runnables_null() {
        assertThrows(NullPointerException.class, () -> Executable.of(null));
    }

    @Test
    void of2_runnables_containsNull() {
        List<Runnable> runnables = Arrays.asList(mockedRunnableSimple(), null);
        assertThrows(NullPointerException.class, () -> Executable.of(runnables));
    }
    //--------------------------------------------------------------------

    @Test
    void getRunnables_of1() {
        Runnable runnable = mockedRunnableSimple();
        Executable executable = Executable.of(runnable, 1);
        assertEquals(1, executable.getRunnables().size());
        assertEquals(runnable, executable.getRunnables().get(0));
    }

    @Test
    void getRunnables_of2() {
        Runnable runnable1 = mockedRunnableSimple();
        Runnable runnable2 = mockedRunnableSimple();
        Executable executable = Executable.of(Arrays.asList(runnable1, runnable2));
        assertEquals(2, executable.getRunnables().size());
        assertTrue(executable.getRunnables().contains(runnable1));
        assertTrue(executable.getRunnables().contains(runnable2));
    }
}