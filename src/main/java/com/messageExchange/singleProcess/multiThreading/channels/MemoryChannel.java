package com.messageExchange.singleProcess.multiThreading.channels;

import com.messageExchange.singleProcess.IPlayer;
import com.messageExchange.singleProcess.multiThreading.GenericStrategy;
import com.messageExchange.singleProcess.multiThreading.ICustomChannel;
import com.messageExchange.singleProcess.multiThreading.players.Initiator;
import com.messageExchange.singleProcess.multiThreading.players.Replier;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

public class MemoryChannel implements ICustomChannel {
    private  FileChannel file;
    private  MappedByteBuffer mem;
    private int bufferSize;

    public MemoryChannel(int buffer)  {
        try {
            bufferSize = buffer;
            file = new RandomAccessFile(Paths.get("target/runtime.txt").toFile(), "rw").getChannel();
            mem = file.map(FileChannel.MapMode.READ_WRITE, 0, buffer);
            for (int i = 0; i < buffer; i++) {
                mem.put(i, (byte)0);
            }
        } catch (IOException ignore) {

        }
    }
    @Override
    public byte get(int index) {
        return mem.get(index);
    }

    @Override
    public void put(int index, byte value) {
        mem.put(index,value);
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
