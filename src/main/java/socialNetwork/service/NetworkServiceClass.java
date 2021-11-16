package socialNetwork.service;

import socialNetwork.domain.*;
import socialNetwork.domain.exception.EntityNullException;
import socialNetwork.repository.FriendshipRepository;
import socialNetwork.repository.UserRepository;
import socialNetwork.service.networkUtils.Graph;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkServiceClass implements NetworkService<Tuple<Long, Long>, Friendship> {
    public final UserRepository<Long, User> userRepository;
    public final FriendshipRepository<Tuple<Long, Long>, Friendship> friendshipRepository;

    public NetworkServiceClass(UserRepository<Long, User> userRepository, FriendshipRepository<Tuple<Long, Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public void addFriendship(String username) {
        Long currentUserId =  this.userRepository.getCurrentUserId();
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
        Long currentUserId =  this.userRepository.getCurrentUserId();
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

        Friendship imaginaryFriendship = new Friendship(LocalDateTime.now());
        imaginaryFriendship.setId(new Tuple<>(currentUser.getId(), removedUser.getId()));
        friendshipRepository.delete(imaginaryFriendship);
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

    /**
     * Gets the friends for a user
     * @param username - String - the username of the user
     * @return List(FriendDTO) - a list of FriendDTO objects , each objects contains the first name and last name
     * of the friend along with the date when the friendship with it was made
     */
    @Override
    public List<FriendDTO> getAllFriends(String username) {
        Long idUser = userRepository.getUserByUsername(username).getId();
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

    @Override
    public List<FriendDTO> getAllFriendsByStatus(String status){
        Long idUser = userRepository.getUserByUsername(this.userRepository.getCurrentUsername()).getId();
        if(status.equals("pending")){
            return getAllFriendShipsAsList().stream()
                    .filter(friendship -> friendship.getId().getRight().equals(idUser))
                    .filter(friendship -> friendship.getStatus().equals(status))
                    .map(friendship -> {
                        User user = userRepository.findOne(friendship.getId().getLeft());
                        return new FriendDTO(user, friendship.getDate(), friendship.getStatus());
                    })
                    .collect(Collectors.toList());
        }
        return this.getAllFriends(this.userRepository.getCurrentUsername()).stream()
                .filter(friendDTO -> friendDTO.getFriendshipStatus().equals(status))
                .collect(Collectors.toList());
    }

    public void setFriendshipStatus(String friendUsername, String status){
        Long idUserFriend = userRepository.getUserByUsername(friendUsername).getId();
        Long idCurrentUser = this.userRepository.getCurrentUserId();
        Friendship friendship = this.friendshipRepository.findOne(new Tuple<>(idUserFriend, idCurrentUser));
        friendship.setStatus(status);
        this.friendshipRepository.update(friendship);
    }
}
