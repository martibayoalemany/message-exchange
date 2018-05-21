package com.messageExchange.benchmarks;

import com.messageExchange.singleProcess.recursive.RecursiveRunner;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 8)
@Measurement(iterations = 8)
@Fork(1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FastestsBenchmarks {
    private static final int SMALL = 1_000;
    private static final int LARGE = 1_000_000_000;

    @Benchmark
    public void executeRecursive() throws Exception {
        new RecursiveRunner(SMALL).execute();
    }

    @Benchmark
    public void executeRecursiveLarge() throws Exception {
        new RecursiveRunner(LARGE).execute();
    }
}
