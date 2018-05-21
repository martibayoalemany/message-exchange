package com.messageExchange.singleProcess;


import java.util.concurrent.Callable;

public interface IPlayer extends  Callable<IPlayer> {

    String getName();

    int getPingsCount();

    int getRepliesCount();

    boolean hasFinished();

    int getBufferSize();

    // region defaults

    default boolean isVerbose() {
        return false;
    }

    @Override
    default IPlayer call() throws Exception {
        if(isVerbose()) {
            System.out.println(getRepr());
        }

        return this;
    }

    default String getRepr() {
        return String.format("\n%20s >>> Pings : %3d Replies: %3d",
                getName(), getPingsCount(), getRepliesCount());
    }

    default int nextIndex(int currentIndex) { return getBufferSize() == 1 ? 0 : (currentIndex + 1) % (getBufferSize() - 1);}
    // end region
}
