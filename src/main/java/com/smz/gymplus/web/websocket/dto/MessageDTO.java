package com.smz.gymplus.web.websocket.dto;

import java.time.Instant;

public class MessageDTO {

    private String sender;
    private String recipient;
    private String message;
    private Instant time;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant instant) {
        this.time = instant;
    }

    @Override
    public String toString() {
        return (
            "MessageDTO{" +
            "userLogin='" +
            sender +
            '\'' +
            ", recipient='" +
            recipient +
            '\'' +
            ", message='" +
            message +
            '\'' +
            ", time='" +
            time +
            '\'' +
            '}'
        );
    }
}
