package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.*;
import com.codebase.socialnetwork.utils.observer.Observer;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface Controller {
    void startApp();
    void exitApp();
    String getCurrentUsername();
    Long getCurrentUserId();
    void login(String username, String password);
    void logout();
    HashSet<User> getAllUsers();
    HashSet<Friendship> getAllFriendships();
    void signup(String firstName, String lastName, String username, String password);
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
    List<Conversation> getConversations(String username);
    Message createMessage(Message message,String text);
    Message createMessage(String text, List<User> receivers);
    HashSet<Post> getAllPosts(String username);
    Post getProfilePost(String username);
    void addPost(InputStream imageStream, String description, int likes, String type, String username);
    void updatePost(InputStream imageStream, String description, int likes, Long idPost, String username);
    Set<Post> getPrevPosts(String username);
    Set<Post> getNextPosts(String username);
    Set<Post> getPostsOnCurrentPage(int page, String username);
    Integer getNumberOfSentMessages(LocalDateTime startDate, LocalDateTime endDate);
    Integer getNumberOfReceivedMessages(LocalDateTime startDate, LocalDateTime endDate);
    List<Message> getMessagesFromFriend(User friend, LocalDateTime startDate, LocalDateTime endDate);
    void addObserver(Observer observer);
    void addEvent(String nameEvent, LocalDateTime startingDate, LocalDateTime endingDate, String location, String description,
                  String tags, String host, InputStream imageStream,Long creatorId);
    Event findEvent(Long id);
    List<Long> getAllEventsIds();
    void addParticipant(Long idUser, Long idEvent);
    void deleteParticipant(Long idUser, Long idEvent);
    List<Long> getAttendedEvents(Long idUser);
}
