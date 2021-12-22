package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.*;
import com.codebase.socialnetwork.service.NetworkService;
import com.codebase.socialnetwork.service.PostService;
import com.codebase.socialnetwork.service.UserService;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public class BackendController implements Controller {
    public final UserService<Long, User> userService;
    public final NetworkService<Tuple<Long, Long>, Friendship> networkService;
    public final PostService<Long, Post> postService;

    public BackendController(UserService<Long, User> userService, NetworkService<Tuple<Long, Long>, Friendship> statisticsService, PostService<Long, Post> postService) {
        this.userService = userService;
        this.networkService = statisticsService;
        this.postService = postService;
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
    public void login(String username) {
        userService.login(username);
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
    public void signup(String firstName, String lastName, String username) {
        userService.addUser(firstName, lastName, username);
    }

    @Override
    public void deleteAccount() {
        userService.removeUser();
    }

    @Override
    public void addFriend(String username) {
        networkService.addFriendship(username);
    }

    @Override
    public void removeFriend(String username) {
        networkService.removeFriendship(username);
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
        Message replyMessage = new Message(this.getUserByUsername(this.getCurrentUsername()), LocalDateTime.now(),text, true);
        replyMessage.addReceiver(message.getSender());
//        for(User receiver: message.getReceivers()){
//            if(!receiver.getId().equals(this.getCurrentUserId()))
//                replyMessage.addReceiver(receiver);
//        }
        message.addReply(replyMessage);
        this.sendMessage(replyMessage);
        this.sendMessage(message);
        return replyMessage;
    }

    @Override
    public Message createMessage(String text, List<User> receivers){
        Message message = new Message(this.getUserById(this.getCurrentUserId()),LocalDateTime.now(),text, false);
        for(User user: receivers){
            message.addReceiver(user);
        }
        this.sendMessage(message);
        return message;
    }

    @Override
    public HashSet<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @Override
    public void addPost(InputStream imageStream, String description, int likes, String type, String username) {
        postService.addPost(imageStream, description, likes, type, username);
    }

    @Override
    public void updatePost(InputStream imageStream, String description, int likes, Long idPost) {
        postService.updatePost(imageStream, description, likes, idPost);
    }
}
