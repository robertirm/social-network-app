package com.codebase.socialnetwork.repository;

import com.codebase.socialnetwork.domain.Entity;

import java.util.HashSet;

public interface Repository<ID, E extends Entity<ID>> {
    Long getCount();

    E findOne(ID id);

    HashSet<E> findAll();

    E save(E entity);

    E delete(ID id);

    E update(E entity);
}
