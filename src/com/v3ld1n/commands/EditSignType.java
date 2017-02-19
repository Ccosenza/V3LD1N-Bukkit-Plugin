package com.v3ld1n.commands;

import com.v3ld1n.Message;

public enum EditSignType {
    SET(Message.get("editsign-set")),
    ADD(Message.get("editsign-add")),
    REMOVE(Message.get("editsign-remove"));

    private final Message message;

    private EditSignType(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}