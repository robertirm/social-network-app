package socialNetwork.repository.file;

import socialNetwork.domain.Entity;
import socialNetwork.domain.User;
import socialNetwork.domain.validator.Validator;

import java.util.List;

public class UserRepositoryFile<ID, E extends Entity<ID>>  extends UserRepositoryFileAbstract<ID, E> {

    public UserRepositoryFile(String filename, Validator<E> validator) {
        super(filename, validator);
    }

    @Override
    public E extractUser(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2), attributes.get(3));
        user.setId(Long.parseLong(attributes.get(0)));

        return (E)user;
    }

    @Override
    protected String createUserAsString(E userEntity) {
        User user = (User) userEntity;
        return user.getId().toString() + ";" + user.getFirstName() + ";" + user.getLastName() + ";" + user.getUsername();
    }
}
