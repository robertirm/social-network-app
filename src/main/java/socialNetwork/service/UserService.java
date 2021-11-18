package socialNetwork.service;

import socialNetwork.domain.Entity;

import java.util.HashSet;

public interface UserService <ID, E extends Entity<ID>> {
    void startApp();
    void exitApp();
    void login(String username);
    void logout();
    String getCurrentUsername();
    ID getCurrentUserId();
    HashSet<E> getAllUsers();
    void addUser(String firstName, String lastName, String username);
    void removeUser();
    E getUserById(ID id);
    E getUserByUsername(String username);
}
