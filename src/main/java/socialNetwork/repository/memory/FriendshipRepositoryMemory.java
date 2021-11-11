package socialNetwork.repository.memory;

import socialNetwork.domain.Entity;
import socialNetwork.domain.Friendship;
import socialNetwork.domain.User;
import socialNetwork.domain.exception.EntityNullException;
import socialNetwork.domain.validator.Validator;
import socialNetwork.repository.FriendshipRepository;
import socialNetwork.repository.UserRepository;

import java.util.Set;
import java.util.TreeSet;

public class FriendshipRepositoryMemory<ID, E extends Entity<ID>> implements FriendshipRepository<ID, E> {

    private Set<E> friendships;
    private Validator<E> validator;

    public FriendshipRepositoryMemory(Validator<E> validator) {
        this.friendships = new TreeSet<>();
        this.validator = validator;
    }

    @Override
    public void writeToFile() {

    }

    @Override
    public Long getFriendshipsCount() {
        return Long.valueOf(friendships.size());
    }

    @Override
    public Iterable<E> getAllFriendships() {
        return friendships;
    }

    @Override
    public E addFriendship(E friendshipEntity) {
        if(friendshipEntity == null){
            throw new EntityNullException();
        }
        validator.validate(friendshipEntity);

        if(friendships.contains(friendshipEntity)){
            return friendshipEntity;
        }
        friendships.add(friendshipEntity);

        return null;
    }

    @Override
    public E removeFriendship(E friendship) {
        if(friendship == null){
            throw new EntityNullException();
        }

        if(!friendships.contains(friendship)){
            throw new EntityNullException();
        }

        friendships.remove(friendship);
        return friendship;
    }
}
