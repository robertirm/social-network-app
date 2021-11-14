package socialNetwork.controller;

import socialNetwork.domain.Entity;
import socialNetwork.domain.FriendDTO;
import socialNetwork.domain.Friendship;
import socialNetwork.service.NetworkService;
import socialNetwork.service.UserService;

import java.time.LocalDateTime;
import java.util.List;


public class ControllerClass<ID, E extends Entity<ID>> implements Controller<ID, E> {

    public final UserService<ID, E> userService;
    public final NetworkService<ID, E> networkService;

    public ControllerClass(UserService<ID, E> userService, NetworkService<ID, E> statisticsService) {
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
        userService.setCurrentUser(username);
    }

    @Override
    public Iterable<E> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public Iterable<E> getAllFriendships() {
        return userService.getAllFriendships();
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
        userService.addFriendship(username);
    }

    @Override
    public void removeFriend(String username) {
        userService.removeFriendship(username);
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
    public List<Friendship> getAllFriendsByStatus(String status) {
        return this.networkService.getAllFriendsByStatus(status);
    }
}
