package socialNetwork.repository;

import socialNetwork.domain.Entity;

import java.util.HashSet;

public interface UserRepository <ID, E extends Entity<ID>> {

    E getUserByUsername(String username);

    Long getCount();

    E findOne(ID id);

    HashSet<E> findAll();

    E save(E entity);

    E delete(ID id);

    E update(E entity);
}
