package com.graphai.multiProcess.fileBased;

import com.graphai.singleProcess.multiThreading.GenericStrategy;
import com.graphai.singleProcess.multiThreading.channels.FileBasedChannel;
import com.graphai.singleProcess.multiThreading.enums.Locking;
import com.graphai.singleProcess.multiThreading.enums.PlayerBehaviour;
import com.graphai.singleProcess.multiThreading.runners.SingleThreadedRunner;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class InitiatorProcess  {

    public static void main(String[] args) throws IOException, ExecutionException {
        if(args.length != 3)
            throw new IllegalArgumentException("args");
        Integer totalMessages = Integer.valueOf(args[0]);
        Integer bufferSize = Integer.valueOf(args[1]);
        Boolean verbose = Boolean.valueOf(args[2]);
        new InitiatorProcess(totalMessages, bufferSize, verbose);
    }

    private InitiatorProcess(Integer totalMessages, int bufferSize, boolean verbose) throws IOException, ExecutionException {
        GenericStrategy strategy = GenericStrategy.get()
                .totalMessages(totalMessages)
                .locking(Locking.None)
                .numThreads(1)
                .bufferSize(bufferSize)
                .verbose(verbose)
                .channel(new FileBasedChannel(bufferSize))
                .select(PlayerBehaviour.Initiator).verbose(verbose).build();
        new SingleThreadedRunner(strategy).execute();
    }

}
