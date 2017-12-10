package com.graphai.benchmarks;

import com.graphai.multiProcess.fileBased.ProcessRunner;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 8)
@Measurement(iterations = 8)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MultiProcessBenchmark {

    private static final int SMALL = 1_000;
    private static final int LARGE = 100_000;

    @Benchmark
    public void executeProcessBuffered() {
        new ProcessRunner().execute(SMALL, 20, false);
    }

    @Benchmark
    public void executeProcessBufferedLarge() {
        new ProcessRunner().execute(LARGE, 20, false);
    }
}
