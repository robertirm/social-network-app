package com.codebase.socialnetwork.repository.memory;

import com.codebase.socialnetwork.domain.Entity;

public interface LoginSystem<ID, E extends Entity<ID>> {
    ID getCurrentUserId();

    String getCurrentUsername();

    void login(E user);

    void logout();
}
