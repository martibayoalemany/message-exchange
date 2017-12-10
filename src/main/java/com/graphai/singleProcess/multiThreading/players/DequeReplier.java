package com.graphai.singleProcess.multiThreading.players;

import com.graphai.messages.ByteMessage;
import com.graphai.singleProcess.IPlayer;
import com.graphai.singleProcess.multiThreading.GenericStrategy;

import java.util.concurrent.TimeUnit;

public class DequeReplier implements IPlayer {
    private GenericStrategy parent;
    private int repliesCount;

    public DequeReplier(GenericStrategy parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName().concat(" ").concat(parent.getChannel().getClass().getSimpleName());
    }

    @Override
    public int getPingsCount() {
        return 0;
    }

    @Override
    public int getRepliesCount() {
        return this.repliesCount;
    }

    @Override
    public boolean hasFinished() {
        return getRepliesCount() >= parent.getTotalMessages();
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
                // We need to use a lock, it seems the blocking queue does not perform properlie
                parent.getLock().lock();
                try {
                    Byte message;
                    if((message = parent.getChannel().poll(10, TimeUnit.MILLISECONDS)) != null) {
                        if (message == ByteMessage.PING.getValue()) {
                            this.repliesCount++;
                            parent.getChannel().put(ByteMessage.REPLY.getValue());
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
