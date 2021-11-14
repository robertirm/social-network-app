package socialNetwork.repository;

import socialNetwork.domain.Entity;

public interface UserRepository<ID, E extends Entity<ID>> {

    void writeToFile();

    ID getCurrentUserId();

    void setCurrentUser(String username);

    String getCurrentUsername();

    Long getUsersCount();

    E getUserByID(ID id);

    Iterable<E> getAllUsers();

    E addUser(E user);

    E removeUser(ID id);

    E getUserByUsername(String username);
}
