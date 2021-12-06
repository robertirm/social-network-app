package com.codebase.socialnetwork.service;

import com.codebase.socialnetwork.domain.Entity;
import com.codebase.socialnetwork.domain.FriendDTO;
import com.codebase.socialnetwork.domain.Message;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public interface NetworkService<ID, E extends Entity<ID>> {
    void addFriendship(String username);
    void removeFriendship(String username);
    int getNumberOfCommunities();
    HashSet<E> getAllFriendships();
    List<Integer> getTheMostSocialCommunity();
    List<FriendDTO> getAllFriends(String username);
    List<FriendDTO> getAllFriendsByMonth(String username, LocalDateTime dateTime);
    List<FriendDTO> getAllFriendsByStatus(String status);
    void setFriendshipStatus(String friendUsername, String status);
    void sendMessage(Message message);
    List<Message> getSentMessages(String username);
    List<Message> getReceivedMessages(String username);
    Message getMessageById(Long id);
    List<List<Message>> getConversations(String username1, String username2);
}
