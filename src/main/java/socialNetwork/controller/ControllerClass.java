package socialNetwork.controller;

import socialNetwork.domain.Entity;
import socialNetwork.service.NetworkService;
import socialNetwork.service.UserService;

import java.util.List;


public class ControllerClass<ID, E extends Entity<ID>> implements Controller<ID, E> {

    public final UserService<ID, E> userService;
    public final NetworkService<ID, E> statisticsService;

    public ControllerClass(UserService<ID, E> userService, NetworkService<ID, E> statisticsService) {
        this.userService = userService;
        this.statisticsService = statisticsService;
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
        return this.statisticsService.getNumberOfCommunities();
    }

    @Override
    public List<Integer> getTheMostSocialCommunity() {
        return this.statisticsService.getTheMostSocialCommunity();
    }
}
