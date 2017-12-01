package com.graphai.channelBased.channels;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * - It the synchronized keyword
 * - It contains a LinkedList with from were to push and pop messages
 */
public class SynchronizedChannel implements IChannel {
    private final List<Message> messageList = new LinkedList<>();

    @Override
    public synchronized List<Message> pushAll(List<Message> messages) {
        messageList.addAll(messages);
        return messages;
    }

    @Override
    public synchronized List<Message> pop(Predicate<Message> filterBy) {
        List<Message> popped = messageList.stream().filter(filterBy).collect(Collectors.toList());
        messageList.removeIf(filterBy);
        return popped;
    }

    @Override
    public boolean isObservable() {
        return false;
    }
}

