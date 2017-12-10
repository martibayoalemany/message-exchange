package com.graphai.singleProcess.multiThreading.runners;

import com.graphai.singleProcess.IPlayer;
import com.graphai.singleProcess.multiThreading.GenericStrategy;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.graphai.utils.StreamUtils.propagateExc;

public class SingleThreadedRunner  {

    private final GenericStrategy strategy;

    public SingleThreadedRunner(GenericStrategy strategy) {
        if (!strategy.isBuilt())
            throw new RuntimeException("Invalid strategy, isBuilt is false");
        if(strategy.getNumThreads() > 1)
            throw new RuntimeException("Invalid strategy, SingleThreadedRunner supports one thread");
        this.strategy = strategy;
    }

    public boolean execute() throws ExecutionException {
        return execute(Executors.newFixedThreadPool(strategy.getNumThreads()), strategy.getPlayers());
    }

    protected boolean execute(final ExecutorService executor, List<IPlayer> players) throws ExecutionException {
        try {
            // Single threading repeat executing the gets until hasFinished is true
            while (true) {
                List<Future<IPlayer>> futures = executor.invokeAll(players);
                if(futures.stream().map(s -> propagateExc(s::get)).allMatch(IPlayer::hasFinished))
                    return true;
            }
        } catch (InterruptedException ignored) {

        } finally {
            List<Runnable> runnableList = executor.shutdownNow();
            if (runnableList.size() > 0) {
                //noinspection ThrowFromFinallyBlock
                throw new RuntimeException("ExecutorService failed to shutdown the players properly");
            }
        }
        return false;
    }
}
