package socialNetwork.repository;

import socialNetwork.domain.Entity;

public interface FriendshipRepository <ID, E extends Entity<ID>> {

    void writeToFile();

    Long getFriendshipsCount();

    Iterable<E> getAllFriendships();

    E addFriendship(E friendship);

    E removeFriendship(E friendship);
}
