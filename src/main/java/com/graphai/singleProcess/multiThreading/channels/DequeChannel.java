package com.graphai.singleProcess.multiThreading.channels;

import com.graphai.singleProcess.IPlayer;
import com.graphai.singleProcess.multiThreading.GenericStrategy;
import com.graphai.singleProcess.multiThreading.ICustomChannel;
import com.graphai.singleProcess.multiThreading.players.DequeInitiator;
import com.graphai.singleProcess.multiThreading.players.DequeReplier;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DequeChannel implements ICustomChannel {
    private final BlockingQueue<Byte> queue = new LinkedBlockingQueue<>();
    private int bufferSize;

    public DequeChannel(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public byte get(int index) {
        try {
            return poll(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {

        }
        return -1;
    }

    @Override
    public void put(int index, byte value) {
        try {
            put(value);
        } catch (InterruptedException ignored) {

        }
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public IPlayer createInitiator(GenericStrategy strategy) {
        return new DequeInitiator(strategy);
    }

    @Override
    public IPlayer createReplier(GenericStrategy strategy) {
        return new DequeReplier(strategy);
    }

    @Override
    public void put(byte value) throws InterruptedException {
        queue.put(value);
    }

    @Override
    public Byte poll(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }
}
