package com.graphai.channelBased.channels;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * - It uses a ReentrantLock
 * - It contains a LinkedList with from were to push and pop messages
 */
public class ReentrantLockChannel implements IChannel {
    private final List<Message> messageList = new LinkedList<>();
    private Lock internalLock;

    public ReentrantLockChannel(boolean fair) {
        this.internalLock = new ReentrantLock(fair);
    }

    @Override
    public List<Message> pushAll(List<Message> messages) {
        try {
            internalLock.lock();
            messageList.addAll(messages);
            return messages;
        } finally {
            internalLock.unlock();
        }
    }

    @Override
    public List<Message> pop(Predicate<Message> filterBy) {
        try {
            internalLock.lock();
            List<Message> popped = messageList.stream().filter(filterBy).collect(Collectors.toList());
            messageList.removeIf(filterBy);
            return popped;
        } finally {
            internalLock.unlock();
        }
    }

    @Override
    public boolean isObservable() {
        return false;
    }
}