package socialNetwork.service;

import socialNetwork.domain.Friendship;
import socialNetwork.domain.Tuple;
import socialNetwork.domain.User;
import socialNetwork.domain.exception.EntityNullException;
import socialNetwork.domain.exception.LogInException;
import socialNetwork.domain.exception.WrongUsernameException;
import socialNetwork.repository.FriendshipRepository;
import socialNetwork.repository.UserRepository;
import socialNetwork.repository.memory.LoginSystem;

import java.util.HashSet;

public class UserServiceClass implements UserService<Long, User> {
    public final UserRepository<Long, User> userRepository;
    public final FriendshipRepository<Tuple<Long, Long>, Friendship> friendshipRepository;
    public final LoginSystem<Long, User> loginSystem;

    public UserServiceClass(UserRepository<Long, User> userRepository, FriendshipRepository<Tuple<Long, Long>, Friendship> friendshipRepository, LoginSystem<Long, User> loginSystem) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.loginSystem = loginSystem;
    }

    private void loadFriendships(){
        System.out.println("Starting...");
    }

    @Override
    public void startApp() {
        this.loadFriendships();
    }

    @Override
    public void exitApp() { }

    @Override
    public void login(String username) {
        boolean valid = false;
        for(User user : this.userRepository.findAll()){
            if(user.getUsername().equals(username)){
                this.loginSystem.login(user);
                valid = true;
                break;
            }
        }

        if(!valid){
            throw new WrongUsernameException();
        }
    }

    @Override
    public void logout() {
        this.loginSystem.logout();
    }

    @Override
    public String getCurrentUsername() {
        return this.loginSystem.getCurrentUsername();
    }

    @Override
    public HashSet<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void addUser(String firstName, String lastName, String username) {
        User newUser = new User(firstName, lastName, username);
        this.userRepository.save(newUser);
    }

    @Override
    public void removeUser() {
        String currentUsername = this.loginSystem.getCurrentUsername();
        if(currentUsername == null){
            throw new LogInException();
        }

        Long currentUserId =  this.loginSystem.getCurrentUserId();
        User currentUser = this.userRepository.findOne(currentUserId);

        if (currentUser == null){
            throw new EntityNullException();
        }

        //remove all friendships for user
        Iterable<Friendship> allFriendships;
        boolean allRemoved = false;
        while (!allRemoved){
            allRemoved = true;
            allFriendships = friendshipRepository.findAll();
            for(Friendship friendship : allFriendships){
                if(friendship.getId().getLeft().equals(currentUser.getId()) || friendship.getId().getRight().equals(currentUser.getId())){
                    friendshipRepository.delete(friendship);
                    allRemoved = false;
                    break;
                }
            }
        }

        //remove user from users
        User removedUser = null;
        Iterable<User> allUsers = userRepository.findAll();
        for(User user : allUsers){
            if(user.getUsername().equals(currentUsername)){
                removedUser = this.userRepository.delete(user.getId());
                break;
            }
        }

        if(removedUser != null){
            loginSystem.logout();
        }
    }
}
