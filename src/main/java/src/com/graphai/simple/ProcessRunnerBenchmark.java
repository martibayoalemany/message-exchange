package com.graphai.simple;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(org.openjdk.jmh.annotations.Mode.SingleShotTime)
@Warmup(iterations = 8)
@Measurement(iterations = 8)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Deprecated
public class ProcessRunnerBenchmark {

    @Benchmark
    public void execute() {
        new ProcessRunner().execute();
    }
}
