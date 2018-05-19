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

    private void throwRuntimeException() {
        throw new RuntimeException();
    }

    private void throwIllegalArgException() {
        throw new IllegalArgumentException();
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
        Executable executable = Executable.of(this::throwRuntimeException, 5);
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
    //--------------------------------------------------------------------

    @Test
    void assertDoesNotThrowsException_executable_null() {
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertDoesNotThrowException(null));
    }

    @Test
    void assertDoesNotThrowsException_throwException() {
        Executable executable = Executable.of(this::throwRuntimeException, 5);
        assertThrows(RuntimeException.class, () ->
                JConUnit.assertDoesNotThrowException(executable));
    }

    @Test
    void assertDoesNotThrowsException() {
        JConUnit.assertDoesNotThrowException(Executable.of(() -> sleepMillis(2), 5));
    }
    //--------------------------------------------------------------------

    @Test
    void assertThrows_exception_null() {
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertThrows(null, Executable.of(mockedRunnableSimple(), 4)));
    }

    @Test
    void assertThrows_executable_null() {
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertThrows(NullPointerException.class, null));
    }

    @Test
    void assertThrows_differentExpectedException() {
        Executable executable = Executable.of(this::throwIllegalArgException, 5);
        assertThrows(AssertionError.class, () ->
                JConUnit.assertThrows(NullPointerException.class, executable));
    }

    @Test
    void assertThrows_noException() {
        Executable executable = Executable.of(() -> sleepMillis(1), 5);
        assertThrows(AssertionError.class, () ->
                JConUnit.assertThrows(NullPointerException.class, executable));
    }

    @Test
    void assertThrows_ok() {
        Executable executable = Executable.of(this::throwIllegalArgException, 5);
        JConUnit.assertThrows(IllegalArgumentException.class, executable);
    }
}