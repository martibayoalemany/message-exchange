package com.graphai.channelBased.channels;

import com.graphai.channelBased.Player;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable{
    private final Player senderPlayer;
    private final Kind kind;
    private final UUID receiver;
    private final UUID sender;
    private final UUID id;
    private final Message original;

    public static final UUID BROADCAST_ID = UUID.randomUUID();

    public static Message ping(UUID receiver) {
        return new Message(null, Kind.PING, receiver, null);
    }

    public static Message reply(Message ping) {
        return new Message(null, Kind.REPLY,  ping.getSender(), ping);
    }

    public static Message ping(Player senderPlayer, UUID receiver) {
        return new Message(senderPlayer, Kind.PING, receiver, null);
    }

    public static Message reply(Player senderPlayer, Message ping) {
        return new Message(senderPlayer, Kind.REPLY,  ping.getSender(), ping);
    }

    private Message(Player senderPlayer, Kind kind, UUID receiver, Message original) {
        this.sender =(senderPlayer != null) ? senderPlayer.getId() : null;
        this.senderPlayer = senderPlayer;
        this.kind = kind;
        this.receiver = receiver;
        this.id = UUID.randomUUID();
        this.original = original;
    }

    public Kind getKind() {
        return kind;
    }

    Player getSenderPlayer() {
        return senderPlayer;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public enum Kind {
        PING,
        REPLY
    }

}