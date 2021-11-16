package socialNetwork.repository;

import socialNetwork.domain.Entity;

public interface FriendshipRepository <ID, E extends Entity<ID>> {

    void writeToFile();

    Long getFriendshipsCount();

    E getFriendshipByID(ID id);

    Iterable<E> getAllFriendships();

    E addFriendship(E friendship);

    E removeFriendship(E friendship);

    E updateFriendship(E friendship);
}
