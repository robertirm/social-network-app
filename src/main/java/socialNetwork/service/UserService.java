package socialNetwork.service;

import socialNetwork.domain.Entity;

import java.util.HashSet;

public interface UserService <ID, E extends Entity<ID>> {
    void startApp();
    void exitApp();
    void setCurrentUser(String username);
    String getCurrentUsername();
    HashSet<E> getAllUsers();
    void addUser(String firstName, String lastName, String username);
    void removeUser();
}
