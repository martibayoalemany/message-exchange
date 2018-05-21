package com.messageExchange.singleProcess.multiThreading.players;

import com.messageExchange.messages.ByteMessage;
import com.messageExchange.singleProcess.IPlayer;
import com.messageExchange.singleProcess.multiThreading.GenericStrategy;

import java.util.concurrent.locks.Lock;

public class AtomicReplier implements IPlayer {
    private GenericStrategy parent;
    private int index = 0;
    private int repliesCount = 0;

    public AtomicReplier(GenericStrategy parent) {
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
        return repliesCount;
    }

    @Override
    public boolean hasFinished() {
        return repliesCount >= parent.getTotalMessages();
    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public boolean isVerbose() {
        return this.parent.isVerbose();
    }

    @Override
    public IPlayer call() throws Exception {
        while (true) {
            if(isVerbose())
                System.out.println(getRepr());
            Lock lock = this.parent.getLock();
            if(lock != null)
                lock.lock();
            try {
                for (int i = 0; i < parent.getBufferSize(); i++, index = nextIndex(index)) {
                    // Seek the next reply
                    if (!hasFinished()) {
                        if (parent.getChannel().compareAndSet(index, ByteMessage.PING.getValue(), ByteMessage.REPLY.getValue())) {
                            this.repliesCount++;
                            break;
                        }
                    } else {
                        return this;
                    }
                }
            }
            finally {
                if(lock != null)
                    lock.unlock();
            }

            if (parent.getNumThreads() == 1) break;
        }
        return this;
    }
}
