package socialNetwork.service;

import socialNetwork.domain.Entity;
import socialNetwork.domain.Friendship;
import socialNetwork.domain.Tuple;
import socialNetwork.domain.User;
import socialNetwork.domain.exception.EntityNullException;
import socialNetwork.domain.exception.LogInException;
import socialNetwork.repository.FriendshipRepository;
import socialNetwork.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

public class UserServiceClass <ID, E extends Entity<ID>> implements UserService<ID, E> {
    public final UserRepository<ID, E> userRepository;
    public final FriendshipRepository friendshipRepository;


    public UserServiceClass(UserRepository<ID, E> userRepository, FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    private void loadFriendships(){
        System.out.println("Starting...");

//        Iterable<Friendship> allFriendships = (Iterable<Friendship>) friendshipRepository.getAllFriendships();
//
//        for(Friendship friendship : allFriendships){
//            ID firstFriendId = (ID)friendship.getId().getLeft();
//            User firstFriend = (User)userRepository.getUserByID(firstFriendId);
//
//            ID secondFriendId = (ID)friendship.getId().getRight();
//            User secondFriend = (User)userRepository.getUserByID(secondFriendId);
//
//            firstFriend.addFriend(secondFriend);
//            secondFriend.addFriend(firstFriend);
//        }
    }

    @Override
    public void startApp() {
        this.loadFriendships();
    }

    @Override
    public void exitApp() {
//        this.userRepository.writeToFile();
//        this.friendshipRepository.writeToFile();
    }

    @Override
    public void setCurrentUser(String username) {
        this.userRepository.setCurrentUser(username);
    }

    @Override
    public String getCurrentUsername() {
        return this.userRepository.getCurrentUsername();
    }

    @Override
    public Iterable<E> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public Iterable<E> getAllFriendships() {
        return friendshipRepository.getAllFriendships();
    }

//    private Long findFirstFreeUserId(){
//        Iterable<E> allUsers = userRepository.getAllUsers();
//        Long usersCount = userRepository.getUsersCount();
//        for(Long possibleID = 0L; possibleID < usersCount; possibleID++){
//            boolean valid = true;
//            for(E user : allUsers){
//                if(user.getId().equals(possibleID)){
//                    valid = false;
//                    break;
//                }
//            }
//            if(valid){
//                return possibleID;
//            }
//        }
//
//        return userRepository.getUsersCount();
//    }

    @Override
    public void addUser(String firstName, String lastName, String username) {
        User newUser = new User(firstName, lastName, username);
        // newUser.setId(this.findFirstFreeUserId());
        this.userRepository.addUser((E) newUser);
    }

    @Override
    public void removeUser() {
        String currentUsername = this.userRepository.getCurrentUsername();
        if(currentUsername == null){
            throw new LogInException();
        }

        ID currentUserId =  this.userRepository.getCurrentUserId();
        User currentUser = (User) this.userRepository.getUserByID(currentUserId);

        if (currentUser == null){
            throw new EntityNullException();
        }

        //remove all friendships for user
        Iterable<Friendship> allFriendships;
        boolean allRemoved = false;
        while (!allRemoved){
            allRemoved = true;
            allFriendships = (Iterable<Friendship>) friendshipRepository.getAllFriendships();
            for(Friendship friendship : allFriendships){
                if(friendship.getId().getLeft().equals(currentUser.getId()) || friendship.getId().getRight().equals(currentUser.getId())){
                    friendshipRepository.removeFriendship(friendship);
                    allRemoved = false;
                    break;
                }
            }
        }

        //remove user from users
        Iterable<User> allUsers = (Iterable<User>) userRepository.getAllUsers();
        for(User user : allUsers){
            if(user.getUsername().equals(currentUsername)){
                this.userRepository.removeUser((ID)user.getId());
                break;
            }
        }
    }

    @Override
    public void addFriendship(String username) {
        ID currentUserId =  this.userRepository.getCurrentUserId();
        User currentUser = (User) this.userRepository.getUserByID(currentUserId);
        Iterable<User> allUsers = (Iterable<User>) userRepository.getAllUsers();
        User addedUser = null;
        for(User user : allUsers){
            if(user.getUsername().equals(username)){
                addedUser = user;
                Friendship friendship = new Friendship(LocalDateTime.now());
                friendship.setId(new Tuple<>(currentUser.getId(), user.getId()));
                friendshipRepository.addFriendship(friendship);
//                currentUser.addFriend(user);
//                user.addFriend(currentUser);
                break;
            }
        }
        if (addedUser == null){
            throw new EntityNullException();
        }
    }

    @Override
    public void removeFriendship(String username) {
        ID currentUserId =  this.userRepository.getCurrentUserId();
        User currentUser = (User) this.userRepository.getUserByID(currentUserId);

        //remove from user friend list
        Iterable<User> allUsers = (Iterable<User>) userRepository.getAllUsers();
        User removedUser = null;
        for(User user : allUsers){
            if(user.getUsername().equals(username)){
                removedUser = user;
//                currentUser.removeFriend(user);
//                user.removeFriend(currentUser);
                break;
            }
        }
        if (removedUser == null){
            throw new EntityNullException();
        }

        Friendship imaginaryFriendship = new Friendship(LocalDateTime.now());
        imaginaryFriendship.setId(new Tuple<>(currentUser.getId(), removedUser.getId()));
        friendshipRepository.removeFriendship(imaginaryFriendship);
    }
}
