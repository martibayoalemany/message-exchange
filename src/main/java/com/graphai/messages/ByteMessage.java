package com.graphai.messages;

/**
 * A message containing the communication between an initiator (0x4) and a replier (0x8).
 */
public enum ByteMessage {
    PING(0x1),
    REPLY(0x2);
    private byte value;

    ByteMessage(int value) {
        this((byte) value);
    }

    ByteMessage(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toBinaryString(value);
    }
}
