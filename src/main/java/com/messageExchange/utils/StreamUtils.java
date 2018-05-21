package com.messageExchange.utils;

import java.util.concurrent.Callable;

/**
 * Utils to handle exceptions in java 8 stream scenarios
 */
public class StreamUtils {
    private static RuntimeException runtime(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException)e;
        }

        return new RuntimeException(e);
    }

    public static <V> V propagateExc(Callable<V> callable){
        try {
            return callable.call();
        } catch (Exception e) {
            throw runtime(e);
        }
    }
}
