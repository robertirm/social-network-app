package socialNetwork.repository.memory;

import socialNetwork.domain.User;
import socialNetwork.domain.exception.LogInException;

public class LoginSystemClass implements LoginSystem<Long, User> {
    private Long currentUserId;
    private String currentUsername;

    @Override
    public Long getCurrentUserId() {
        if(currentUserId == null){
            throw new LogInException();
        }

        return currentUserId;
    }

    @Override
    public String getCurrentUsername() {
        return this.currentUsername;
    }

    @Override
    public void login(User user) {
        this.currentUserId = user.getId();
        this.currentUsername = user.getUsername();
    }

    @Override
    public void logout() {
        this.currentUsername = null;
        this.currentUserId = null;
    }
}
