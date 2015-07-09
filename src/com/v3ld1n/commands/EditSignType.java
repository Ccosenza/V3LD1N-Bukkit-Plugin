package com.v3ld1n.commands;

import com.v3ld1n.Message;

public enum EditSignType {
    SET(Message.EDITSIGN_SET),
    ADD(Message.EDITSIGN_ADD),
    REMOVE(Message.EDITSIGN_REMOVE);

    private final Message message;

    private EditSignType(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}