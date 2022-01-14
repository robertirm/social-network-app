package com.codebase.socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message extends Entity<Long>{
    private final User sender;
    private final List<User> receivers;
    private final LocalDateTime messageDate;
    private String messageContent;
    private final List<Message> replies;
    private final Boolean isAReply;

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    private String repliedTo;

    public Message(User sender, LocalDateTime messageDate, String messageContent, Boolean isAReply,String repliedTo) {
        this.sender = sender;
        this.messageDate = messageDate;
        this.messageContent = messageContent;
        this.isAReply = isAReply;
        this.repliedTo = repliedTo;

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

    public String getRepliedTo() {
        return repliedTo;
    }
    public void setRepliedTo(String repliedTo) {
        this.repliedTo = repliedTo;
    }

    @Override
    public String toString() {
        return this.sender.getUsername() + " : " + this.messageContent;
    }



}
