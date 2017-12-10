package com.graphai.multiProcess.socketBased;

import com.graphai.singleProcess.multiThreading.GenericStrategy;
import com.graphai.singleProcess.multiThreading.channels.FileBasedChannel;
import com.graphai.singleProcess.multiThreading.enums.Locking;
import com.graphai.singleProcess.multiThreading.enums.PlayerBehaviour;
import com.graphai.singleProcess.multiThreading.runners.SingleThreadedRunner;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ReplierProcess {

    public static void main(String[] args) throws IOException, ExecutionException {
        if (args.length != 3)
            throw new IllegalArgumentException("args");
        Integer totalMessages = Integer.valueOf(args[0]);
        Integer bufferSize = Integer.valueOf(args[1]);
        Boolean verbose = Boolean.valueOf(args[2]);
        new ReplierProcess(totalMessages, bufferSize, verbose);
    }

    private ReplierProcess(Integer totalMessages, int bufferSize, boolean verbose) throws IOException, ExecutionException {
        GenericStrategy strategy = GenericStrategy.get()
                .totalMessages(totalMessages)
                .locking(Locking.None)
                .numThreads(1)
                .bufferSize(bufferSize)
                .verbose(verbose)
                .channel(new FileBasedChannel(bufferSize))
                .select(PlayerBehaviour.Replier).verbose(true).build();
        new SingleThreadedRunner(strategy).execute();
    }

}
