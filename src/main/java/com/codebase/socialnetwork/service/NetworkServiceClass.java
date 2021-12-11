package com.codebase.socialnetwork.service;

import com.codebase.socialnetwork.domain.*;
import com.codebase.socialnetwork.domain.exception.EntityNullException;
import com.codebase.socialnetwork.repository.Repository;
import com.codebase.socialnetwork.repository.memory.LoginSystem;
import com.codebase.socialnetwork.service.networkUtils.Graph;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkServiceClass implements NetworkService<Tuple<Long, Long>, Friendship> {
    public final Repository<Long, User> userRepository;
    public final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;
    public final Repository<Long,Message> messageRepository;
    public final LoginSystem<Long, User> loginSystem;

    public NetworkServiceClass(Repository<Long, User> userRepository, Repository<Tuple<Long, Long>, Friendship> friendshipRepository, Repository<Long, Message> messageRepository, LoginSystem<Long, User> loginSystem) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.messageRepository = messageRepository;
        this.loginSystem = loginSystem;
    }

    @Override
    public void addFriendship(String username) {
        Long currentUserId =  this.loginSystem.getCurrentUserId();
        User currentUser = this.userRepository.findOne(currentUserId);
        Iterable<User> allUsers = userRepository.findAll();
        User addedUser = null;
        for(User user : allUsers){
            if(user.getUsername().equals(username)){
                addedUser = user;
                Friendship friendship = new Friendship(LocalDateTime.now());
                friendship.setId(new Tuple<>(currentUser.getId(), user.getId()));
                friendshipRepository.save(friendship);
                break;
            }
        }
        if (addedUser == null){
            throw new EntityNullException();
        }
    }

    @Override
    public void removeFriendship(String username) {
        Long currentUserId =  this.loginSystem.getCurrentUserId();
        User currentUser = this.userRepository.findOne(currentUserId);

        //remove from user friend list
        Iterable<User> allUsers = userRepository.findAll();
        User removedUser = null;
        for(User user : allUsers){
            if(user.getUsername().equals(username)){
                removedUser = user;
                break;
            }
        }
        if (removedUser == null){
            throw new EntityNullException();
        }

        friendshipRepository.delete(new Tuple<>(currentUser.getId(), removedUser.getId()));
    }

    @Override
    public int getNumberOfCommunities() {
        Iterable<User> allUser = userRepository.findAll();
        int maxId = -1;
        for(User user : allUser){
            if(maxId < user.getId().intValue()){
                maxId = user.getId().intValue();
            }
        }
        boolean[] validId = new boolean[maxId + 1];

        for(User user : allUser){
            validId[user.getId().intValue()] = true;
        }

        Graph graph = new Graph(maxId + 1);

        Iterable<Friendship> allFriendships = friendshipRepository.findAll();

        for(Friendship friendship : allFriendships){
            int firstFriendId = friendship.getId().getLeft().intValue();
            int secondFriendId = friendship.getId().getRight().intValue();
            graph.addEdge(firstFriendId, secondFriendId);
        }

        return graph.connectedComponents(validId);
    }

    @Override
    public List<Integer> getTheMostSocialCommunity() {
        Iterable<User> allUser = userRepository.findAll();
        int maxId = -1;
        for(User user : allUser){
            if(maxId < user.getId().intValue()){
                maxId = user.getId().intValue();
            }
        }
        boolean[] validId = new boolean[maxId + 1];

        for(User user : allUser){
            validId[user.getId().intValue()] = true;
        }

        Graph graph = new Graph(maxId + 1);

        Iterable<Friendship> allFriendships = friendshipRepository.findAll();

        for(Friendship friendship : allFriendships){
            int firstFriendId = friendship.getId().getLeft().intValue();
            int secondFriendId = friendship.getId().getRight().intValue();
            graph.addEdge(firstFriendId, secondFriendId);
        }

        return graph.maxConnectedComponents(validId);
    }

    @Override
    public HashSet<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }

    private List<Friendship> getAllFriendShipsAsList(){
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        List<Friendship> friendshipList = new LinkedList<>();
        friendships.forEach(friendshipList::add);
        return friendshipList;
    }

    private User getUserByUsername(String username){
        for(User user : this.userRepository.findAll()){
            if(user.getUsername().equals(username)){
                return user;
            }
        }

        throw new EntityNullException();
    }

    /**
     * Gets the friends for a user
     * @param username - String - the username of the user
     * @return List(FriendDTO) - a list of FriendDTO objects , each objects contains the first name and last name
     * of the friend along with the date when the friendship with it was made
     */
    @Override
    public List<FriendDTO> getAllFriends(String username) {
        Long idUser = this.getUserByUsername(username).getId();
        return getAllFriendShipsAsList().stream()
                .filter( friendship -> friendship.getId().getLeft().equals(idUser) || friendship.getId().getRight().equals(idUser))
                .map(friendship -> {
                    User user;
                    if(friendship.getId().getRight().equals(idUser)){
                        user = userRepository.findOne(friendship.getId().getLeft());
                    }
                    else{
                        user = userRepository.findOne(friendship.getId().getRight());
                    }
                    return new FriendDTO(user, friendship.getDate(), friendship.getStatus());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendDTO> getAllFriendsByMonth(String username, LocalDateTime dateTime) {
        return this.getAllFriends(username).stream()
                .filter(friendDTO -> friendDTO.getDataOfFriendship().getMonth() == dateTime.getMonth())
                .collect(Collectors.toList());
    }

//    @Override
//    public List<FriendDTO> getAllFriendsByStatus(String status){
//        Long idUser = this.getUserByUsername(this.loginSystem.getCurrentUsername()).getId();
//        if(status.equals("pending")){
//            return getAllFriendShipsAsList().stream()
//                    .filter(friendship -> friendship.getId().getRight().equals(idUser))
//                    .filter(friendship -> friendship.getStatus().equals(status))
//                    .map(friendship -> {
//                        User user = userRepository.findOne(friendship.getId().getLeft());
//                        return new FriendDTO(user, friendship.getDate(), friendship.getStatus());
//                    })
//                    .collect(Collectors.toList());
//        }
//        return this.getAllFriends(this.loginSystem.getCurrentUsername()).stream()
//                .filter(friendDTO -> friendDTO.getFriendshipStatus().equals(status))
//                .collect(Collectors.toList());
//    }

    @Override
    public List<FriendDTO> getAllFriendsByStatus(String status){
        Long idUser = this.getUserByUsername(this.loginSystem.getCurrentUsername()).getId();
        switch (status) {
            case "Friends":
                return this.getAllFriends(this.loginSystem.getCurrentUsername()).stream()
                        .filter(friendDTO -> friendDTO.getFriendshipStatus().equals("approved"))
                        .collect(Collectors.toList());
            case "Request":
                return getAllFriendShipsAsList().stream()
                        .filter(friendship -> friendship.getId().getRight().equals(idUser))
                        .filter(friendship -> friendship.getStatus().equals("pending"))
                        .map(friendship -> {
                            User user = userRepository.findOne(friendship.getId().getLeft());
                            return new FriendDTO(user, friendship.getDate(), friendship.getStatus());
                        })
                        .collect(Collectors.toList());
            case "Sent Request":
                return getAllFriendShipsAsList().stream()
                        .filter(friendship -> friendship.getId().getLeft().equals(idUser))
                        .filter(friendship -> friendship.getStatus().equals("pending"))
                        .map(friendship -> {
                            User user = userRepository.findOne(friendship.getId().getRight());
                            return new FriendDTO(user, friendship.getDate(), friendship.getStatus());
                        })
                        .collect(Collectors.toList());
            default:
                HashSet<User> allUsers = this.userRepository.findAll();
                List<FriendDTO> friends = this.getAllFriends(this.loginSystem.getCurrentUsername());

                List<FriendDTO> others = new ArrayList<>();

                for (User user : allUsers) {
                    if(user.getUsername().equals(this.loginSystem.getCurrentUsername())){
                        continue;
                    }
                    boolean valid = true;
                    for (FriendDTO friendDTO : friends) {
                        if(!friendDTO.getFriendshipStatus().equals("rejected")) {
                            if (user.getUsername().equals(friendDTO.getUser().getUsername())) {
                                valid = false;
                                break;
                            }
                        }
                    }
                    if (valid) {
                        others.add(new FriendDTO(user, LocalDateTime.now(), "other"));
                    }
                }

                return others;
        }
    }

    public void setFriendshipStatus(String friendUsername, String status){
        if(status.equals("rejected")){
            removeFriendship(friendUsername);
            return;
        }
        Long idUserFriend = this.getUserByUsername(friendUsername).getId();
        Long idCurrentUser = this.loginSystem.getCurrentUserId();
        Friendship friendship = this.friendshipRepository.findOne(new Tuple<>(idUserFriend, idCurrentUser));
        friendship.setStatus(status);
        this.friendshipRepository.update(friendship);
    }

    @Override
    public void sendMessage(Message message) {
        messageRepository.save(message);
    }

    @Override
    public List<Message> getSentMessages(String username) {
        List<Message> messageList = new LinkedList<>();
        for(Message message : messageRepository.findAll()){
            if(message.getSender().getUsername().equals(username))
                messageList.add(message);
        }
        return messageList;
    }

    @Override
    public List<Message> getReceivedMessages(String username) {
        List<Message> messageList = new LinkedList<>();
        for(Message message : messageRepository.findAll()){
            List<User> receivers = message.getReceivers().stream()
                    .filter(x-> x.getUsername().equals(username))
                    .collect(Collectors.toList());
            boolean hasReplied = false;
            for (Message reply: message.getReplies())
                if (reply.getSender().getUsername().equals(username)) {
                    hasReplied = true;
                    break;
                }
            if(receivers.size() > 0 && !hasReplied) messageList.add(message);
        }
        return messageList;
    }

    @Override
    public Message getMessageById(Long id) {
        return messageRepository.findOne(id);
    }


    @Override
    public List<List<Message>> getConversations(String username1, String username2) {
        List<List<Message>> conversations = new ArrayList<>();
        HashSet<Message> allMessages = messageRepository.findAll();

        for (Message message: allMessages){
            if(message.isAReply().equals(false) && (message.getSender().getUsername().equals(username1) || message.getSender().getUsername().equals(username2))) {
                List<Message> conversationThread = new ArrayList<>();
                conversationThread.add(message);

                List<Message> replies = message.getReplies();
                while (replies != null) {
                    boolean foundReply = false;
                    for (Message reply : replies) {
                        if (reply.getSender().getUsername().equals(username1) || reply.getSender().getUsername().equals(username2)) {
                            conversationThread.add(reply);
                            replies = reply.getReplies();
                            foundReply = true;
                            break;
                        }
                    }
                    if(!foundReply)replies = null;
                }
                if(conversationThread.size() > 1)
                    conversations.add(conversationThread);
            }
        }
        return conversations;
    }


}
