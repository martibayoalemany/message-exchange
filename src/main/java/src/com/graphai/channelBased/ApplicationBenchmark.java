package com.graphai.channelBased;


import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(org.openjdk.jmh.annotations.Mode.SingleShotTime)
@Warmup(iterations = 8)
@Measurement(iterations = 8)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ApplicationBenchmark {
    @State(Scope.Thread)
    public static class MyState {

        @Setup(Level.Trial)
        public void doSetup() {
            Logger.LOGGER.instance.setEnabled(false);
        }
    }

    @Benchmark
    public void fairChannel() {
        new Application(Channels.get(true)).execute();
    }

    @Benchmark
    public void unfairChannel() {
        new Application(Channels.get(false)).execute();
    }

    @Benchmark
    public void synchronizedChannel() {
        new Application(Channels.getSynchronized()).execute();
    }

    @Benchmark
    public void singleThreaded() {
        new Application(Channels.getSynchronized()).executeSingleThreaded();
    }

    @Benchmark
    public void fairChannelOneShot() {
        new Application(Channels.get(true), true).execute();
    }

    @Benchmark
    public void unfairChannelOneShot() {
        new Application(Channels.get(false), true).execute();
    }

    @Benchmark
    public void synchronizedChannelOneShot() {
        new Application(Channels.getSynchronized(), true).execute();
    }

    @Benchmark
    public void singleThreadedOneShot() {
        new Application(Channels.getSynchronized(), true).executeSingleThreaded();
    }

//    @Benchmark
//    public void synchronizedObservable() {
//        new Application(Channels.getObservable()).execute();
//    }
//
//    @Benchmark
//    public void singleThreadedObservable() {
//        new Application(Channels.getObservable()).executeSingleThreaded();
//    }
//
//    @Benchmark
//    public void synchronizedObservableOneShot() {
//        new Application(Channels.getObservable(), true).execute();
//    }
//
//    @Benchmark
//    public void singleThreadedObservableOneShot() {
//        new Application(Channels.getObservable(),true).executeSingleThreaded();
//    }

}
