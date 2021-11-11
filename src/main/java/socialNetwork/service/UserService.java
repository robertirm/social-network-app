package socialNetwork.service;

import socialNetwork.domain.Entity;

public interface UserService <ID, E extends Entity<ID>> {
    void startApp();
    void exitApp();
    void setCurrentUser(String username);
    String getCurrentUsername();
    Iterable<E> getAllUsers();
    Iterable<E> getAllFriendships();
    void addUser(String firstName, String lastName, String username);
    void removeUser();
    void addFriendship(String username);
    void removeFriendship(String username);
}
