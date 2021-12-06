package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.FriendDTO;
import com.codebase.socialnetwork.domain.Friendship;
import com.codebase.socialnetwork.domain.Message;
import com.codebase.socialnetwork.domain.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public interface Controller {
    void startApp();
    void exitApp();
    String getCurrentUsername();
    Long getCurrentUserId();
    void login(String username);
    void logout();
    HashSet<User> getAllUsers();
    HashSet<Friendship> getAllFriendships();
    void signup(String firstName, String lastName, String username);
    void deleteAccount();
    void addFriend(String username);
    void removeFriend(String username);
    int getNumberOfCommunities();
    List<Integer> getTheMostSocialCommunity();
    List<FriendDTO> getAllFriendsForUser(String username);
    List<FriendDTO> getAllFriendsForUserByMonth(String username, LocalDateTime dateTime);
    List<FriendDTO> getAllFriendsByStatus(String status);
    void setFriendshipStatus(String friendUsername, String status);
    User getUserById(Long id);
    User getUserByUsername(String username);
    void sendMessage(Message message);
    List<Message> getReceivedMessages(String username);
    Message getMessageById(Long id);
    List<List<Message>> getConversations(String username1, String username2);
}
