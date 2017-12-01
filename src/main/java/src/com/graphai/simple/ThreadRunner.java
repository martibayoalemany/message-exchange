package com.graphai.simple;

import com.graphai.simple.entities.SimpleMessage;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ThreadRunner {
    private static final int PING_TOTAL = 100_000;
    private volatile List<SimpleMessage> channel = new LinkedList<>();
    private List<Thread> threads = new LinkedList<>();

    public static void main(String[] args) {
        Instant start = Instant.now();
        new ThreadRunner().execute();
        System.out.printf("Execute: %d ms ", Instant.now().toEpochMilli() - start.toEpochMilli());
    }

    private synchronized List<SimpleMessage> takeWhile(Predicate<SimpleMessage> filterBy) {
        List<SimpleMessage> messages = channel.stream().filter(filterBy).collect(Collectors.toList());
        channel.removeIf(filterBy);
        return messages;
    }

    private synchronized void put(SimpleMessage message) {
        channel.add(message);
    }

    void execute() {

        final UUID initiatorId = UUID.randomUUID();
        final UUID listenerId = UUID.randomUUID();

        Thread initiator = new Thread(new Runnable() {
            private int repliesCount = 0;

            @Override
            public void run() {
                Thread.currentThread().setName(String.valueOf(initiatorId));

                for (int i = 0; i < PING_TOTAL; i++)
                    put(SimpleMessage.ping(initiatorId, listenerId));

                while (repliesCount < PING_TOTAL) {
                    List<SimpleMessage> replies = takeWhile(
                            (message) -> message.getKind() == SimpleMessage.Kind.REPLY && message.getReceiver().equals(initiatorId));
                    repliesCount += replies.size();
                }
            }
        });

        Thread listener = new Thread(new Runnable() {
            private int repliedCount = 0;

            @Override
            public void run() {
                Thread.currentThread().setName(String.valueOf(listenerId));
                while (repliedCount < PING_TOTAL) {
                    List<SimpleMessage> replies = takeWhile(
                            (message) -> message.getKind() == SimpleMessage.Kind.PING && message.getReceiver().equals(listenerId));
                    for(SimpleMessage reply : replies)
                        put(SimpleMessage.reply(reply));
                    repliedCount = replies.size() + repliedCount;
                }
            }
        });

        threads.addAll(Arrays.asList(initiator, listener));
        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
