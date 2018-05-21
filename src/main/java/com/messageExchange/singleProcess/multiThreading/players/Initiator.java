package com.messageExchange.singleProcess.multiThreading.players;

import com.messageExchange.messages.ByteMessage;
import com.messageExchange.singleProcess.IPlayer;
import com.messageExchange.singleProcess.multiThreading.GenericStrategy;

import java.io.IOException;

public class Initiator implements IPlayer {

    // static to support the single threaded implementation
    private static int pingsCount = 0;
    private static int repliesCount = 0;
    private int index;
    private GenericStrategy parent;

    public Initiator(GenericStrategy parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName().concat(" ").concat(parent.getChannel().getClass().getSimpleName());
    }

    @Override
    public int getBufferSize() {
        return parent.getBufferSize();
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
        return getPingsCount() == parent.getTotalMessages() && getRepliesCount() == parent.getTotalMessages();
    }

    @Override
    public boolean isVerbose() {
        return parent.isVerbose();
    }

    @Override
    public IPlayer call() throws IOException {
        while (true) {
            if(parent.getLock() != null)
                parent.getLock().lock();
            try {

                for (int i = 0; i < getBufferSize(); i++, index = nextIndex(index)) {

                    // Seek the next empty position and set a ping
                    if (pingsCount < parent.getTotalMessages()) {
                        if (parent.getChannel().get(index) == 0) {
                            parent.getChannel().put(index, ByteMessage.PING.getValue());
                            pingsCount++;
                            break;
                        }

                    }
                }

                for (int i = 0; i < getBufferSize(); i++, index = nextIndex(index)) {
                    // Seek the next reply
                    if (repliesCount < parent.getTotalMessages()) {
                        if (parent.getChannel().get(index) == ByteMessage.REPLY.getValue()) {
                            parent.getChannel().put(index, (byte)0);
                            repliesCount++;
                            break;
                        }
                    } else {
                        return this;
                    }
                }
            } finally {
                if (parent.getLock() != null)
                    parent.getLock().unlock();
            }
            if (hasFinished()) break;
            if (parent.getNumThreads() == 1) break;
        }

        return this;
    }
}
