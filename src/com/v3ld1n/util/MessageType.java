package com.v3ld1n.util;

public enum MessageType {
    CHAT(0),
    COMMAND(1),
    ACTION_BAR(2);

    private final byte id;

    private MessageType(int id) {
        this.id = (byte) id;
    }

    /**
     * Returns the ID of the message type
     * @return the type ID
     */
    public byte getId() {
        return this.id;
    }
}