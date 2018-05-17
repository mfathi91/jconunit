package com.github.mfathi91;

/**
 * This exception may be thrown by methods that have detected unknown concurrent
 * execution problems.
 *
 * <p>For example when a class is designed for concurrency, but the invariants are
 * violated in a multithreaded environment.
 */
public class UnknownConcurrentExecutionException extends RuntimeException {

    /**
     * Constructs a new {@code UnknownConcurrentExecutionException} with the specified
     * cause and a detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of {@code cause}.
     *
     * @param cause the cause of this UnknownConcurrentExecutionException
     */
    public UnknownConcurrentExecutionException(Throwable cause) {
        super(cause);
    }
}
