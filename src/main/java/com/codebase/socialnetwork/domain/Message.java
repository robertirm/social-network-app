package com.codebase.socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message extends Entity<Long>{
    private final User sender;
    private final List<User> receivers;
    private final LocalDateTime messageDate;
    private final String messageContent;
    private final List<Message> replies;
    private final Boolean isAReply;

    public Message(User sender, LocalDateTime messageDate, String messageContent, Boolean isAReply) {
        this.sender = sender;
        this.messageDate = messageDate;
        this.messageContent = messageContent;
        this.isAReply = isAReply;

        receivers = new ArrayList<>();
        replies = new ArrayList<>();
    }

    public User getSender() {
        return sender;
    }

    public List<User> getReceivers() {
        return receivers;
    }

    public LocalDateTime getMessageDate() {
        return messageDate;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public List<Message> getReplies() {
        return replies;
    }

    public void addReceiver(User receiver){
        receivers.add(receiver);
    }

    public void addReply(Message reply){
        replies.add(reply);
    }

    public Boolean isAReply() {
        return isAReply;
    }
}
