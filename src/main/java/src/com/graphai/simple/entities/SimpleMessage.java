package com.graphai.simple.entities;

import java.io.Serializable;
import java.util.UUID;

/**
 * A simple message with ping and reply support
 */
public class SimpleMessage implements Serializable {
    private final Kind kind;

    private UUID sender;
    private final UUID receiver;
    private final UUID id;
    private final SimpleMessage original;

    public enum Kind {
        PING,
        REPLY
    }

    public static SimpleMessage ping(UUID sender, UUID receiver) {
        return new SimpleMessage(Kind.PING, sender, receiver, null);
    }

    public static SimpleMessage reply(SimpleMessage ping) {
        return new SimpleMessage(Kind.REPLY, ping.getReceiver(), ping.getSender(), ping);
    }

    private SimpleMessage(Kind kind, UUID sender, UUID receiver, SimpleMessage original) {
        this.kind = kind;
        this.sender = sender;
        this.receiver = receiver;
        this.id = UUID.randomUUID();
        this.original = original;
    }

    public Kind getKind() {
        return kind;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getReceiver() {
        return receiver;
    }
}