package com.graphai.singleProcess.multiThreading.players;

import com.graphai.messages.ByteMessage;
import com.graphai.singleProcess.IPlayer;
import com.graphai.singleProcess.multiThreading.GenericStrategy;

import java.util.concurrent.TimeUnit;

public class DequeInitiator implements IPlayer {
    private GenericStrategy parent;
    private int pingsCount;
    private int repliesCount;

    public DequeInitiator(GenericStrategy parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName().concat(" ").concat(parent.getChannel().getClass().getSimpleName());
    }

    @Override
    public int getPingsCount() {
        return pingsCount;
    }

    @Override
    public int getRepliesCount() {
        return repliesCount;
    }

    @Override
    public boolean hasFinished() {
        return pingsCount >= parent.getTotalMessages();
    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public boolean isVerbose() {
        return parent.isVerbose();
    }

    @Override
    public IPlayer call() throws Exception {
        try {
            while (true) {
                if(isVerbose())
                    System.out.println(getRepr());

                // It sends pings until pingsCount reaches pingsTotal
                if (pingsCount < parent.getTotalMessages()) {
                    parent.getChannel().put(ByteMessage.PING.getValue());
                    pingsCount++;
                }

                // We need to use a lock here eventhough poll is a blocking call
                parent.getLock().lock();
                try {
                    Byte message;
                    if((message = parent.getChannel().poll(10, TimeUnit.MILLISECONDS)) != null) {
                        if (message == ByteMessage.REPLY.getValue()) {
                            this.repliesCount++;
                        } else {
                            parent.getChannel().put(message);
                        }
                    }

                    if (hasFinished()) break;
                    if (parent.getNumThreads() == 1) break;
                }
                finally {
                    parent.getLock().unlock();
                }
            }
        } catch (InterruptedException | IllegalMonitorStateException e) {
            e.printStackTrace();
        }
        return this;
    }
}
