package socialNetwork.service;

import socialNetwork.domain.Friendship;
import socialNetwork.domain.Tuple;
import socialNetwork.domain.User;
import socialNetwork.domain.exception.EntityNullException;
import socialNetwork.domain.exception.LogInException;
import socialNetwork.repository.FriendshipRepository;
import socialNetwork.repository.UserRepository;

import java.util.HashSet;

public class UserServiceClass implements UserService<Long, User> {
    public final UserRepository<Long, User> userRepository;
    public final FriendshipRepository<Tuple<Long, Long>, Friendship> friendshipRepository;

    public UserServiceClass(UserRepository<Long, User> userRepository, FriendshipRepository<Tuple<Long, Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
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
    public void setCurrentUser(String username) {
        this.userRepository.setCurrentUser(username);
    }

    @Override
    public String getCurrentUsername() {
        return this.userRepository.getCurrentUsername();
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
        String currentUsername = this.userRepository.getCurrentUsername();
        if(currentUsername == null){
            throw new LogInException();
        }

        Long currentUserId =  this.userRepository.getCurrentUserId();
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
        Iterable<User> allUsers = userRepository.findAll();
        for(User user : allUsers){
            if(user.getUsername().equals(currentUsername)){
                this.userRepository.delete(user.getId());
                break;
            }
        }
    }
}
