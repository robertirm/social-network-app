package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.*;
import com.codebase.socialnetwork.service.NetworkService;
import com.codebase.socialnetwork.service.UserService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public class ControllerClass implements Controller {
    public final UserService<Long, User> userService;
    public final NetworkService<Tuple<Long, Long>, Friendship> networkService;

    public ControllerClass(UserService<Long, User> userService, NetworkService<Tuple<Long, Long>, Friendship> statisticsService) {
        this.userService = userService;
        this.networkService = statisticsService;
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
}
