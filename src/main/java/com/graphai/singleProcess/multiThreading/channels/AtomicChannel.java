package com.graphai.singleProcess.multiThreading.channels;

import com.graphai.singleProcess.IPlayer;
import com.graphai.singleProcess.multiThreading.GenericStrategy;
import com.graphai.singleProcess.multiThreading.ICustomChannel;
import com.graphai.singleProcess.multiThreading.players.AtomicInitiator;
import com.graphai.singleProcess.multiThreading.players.AtomicReplier;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicChannel implements ICustomChannel {
    private volatile AtomicIntegerArray data;
    private int bufferSize;

    public AtomicChannel(int bufferSize) {
        this.data = new AtomicIntegerArray(bufferSize);
        this.bufferSize = bufferSize;
    }

    public boolean compareAndSet(int i, int expect, int update) {
        return data.compareAndSet( i,  expect, update);
    }

    @Override
    public byte get(int index) {
        return (byte)data.get(index);
    }

    @Override
    public void put(int index, byte value) {
        data.set(index, value);
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public IPlayer createInitiator(GenericStrategy strategy) {
        return new AtomicInitiator(strategy);
    }

    @Override
    public IPlayer createReplier(GenericStrategy strategy) {
        return new AtomicReplier(strategy);
    }
}
