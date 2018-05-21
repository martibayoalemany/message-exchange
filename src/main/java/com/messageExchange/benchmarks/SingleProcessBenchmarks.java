package com.messageExchange.benchmarks;

import com.messageExchange.singleProcess.multiThreading.GenericStrategy;
import com.messageExchange.singleProcess.multiThreading.channels.ArrayBasedChannel;
import com.messageExchange.singleProcess.multiThreading.channels.AtomicChannel;
import com.messageExchange.singleProcess.multiThreading.channels.DequeChannel;
import com.messageExchange.singleProcess.multiThreading.enums.Locking;
import com.messageExchange.singleProcess.multiThreading.runners.GenericRunner;
import com.messageExchange.singleProcess.recursive.RecursiveRunner;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 8)
@Measurement(iterations = 8)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SingleProcessBenchmarks {

    private static final int SMALL = 1_000;
    private static final int LARGE = 1_000_000;
    private static final int BUFFER = 20;


    @Benchmark
    public void executeFair() throws ExecutionException {
        new GenericRunner(getFair(SMALL).build()).execute();
    }

    private static GenericStrategy getFair(int totalMessages) {
        return GenericStrategy.get()
                .totalMessages(totalMessages)
                .numThreads(2)
                .channel(new ArrayBasedChannel(BUFFER))
                .locking(Locking.Fair)
                .bufferSize(1);
    }

    @Benchmark
    public void executeFairLarge() throws ExecutionException {
        new GenericRunner(getFair(LARGE).build()).execute();
    }

    @Benchmark
    public void executeFairDeque() throws ExecutionException {
        doExecuteFairDeque(SMALL);
    }

    private void doExecuteFairDeque(int totalMessages) throws ExecutionException {
        new GenericRunner(getFair(totalMessages).channel(new DequeChannel(BUFFER)).build()).execute();
    }

    @Benchmark
    public void executeFairDequeLarge() throws ExecutionException {
        doExecuteFairDeque(LARGE);
    }

    @Benchmark
    public void executeAtomic() throws ExecutionException {
        doExecuteAtomic(SMALL);
    }

    private void doExecuteAtomic(int totalMessages) throws ExecutionException {
        new GenericRunner(getFair(totalMessages).locking(Locking.None).channel(new AtomicChannel(BUFFER)).build()).execute();
    }

    @Benchmark
    public void executeAtomicLarge() throws ExecutionException {
        doExecuteAtomic(LARGE);
    }

    @Benchmark
    public void executeUnfair() throws ExecutionException {
        doExecuteUnfair(SMALL);
    }

    private void doExecuteUnfair(int totalMessages) throws ExecutionException {
        GenericStrategy strategy = GenericStrategy.get()
                .totalMessages(totalMessages)
                .numThreads(2)
                .locking(Locking.Unfair)
                .channel(new ArrayBasedChannel(BUFFER))
                .bufferSize(BUFFER).build();
        new GenericRunner(strategy).execute();
    }


    @Benchmark
    public void executeUnfairLarge() throws ExecutionException {
        doExecuteUnfair(LARGE);
    }

    @Benchmark
    public void executeUnfairUnbuffered() throws ExecutionException {
        doExecuteUnfair(SMALL);
    }

    private void doExecuteUnfairUnbuffered(int totalMessages) throws ExecutionException {
        GenericStrategy strategy = GenericStrategy.get()
                .totalMessages(totalMessages)
                .numThreads(2)
                .locking(Locking.Unfair)
                .channel(new ArrayBasedChannel(1))
                .bufferSize(1).build();
        new GenericRunner(strategy).execute();
    }

    @Benchmark
    public void executeUnfairLargeUnbuffered() throws ExecutionException {
        doExecuteUnfair(LARGE);
    }

    @Benchmark
    public void executeRecursive() throws Exception {
        new RecursiveRunner(SMALL).execute();
    }

    @Benchmark
    public void executeRecursiveLarge() throws Exception {
        new RecursiveRunner(LARGE).execute();
    }
}
