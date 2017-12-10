package com.graphai.singleProcess.multiThreading.runners;

import com.graphai.singleProcess.IPlayer;
import com.graphai.singleProcess.multiThreading.GenericStrategy;
import com.graphai.singleProcess.multiThreading.ICustomChannel;
import com.graphai.singleProcess.multiThreading.enums.PlayerBehaviour;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.graphai.utils.StreamUtils.propagateExc;

public class GenericRunner {

    private GenericStrategy strategy;

    public GenericRunner(GenericStrategy strategy) {
        if (!strategy.isBuilt())
            throw new RuntimeException("Invalid strategy, isBuilt is false");
        this.strategy = strategy;
    }

    public GenericRunner(ICustomChannel channel, int bufferSize, int totalMessages) {
        this(channel, bufferSize, totalMessages, PlayerBehaviour.Both);
    }

    private GenericRunner(ICustomChannel channel, int bufferSize, int totalMessages, PlayerBehaviour behaviour) {
        this.strategy = GenericStrategy.get()
                .bufferSize(bufferSize)
                .channel(channel)
                .numThreads(2)
                .totalMessages(totalMessages)
                .select(behaviour)
                .build();
    }

    /**
     * @return true if the runners have finished succesfully
     */
    public boolean execute() throws ExecutionException {
        return execute(Executors.newFixedThreadPool(strategy.getNumThreads()), strategy.getPlayers());
    }

    /**
     * @return true if the runners have finished succesfully
     */
    private boolean execute(final ExecutorService executor, List<IPlayer> players) throws ExecutionException {
        try {

            List<Future<IPlayer>> futures = executor.invokeAll(players);
            return futures.stream().map(s -> propagateExc(s::get)).allMatch(IPlayer::hasFinished);
        }
        catch (InterruptedException | NullPointerException e) {
            if(strategy.isVerbose()) {
                players.forEach(s -> System.out.println(s.getRepr()));
                System.out.println(e);
            }
        }
        finally {
            List<Runnable> runnableList = executor.shutdownNow();
            if (runnableList.size() > 0) {
                //noinspection ThrowFromFinallyBlock
                throw new RuntimeException("ExecutorService failed to shutdown the players properly");
            }
        }
        return false;
    }
}