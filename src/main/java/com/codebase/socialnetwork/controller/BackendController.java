package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.*;
import com.codebase.socialnetwork.repository.EventRepository;
import com.codebase.socialnetwork.repository.paging.Pageable;
import com.codebase.socialnetwork.service.EventService;
import com.codebase.socialnetwork.service.NetworkService;
import com.codebase.socialnetwork.service.PostService;
import com.codebase.socialnetwork.service.UserService;
import com.codebase.socialnetwork.utils.observer.Observable;
import com.codebase.socialnetwork.utils.observer.Observer;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BackendController implements Observable, Controller {
    public final UserService<Long, User> userService;
    public final NetworkService<Tuple<Long, Long>, Friendship> networkService;
    public final PostService<Long, Post> postService;
    public final EventService eventService;

    public BackendController(UserService<Long, User> userService, NetworkService<Tuple<Long, Long>, Friendship> statisticsService, PostService<Long, Post> postService,EventService eventService) {
        this.userService = userService;
        this.networkService = statisticsService;
        this.postService = postService;
        this.eventService = eventService;
    }

    @Override
    public void startApp() {
        this.userService.startApp();
    }

    @Override
    public void exitApp() {
        this.userService.exitApp();
    }

    @Override
    public String getCurrentUsername() {
        return userService.getCurrentUsername();
    }

    @Override
    public Long getCurrentUserId() {
        return userService.getCurrentUserId();
    }

    @Override
    public void login(String username, String password) {
        userService.login(username, password);
    }

    @Override
    public void logout() {
        this.userService.logout();
    }

    @Override
    public HashSet<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public HashSet<Friendship> getAllFriendships() {
        return networkService.getAllFriendships();
    }

    @Override
    public void signup(String firstName, String lastName, String username, String password) {
        userService.addUser(firstName, lastName, username, password);
    }

    @Override
    public void deleteAccount() {
        userService.removeUser();
    }

    @Override
    public void addFriend(String username) {
        networkService.addFriendship(username);
        notifyObservers();
    }

    @Override
    public void removeFriend(String username) {
        networkService.removeFriendship(username);
        notifyObservers();
    }

    @Override
    public int getNumberOfCommunities() {
        return this.networkService.getNumberOfCommunities();
    }

    @Override
    public List<Integer> getTheMostSocialCommunity() {
        return this.networkService.getTheMostSocialCommunity();
    }

    @Override
    public List<FriendDTO> getAllFriendsForUser(String username) {
        return this.networkService.getAllFriends(username);
    }

    @Override
    public List<FriendDTO> getAllFriendsForUserByMonth(String username, LocalDateTime dateTime) {
        return this.networkService.getAllFriendsByMonth(username, dateTime);
    }

    @Override
    public List<FriendDTO> getAllFriendsByStatus(String status) {
        return this.networkService.getAllFriendsByStatus(status);
    }

    @Override
    public void setFriendshipStatus(String friendUsername, String status) {
        this.networkService.setFriendshipStatus(friendUsername, status);
        notifyObservers();
    }

    @Override
    public User getUserById(Long id) {
        return userService.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    @Override
    public void sendMessage(Message message) {
        this.networkService.sendMessage(message);
    }

    @Override
    public List<Message> getReceivedMessages(String username) {
        return this.networkService.getReceivedMessages(username);
    }

    @Override
    public Message getMessageById(Long id) {
        return this.networkService.getMessageById(id);
    }

    @Override
    public List<List<Message>> getConversations(String username1, String username2) {
        return this.networkService.getConversations(username1,username2);
    }

    @Override
    public List<Conversation> getConversations(String username) {
        return this.networkService.getConversations(username);
    }

    @Override
    public Message createMessage(Message message, String text) {
        Message replyMessage = new Message(this.getUserByUsername(this.getCurrentUsername()), LocalDateTime.now(),text, true,message.getMessageContent());
        replyMessage.addReceiver(message.getSender());
        for(User receiver: message.getReceivers()){
            if(!receiver.getId().equals(this.getCurrentUserId()))
                replyMessage.addReceiver(receiver);
        }
        message.addReply(replyMessage);
        this.sendMessage(replyMessage);
        this.sendMessage(message);
        return replyMessage;
    }

    @Override
    public Message createMessage(String text, List<User> receivers){
        Message message = new Message(this.getUserById(this.getCurrentUserId()),LocalDateTime.now(),text, false,"");
        for(User user: receivers){
            message.addReceiver(user);
        }
        this.sendMessage(message);
        return message;
    }

    @Override
    public List<Post> getAllPosts(String username) {
        return postService.getAllPosts(username);
    }

    @Override
    public Pageable getPageable() {
        return postService.getPageable();
    }

    @Override
    public Long getPostsCount(String username) {
        return postService.getPostsCount(username);
    }

    @Override
    public Post getProfilePost(String username) {
        return postService.getProfilePost(username);
    }

    @Override
    public void addPost(InputStream imageStream, String description, int likes, String type, String username) {
        postService.addPost(imageStream, description, likes, type, username);
    }

    @Override
    public void updatePost(InputStream imageStream, String description, int likes, Long idPost, String username) {
        postService.updatePost(imageStream, description, likes, idPost, username);
    }

    @Override
    public List<Post> getPrevPosts(String username) {
        return postService.getPrevPosts(username);
    }

    @Override
    public List<Post> getNextPosts(String username) {
        return postService.getNextPosts(username);
    }

    @Override
    public List<Post> getFirstPagePosts(String username) {
        return postService.getFirstPagePosts(username);
    }

    @Override
    public List<Post> getPostsOnCurrentPage(int page, String username) {
        return postService.getPostsOnCurrentPage(page, username);
    }

    @Override
    public List<Message> getNumberOfSentMessages(LocalDateTime startDate, LocalDateTime endDate) {
        return networkService.getNumberOfSentMessages(startDate,endDate);
    }

    @Override
    public List<Message> getNumberOfReceivedMessages(LocalDateTime startDate, LocalDateTime endDate) {
        return networkService.getNumberOfReceivedMessages(startDate,endDate);
    }

    @Override
    public List<Message> getMessagesFromFriend(User friend, LocalDateTime startDate, LocalDateTime endDate) {
        return networkService.getMessagesFromFriend(friend,startDate,endDate);
    }

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void addEvent(String nameEvent, LocalDateTime startingDate, LocalDateTime endingDate, String location, String description, String tags, String host, InputStream imageStream,Long creatorId) {
        this.eventService.addEvent(nameEvent, startingDate, endingDate, location, description, tags, host, imageStream,creatorId);
    }

    @Override
    public Event findEvent(Long id) {
        return this.eventService.findEvent(id);
    }

    @Override
    public List<Long> getAllEventsIds() {
        return this.eventService.getAllEventsIds();
    }

    @Override
    public void addParticipant(Long idUser, Long idEvent) {
        this.eventService.addParticipant(idUser, idEvent);
    }

    @Override
    public void deleteParticipant(Long idUser, Long idEvent) {
        this.eventService.deleteParticipant(idUser, idEvent);
    }

    @Override
    public List<Long> getAttendedEvents(Long idUser) {
        return this.eventService.getAttendedEvents(idUser);
    }

    @Override
    public List<Event> getAllUserEvents(Long id) {
        return this.eventService.getAllUserEvents(id);
    }

    @Override
    public List<Long> getEventsByName(String text) {
        return eventService.getEventsByName(text);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
