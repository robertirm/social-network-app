package socialNetwork.repository.memory;

import socialNetwork.domain.Entity;

public interface LoginSystem<ID, E extends Entity<ID>> {
    ID getCurrentUserId();

    String getCurrentUsername();

    void login(E user);

    void logout();
}
