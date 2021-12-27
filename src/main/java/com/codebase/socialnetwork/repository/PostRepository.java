package com.codebase.socialnetwork.repository;

import com.codebase.socialnetwork.domain.Entity;

import java.util.HashSet;

public interface PostRepository<ID, E extends Entity<ID>> {
    Long getCount(String username);

    E findOne(ID id);

    HashSet<E> findAll(String username);

    E findProfilePost(String username);

    E save(E entity);

    E delete(ID id);

    E update(E entity);
}
