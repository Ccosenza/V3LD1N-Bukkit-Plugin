package com.v3ld1n.commands;

import com.v3ld1n.Message;

public enum RideType {
    RIDE(Message.get("ride-ride")),
    HOLD(Message.get("ride-hold"));

    private final Message message;

    private RideType(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}