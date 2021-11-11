package socialNetwork.controller;

import socialNetwork.domain.Entity;

import java.util.List;

public interface Controller<ID, E extends Entity<ID>> {
    void startApp();
    void exitApp();
    String getCurrentUsername();
    void login(String username);
    Iterable<E> getAllUsers();
    Iterable<E> getAllFriendships();
    void signup(String firstName, String lastName, String username);
    void deleteAccount();
    void addFriend(String username);
    void removeFriend(String username);
    int getNumberOfCommunities();
    List<Integer> getTheMostSocialCommunity();
}
