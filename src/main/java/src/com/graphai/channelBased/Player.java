package com.graphai.channelBased;

import com.graphai.channelBased.channels.IChannel;
import com.graphai.channelBased.channels.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * - A player which sends pings and waits for replies
 * - This behaviour stops after the condition given as predicate at instantiation time becomes true
 * - It can be used as a Runnable in a thread or  using the methods mainLoop and isFinished
 */
public class Player implements Runnable, Serializable {
    private final UUID id;
    private final IChannel channel;
    private final Predicate<Player> condition;
    private final String name;
    private final State state;
    private final boolean sentOnePacketPerLoop;
    private int pingsToSend;

    public UUID getId() {
        return id;
    }

    int getRepliesReceivedCount() {
        return state.repliesReceivedCount;
    }

    int getPingsReceivedCount() {
        return state.pingsReceivedCount;
    }

    int getRepliesSentCount() {
        return state.repliesSentCount;
    }

    private class State {
        private int pingsSentCount;
        private int pingsReceivedCount;
        private int repliesSentCount;
        private int repliesReceivedCount;

        @Override
        public String toString() {
            return "State{" +
                    "pingsSentCount=" + pingsSentCount +
                    ", pingsReceivedCount=" + pingsReceivedCount +
                    ", repliesSentCount=" + repliesSentCount +
                    ", repliesReceivedCount=" + repliesReceivedCount +
                    '}';
        }
    }

    static Player pingAndStopAfter(int pingsToSend, boolean sentOnePacketPerLoop, Predicate<Player> condition, IChannel channel) {
        return new Player("pingAndStopAfter", channel, pingsToSend, sentOnePacketPerLoop, condition);
    }

    static Player stopAfter(Predicate<Player> condition, IChannel channel) {
        return new Player("stopAfter", channel, 0, false, condition);
    }

    private Player(String name, IChannel channel, int pingsToSend, boolean sentOnePacketPerLoop, Predicate<Player> condition) {
        this.name = name;
        this.pingsToSend = pingsToSend;
        this.sentOnePacketPerLoop = sentOnePacketPerLoop;
        this.condition = condition;
        this.id = UUID.randomUUID();
        this.channel = channel;
        this.state = new State();
        if(channel.isObservable()) {
            channel.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    if(this == arg)
                        return;
                    Player.this.consume();
                }
            });
        }
    }

    @Override
    public void run() {
        Thread.currentThread().setName(this.name + " " + this.toString());
        Logger.get().log("Started -> %s", Thread.currentThread().getName().trim());

        while (!isFinished()) {
            mainLoop();
        }

        Logger.get().log("Run finished %s ", Thread.currentThread().getName());
    }

    boolean isFinished() {
        return this.condition.test(this);
    }

    void mainLoop() {
        // Send pings
        if (this.state.pingsSentCount < pingsToSend) {
            Stream<Message> pingGenerator = Stream.generate(() -> Message.ping(this, Message.BROADCAST_ID));
            List<Message> sent = sentOnePacketPerLoop ?
                    channel.push(pingGenerator.findFirst().get()) :
                    channel.pushAll(pingGenerator.limit(pingsToSend).collect(Collectors.toList()));

            if (sent != null)
                this.state.pingsSentCount = this.state.pingsSentCount + sent.size();
        }
        consume();
    }

    private void consume() {
        // Receive pings
        List<Message> pingsReceived = channel.pop(message ->
                // The packet is for this player or is a broadcast
                (message.getReceiver() == id || message.getReceiver() == Message.BROADCAST_ID) &&
                        message.getSender() != id &&
                        message.getKind() == Message.Kind.PING);

        // Respond to pings
        List<Message> repliesSent = pingsReceived.stream().collect(ArrayList::new,
                (messages, message) -> messages.addAll(channel.push(Message.reply(this, message))),
                List::addAll
        );
        this.state.pingsReceivedCount = this.state.pingsReceivedCount + pingsReceived.size();
        this.state.repliesSentCount = this.state.repliesSentCount + repliesSent.size();

        // Process replies to pings
        List<Message> repliesReceived = channel.pop(message ->
                // The packet is for this player or is a broadcast
                message.getReceiver() == id && message.getKind() == Message.Kind.REPLY);

        this.state.repliesReceivedCount = this.state.repliesReceivedCount + repliesReceived.size();

        Logger.get().log(this.toString());
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                "}  " + this.state.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
