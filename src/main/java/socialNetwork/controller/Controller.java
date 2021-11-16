package socialNetwork.controller;

import socialNetwork.domain.Entity;
import socialNetwork.domain.FriendDTO;
import socialNetwork.domain.Friendship;
import socialNetwork.domain.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public interface Controller {
    void startApp();
    void exitApp();
    String getCurrentUsername();
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
}
