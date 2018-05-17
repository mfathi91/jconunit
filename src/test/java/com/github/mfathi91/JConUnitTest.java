package com.github.mfathi91;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JConUnitTest {

    private Runnable mockedRunnableSimple() {
        return mock(Runnable.class);
    }

    private void sleepMillis(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void concurrentExecute_executable_null() {
        assertThrows(NullPointerException.class, () ->
                JConUnit.concurrentExecute(null));
    }

    @Test
    void concurrentExecute_throwAnException() {
        Executable executable = Executable.of(() -> {
            throw new RuntimeException();
        }, 5);
        assertThrows(RuntimeException.class, () ->
                JConUnit.concurrentExecute(executable));
    }
    //--------------------------------------------------------------------

    @Test
    void assertTimeout_duration_null() {
        Executable executable = Executable.of(mockedRunnableSimple(), 1);
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertTimeout(null, executable));
    }

    @Test
    void assertTimeout_executable_null() {
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertTimeout(Duration.ofSeconds(1), null));
    }

    @Test
    void assertTimeout_violateTimeout() {
        Executable executable = Executable.of(() -> sleepMillis(100), 10);
        assertThrows(AssertionError.class, () ->
                JConUnit.assertTimeout(Duration.ofMillis(50), executable));
    }

    @Test
    void assertTimeout() {
        Executable executable = Executable.of(() -> sleepMillis(90), 50);
        JConUnit.assertTimeout(Duration.ofMillis(100), executable);
    }

}