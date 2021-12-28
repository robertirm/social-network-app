package com.codebase.socialnetwork.service;

import com.codebase.socialnetwork.domain.Friendship;
import com.codebase.socialnetwork.domain.Tuple;
import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.domain.exception.EntityNullException;
import com.codebase.socialnetwork.domain.exception.LogInException;
import com.codebase.socialnetwork.domain.exception.WrongPasswordException;
import com.codebase.socialnetwork.domain.exception.WrongUsernameException;
import com.codebase.socialnetwork.repository.Repository;
import com.codebase.socialnetwork.repository.memory.LoginSystem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

public class UserServiceClass implements UserService<Long, User> {
    public final Repository<Long, User> userRepository;
    public final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;
    public final LoginSystem<Long, User> loginSystem;

    public UserServiceClass(Repository<Long, User> userRepository, Repository<Tuple<Long, Long>, Friendship> friendshipRepository, LoginSystem<Long, User> loginSystem) {
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
    public void login(String username, String password) {
        boolean valid = false;
        for(User user : this.userRepository.findAll()){
            if(user.getUsername().equals(username)){
                if(!user.getPassword().equals(hashPassword(password))){
                    throw new WrongPasswordException();
                }
                this.loginSystem.login(user);
                valid = true;
                break;
            }
        }

        if(!valid){
            throw new WrongUsernameException();
        }
    }

    //https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    private String hashPassword(String passwordToHash){
        String generatedPassword = null;

        try
        {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(passwordToHash.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        System.out.println(generatedPassword);
        return generatedPassword;
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
    public Long getCurrentUserId() {
        return loginSystem.getCurrentUserId();
    }

    @Override
    public HashSet<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void addUser(String firstName, String lastName, String username, String password) {
        User newUser = new User(firstName, lastName, username, hashPassword(password));
        Iterable<User> allUsers = this.userRepository.findAll();
        for (User user : allUsers){
            if(user.getUsername().equals(username)){
                throw new WrongUsernameException();
            }
        }
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
                    friendshipRepository.delete(friendship.getId());
                    allRemoved = false;
                    break;
                }
            }
        }

        // remove user from users
        User removedUser = this.userRepository.delete(currentUserId);

        if(removedUser != null){
            loginSystem.logout();
        }
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User getUserByUsername(String username) {
        for(User user : this.getAllUsers()) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

}
