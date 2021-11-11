package socialNetwork.repository.memory;

import socialNetwork.domain.Entity;
import socialNetwork.domain.User;
import socialNetwork.domain.exception.EntityNullException;
import socialNetwork.domain.exception.IdNullException;
import socialNetwork.domain.exception.LogInException;
import socialNetwork.domain.exception.WrongUsernameException;
import socialNetwork.domain.validator.Validator;
import socialNetwork.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class UserRepositoryMemory<ID, E extends Entity<ID>> implements UserRepository<ID, E> {
    private Object currentUserId;
    private String username;

    private Map<ID, E> entities;
    private Validator<E> validator;


    public UserRepositoryMemory(Validator<E> validator) {
        this.validator = validator;
        this.entities = new HashMap<>();
        this.currentUserId = null;
    }

    @Override
    public void writeToFile() {

    }

    @Override
    public ID getCurrentUserId() {
        if(currentUserId == null){
            throw new LogInException();
        }
        return (ID) currentUserId;
    }

    @Override
    public void setCurrentUser(String username) {
        boolean valid = false;
        for(E entity : entities.values()){
            User user = (User) entity;
            if(user.getUsername().equals(username)){
                this.currentUserId = user.getId();
                this.username = username;
                valid = true;
                break;
            }
        }
        if(!valid){
            throw new WrongUsernameException();
        }
    }

    @Override
    public String getCurrentUsername() {
        return this.username;
    }

    @Override
    public Long getUsersCount() {
        return Long.valueOf(entities.size());
    }

    @Override
    public E getUserByID(ID id) {
        if(id == null){
            throw new IdNullException();
        }
        return entities.get(id);
    }

    @Override
    public Iterable<E> getAllUsers() {
        return entities.values();
    }

    @Override
    public E addUser(E user) {
        if(user == null){
            throw new EntityNullException();
        }
        validator.validate(user);

        if(entities.get(user.getId()) != null){
            return user;
        }

        entities.put(user.getId(), user);
        return null;
    }

    @Override
    public E removeUser(ID id) {
        if(id == null){
            throw new IdNullException();
        }

        E entity = entities.get(id);
        if(entity == null){
            throw new EntityNullException();
        }

        entities.remove(id);
        this.username = null;
        this.currentUserId = null;
        return entity;
    }
}
