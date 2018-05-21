package com.messageExchange.singleProcess.multiThreading.players;


import com.messageExchange.messages.ByteMessage;
import com.messageExchange.singleProcess.IPlayer;
import com.messageExchange.singleProcess.multiThreading.GenericStrategy;

public class Replier implements IPlayer {
    // Static to support the single threaded implementation where the call method is run over and over again
    private static int repliesCount = 0;
    private static int index = 0;
    private GenericStrategy parent;

    public Replier(GenericStrategy parent) {
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
        return 0;
    }

    @Override
    public int getRepliesCount() {
        return repliesCount;
    }

    @Override
    public boolean hasFinished() {
        return getRepliesCount() == parent.getTotalMessages();
    }

    @Override
    public boolean isVerbose() {
        return parent.isVerbose();
    }

    @Override
    public IPlayer call() throws Exception {
        while (true) {
            //if(isVerbose())
            //    System.out.println(getRepr());

            if (parent.getLock() != null)
                parent.getLock().lock();

            try {

                for (int i = 0 ; i < parent.getBufferSize(); i++, index = nextIndex(index)) {

                    // Seek the next reply
                    if (repliesCount < parent.getTotalMessages()) {
                        if (parent.getChannel().get(index) == ByteMessage.PING.getValue()) {
                            parent.getChannel().put(index, ByteMessage.REPLY.getValue());
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

