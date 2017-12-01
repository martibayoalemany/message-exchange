package com.graphai.channelBased;

import com.graphai.channelBased.channels.IChannel;
import com.graphai.channelBased.channels.IPCChannel;
import com.graphai.channelBased.channels.ProcessChannel;
import com.graphai.channelBased.channels.Message;
import com.graphai.channelBased.channels.ObservableChannel;
import com.graphai.channelBased.channels.ReentrantLockChannel;
import com.graphai.channelBased.channels.SynchronizedChannel;

import java.util.List;
import java.util.Observable;
import java.util.function.Predicate;

/**
 * - It holds several singletons with different synchronization primitives.
 */
public class Channels extends Observable implements IChannel {
    private IChannel impl;

    @Override
    public List<Message> push(Message reply) {
        return impl.push(reply);
    }

    @Override
    public List<Message> pushAll(List<Message> messages) {
        return impl.pushAll(messages);
    }

    @Override
    public List<Message> pop(Predicate<Message> filterBy) {
        return impl.pop(filterBy);
    }

    @Override
    public boolean isObservable() {
        return impl.isObservable();
    }

    /// Singleton "on demand holder pattern"
    private static class Singleton {
        final static IChannel instance = new ReentrantLockChannel(false);
        final static IChannel fairInstance = new ReentrantLockChannel(true);
        final static IChannel synchronizedInstance = new SynchronizedChannel();
        final static IChannel observableInstance = new ObservableChannel();
        final static IChannel ipcInstance = new ProcessChannel();
    }

    public static synchronized IChannel get(boolean fair) {
        return fair ? Singleton.fairInstance : Singleton.instance;
    }

    public static synchronized IChannel getSynchronized() {
        return Singleton.synchronizedInstance;
    }

    public static synchronized IChannel getObservable() {
        return Singleton.observableInstance;
    }

    static synchronized IPCChannel getIpc() {
        return (IPCChannel)Singleton.ipcInstance;
    }


    private Channels(IChannel impl) {
        this.impl = impl;
    }

}
