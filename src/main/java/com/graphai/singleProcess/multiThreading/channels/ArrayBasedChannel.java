package com.graphai.singleProcess.multiThreading.channels;

import com.graphai.singleProcess.IPlayer;
import com.graphai.singleProcess.multiThreading.GenericStrategy;
import com.graphai.singleProcess.multiThreading.ICustomChannel;
import com.graphai.singleProcess.multiThreading.players.Initiator;
import com.graphai.singleProcess.multiThreading.players.Replier;

public class ArrayBasedChannel implements ICustomChannel {
    private final int bufferSize;
    private volatile byte[] array;

    public ArrayBasedChannel(int bufferSize) {
        this.bufferSize = bufferSize;
        array = new byte[bufferSize];
    }

    @Override
    public byte get(int index) {
        return array[index];
    }

    @Override
    public void put(int index, byte value) {
        array[index] = value;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public IPlayer createInitiator(GenericStrategy strategy) {
        return new Initiator(strategy);
    }

    @Override
    public IPlayer createReplier(GenericStrategy strategy) {
        return new Replier(strategy);
    }
}
