package socialNetwork.repository.memory;

import socialNetwork.domain.Entity;
import socialNetwork.domain.User;

public interface LoginSystem<ID, E extends Entity<ID>> {
    ID getCurrentUserId();

    String getCurrentUsername();

    void login(User user);

    void logout();
}
