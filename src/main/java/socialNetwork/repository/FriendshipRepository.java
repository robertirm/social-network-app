package socialNetwork.repository;

import socialNetwork.domain.Entity;

import java.util.HashSet;

public interface FriendshipRepository <ID, E extends Entity<ID>> {

    Long getCount();

    E findOne(ID id);

    HashSet<E> findAll();

    E save(E entity);

    E delete(E entity);

    E update(E entity);
}
