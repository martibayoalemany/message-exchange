package com.messageExchange.singleProcess.multiThreading.channels;

import com.messageExchange.singleProcess.IPlayer;
import com.messageExchange.singleProcess.multiThreading.GenericStrategy;
import com.messageExchange.singleProcess.multiThreading.ICustomChannel;
import com.messageExchange.singleProcess.multiThreading.players.Initiator;
import com.messageExchange.singleProcess.multiThreading.players.Replier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;

public class FileBasedChannel implements ICustomChannel {

    private RandomAccessFile file;
    private int bufferSize;

    public FileBasedChannel(int bufferSize) {
        try {
            file = new RandomAccessFile(Paths.get("target/runtime.txt").toFile(), "rw");
            this.bufferSize = bufferSize;
        } catch (FileNotFoundException ignore) {

        }
    }

    @Override
    public byte get(int index) {
        try {
            byte[] b = new byte[1];
            file.seek(index);
            if (file.read(b, 0, 1) == 1) {
                return b[0];
            }
        }
        catch (IOException ignore) {

        }
        return -1;
    }

    @Override
    public void put(int index, byte value) {

        try {
            file.seek(index);
            byte[] tmp = {value};
            file.write(tmp, 0, 1);
        } catch (IOException ignore) {

        }
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
