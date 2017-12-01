package com.graphai.channelBased.channels;

import com.graphai.channelBased.Player;
import com.graphai.channelBased.channels.Message;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;
import java.util.function.Predicate;

/**
 * It allows to push and pop messages
 */
public interface IChannel {

    default List<Message> push(Message reply) {
        return pushAll(Collections.singletonList(reply));
    }

    List<Message> pushAll(List<Message> messages);

    List<Message> pop(Predicate<Message> filterBy);

    default boolean isObservable() { return true; };

    default boolean hasIpcSupport() { return false; };

    final LinkedList<Observer> observers = new LinkedList<>();

    default void addObserver(Observer o) {
        observers.add(o);
    }

    default void notifyObservers(Player sender) {
        for (Observer o : observers)
            o.update(null, sender);
    }

}
