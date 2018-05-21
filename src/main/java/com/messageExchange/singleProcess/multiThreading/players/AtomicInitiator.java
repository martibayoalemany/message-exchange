package com.messageExchange.singleProcess.multiThreading.players;

import com.messageExchange.messages.ByteMessage;
import com.messageExchange.singleProcess.IPlayer;
import com.messageExchange.singleProcess.multiThreading.GenericStrategy;

import java.util.concurrent.locks.Lock;

public class AtomicInitiator implements IPlayer {
    private GenericStrategy parent;
    private int repliesCount;
    private int pingsCount;
    private int index;

    public AtomicInitiator(GenericStrategy parent) {
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
        return getRepliesCount() >= repliesCount && getPingsCount() >= pingsCount ;
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
            Lock lock = parent.getLock();
            if(lock != null)
                lock.lock();
            try {
                for (int i = 0; i < parent.getBufferSize(); i++, index = nextIndex(index)) {
                    // Seek the next empty position and set a ping
                    if (pingsCount < parent.getTotalMessages()) {
                        if (parent.getChannel().compareAndSet(index, 0, ByteMessage.PING.getValue())) {
                            pingsCount++;
                            break;
                        }
                    }
                }

                for (int i = 0; i < parent.getBufferSize(); i++, index = nextIndex(index)) {
                    // Seek the next reply
                    if (repliesCount < parent.getTotalMessages()) {
                        if (parent.getChannel().compareAndSet(index, ByteMessage.REPLY.getValue(), 0)) {
                            repliesCount++;
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
