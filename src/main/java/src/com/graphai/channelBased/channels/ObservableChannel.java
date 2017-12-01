package com.graphai.channelBased.channels;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * TODO: to be finished
 */
@Deprecated
public class ObservableChannel implements IChannel {
    private final List<Message> messageList = new LinkedList<>();

    @Override
    public synchronized List<Message> pushAll(List<Message> messages) {
        messageList.addAll(messages);
        Optional<Message> msg = messages.stream().findFirst();
        this.notifyObservers(msg.isPresent() ? msg.get().getSenderPlayer() : null);
        return messages;
    }

    @Override
    public synchronized List<Message> pop(Predicate<Message> filterBy) {
        List<Message> popped = messageList.stream().filter(filterBy).collect(Collectors.toList());
        messageList.removeIf(filterBy);
        return popped;
    }
}
