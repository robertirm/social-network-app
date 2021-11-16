package socialNetwork.controller;

import socialNetwork.domain.*;
import socialNetwork.service.NetworkService;
import socialNetwork.service.UserService;

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
}
