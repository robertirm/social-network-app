package com.codebase.socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private List<Message> messageList;
    private List<User> members;

    public Conversation(List<Message> messageList) {
        this.messageList = messageList;
        this.members = new ArrayList<>();
        members.add(messageList.get(0).getSender());
        members.addAll(messageList.get(0).getReceivers());
    }

    public Conversation() {
        messageList = new ArrayList<>();
        members = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < members.size()-1; i++) {
            stringBuilder.append(members.get(i).getUsername()).append(",");
        }
        stringBuilder.append(members.get(members.size()-1).getUsername());
        return stringBuilder.toString();
    }


    public List<Message> getMessageList() {
        return messageList;
    }

    public void addMessage(Message message){
        messageList.add(message);
    }


}
