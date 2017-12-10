package com.graphai.singleProcess.multiThreading;

import com.graphai.singleProcess.IPlayer;

import java.util.concurrent.TimeUnit;

public interface ICustomChannel  {

    byte get(int index);

    void put(int index, byte value);

    int getBufferSize();

    default int nextIndex(int currentIndex) {
        return getBufferSize() == 0 ? -1 :
               getBufferSize() == 1 ? 0 : (currentIndex + 1) % getBufferSize();
    }

    IPlayer  createInitiator(GenericStrategy strategy);

    IPlayer  createReplier(GenericStrategy strategy);

    default void put(byte value) throws InterruptedException {
        put(0, value);
    }

    default Byte poll(long timeout, TimeUnit unit) throws InterruptedException {
        return -1;
    }

    default boolean compareAndSet(int i, int expect, int update) {
        return false;
    }

}