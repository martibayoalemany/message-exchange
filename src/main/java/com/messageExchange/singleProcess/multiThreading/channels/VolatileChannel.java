package com.messageExchange.singleProcess.multiThreading.channels;

import com.messageExchange.singleProcess.IPlayer;
import com.messageExchange.singleProcess.multiThreading.GenericStrategy;
import com.messageExchange.singleProcess.multiThreading.ICustomChannel;
import com.messageExchange.singleProcess.multiThreading.players.Initiator;
import com.messageExchange.singleProcess.multiThreading.players.Replier;
import com.messageExchange.utils.UnsafeChannel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

/**
 * This implementation is still not working, reflection is failing to access a "private native" method
 */
@Deprecated
public class VolatileChannel implements ICustomChannel {

    private final FileChannel file;
    private final MappedByteBuffer mem;
    private final UnsafeChannel unsafeChannel;
    private final int bufferSize;

    VolatileChannel(int buffer) throws IOException {

        bufferSize = buffer;
        file = new RandomAccessFile(Paths.get("target/runtime.txt").toFile(), "rw").getChannel();
        mem = file.map(FileChannel.MapMode.READ_WRITE, 0, buffer);
        for (int i = 0; i < buffer; i++) {
            mem.put(i, (byte) 0);
        }
        unsafeChannel = UnsafeChannel.of(file);
        unsafeChannel.setAddressUnsafe(buffer);

    }
    @Override
    public byte get(int index) {
        return unsafeChannel.getByteVolatile(index);
    }

    @Override
    public void put(int index, byte value) {
        unsafeChannel.putByteVolatile(index, value);
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
