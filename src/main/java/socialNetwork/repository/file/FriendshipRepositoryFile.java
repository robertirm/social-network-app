package socialNetwork.repository.file;

import socialNetwork.domain.Entity;
import socialNetwork.domain.Friendship;
import socialNetwork.domain.Tuple;
import socialNetwork.domain.validator.Validator;
import socialNetwork.repository.UserRepository;
import static socialNetwork.utils.Constants.DATE_TIME_FORMATTER;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipRepositoryFile <ID, E extends Entity<ID>>  extends FriendshipRepositoryFileAbstract<ID, E>{

    public FriendshipRepositoryFile(String filename, Validator<E> validator) {
        super(filename, validator);
    }

    @Override
    public E extractFriendship(List<String> attributes) {
        LocalDateTime dateTime = LocalDateTime.parse(attributes.get(2).strip(), DATE_TIME_FORMATTER);
        Friendship friendship = new Friendship(dateTime);
        friendship.setId(new Tuple<>(Long.parseLong(attributes.get(0)), Long.parseLong(attributes.get(1))));

        return (E)friendship;
    }

    @Override
    protected String createFriendshipAsString(E friendshipEntity) {
        Friendship friendship = (Friendship) friendshipEntity;
        return friendship.getId().getLeft().toString() + ";" + friendship.getId().getRight().toString() + ";" + friendship.getDate().format(DATE_TIME_FORMATTER);
    }
}
