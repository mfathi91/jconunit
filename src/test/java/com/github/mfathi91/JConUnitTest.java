package com.github.mfathi91;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JConUnitTest {

    // A few helper methods
    private Runnable mockedRunnable() {
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

    private List<Runnable> listOf(Runnable... runnables) {
        return Arrays.asList(runnables);
    }
    //--------------------------------------------------------------------

    @Test
    void assertTimeout_duration_null() {
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertTimeout(null, listOf(mockedRunnable())));
    }

    @Test
    void assertTimeout_runnables_null() {
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertTimeout(Duration.ofSeconds(1), null));
    }

    @Test
    void assertTimeout_runnables_containNull() {
        List<Runnable> runnables = listOf(mockedRunnable(), null, mockedRunnable());
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertTimeout(Duration.ofSeconds(1), runnables));
    }

    @Test
    void assertTimeout_violateTimeout() {
        List<Runnable> runnables = Collections.nCopies(10, () -> sleepMillis(10));
        assertThrows(AssertionError.class, () ->
                JConUnit.assertTimeout(Duration.ofMillis(5), runnables));
    }

    @Test
    void assertTimeout() {
        List<Runnable> runnables = Collections.nCopies(50, () -> sleepMillis(5));
        JConUnit.assertTimeout(Duration.ofMillis(100), runnables);
    }
    //--------------------------------------------------------------------

    @Test
    void assertDoesNotThrowsException_runnables_null() {
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertDoesNotThrowException(null));
    }

    @Test
    void assertDoesNotThrowsException_runnables_containsNull() {
        List<Runnable> runnables = listOf(mockedRunnable(), null, mockedRunnable());
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertDoesNotThrowException(runnables));
    }

    @Test
    void assertDoesNotThrowsException_throwException() {
        List<Runnable> runnables = Collections.nCopies(5, this::throwRuntimeException);
        assertThrows(RuntimeException.class, () ->
                JConUnit.assertDoesNotThrowException(runnables));
    }

    @Test
    void assertDoesNotThrowsException() {
        JConUnit.assertDoesNotThrowException(listOf(() -> sleepMillis(2)));
    }
    //--------------------------------------------------------------------

    @Test
    void assertThrows_exception_null() {
        List<Runnable> runnables = Collections.nCopies(4, mockedRunnable());
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertThrows(null, runnables));
    }

    @Test
    void assertThrows_runnables_null() {
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertThrows(NullPointerException.class, null));
    }

    @Test
    void assertThrows_runnables_containsNull() {
        List<Runnable> runnables = listOf(mockedRunnable(), null, mockedRunnable());
        assertThrows(NullPointerException.class, () ->
                JConUnit.assertThrows(RuntimeException.class, runnables));
    }

    @Test
    void assertThrows_differentExpectedException() {
        List<Runnable> runnables = Collections.nCopies(5, this::throwIllegalArgException);
        assertThrows(AssertionError.class, () ->
                JConUnit.assertThrows(NullPointerException.class, runnables));
    }

    @Test
    void assertThrows_noException() {
        List<Runnable> runnables = Collections.nCopies(5, () -> sleepMillis(1));
        assertThrows(AssertionError.class, () ->
                JConUnit.assertThrows(NullPointerException.class, runnables));
    }

    @Test
    void assertThrows_ok() {
        List<Runnable> runnables = Collections.nCopies(5, this::throwIllegalArgException);
        JConUnit.assertThrows(IllegalArgumentException.class, runnables);
    }
}