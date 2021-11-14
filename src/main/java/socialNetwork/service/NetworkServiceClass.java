package socialNetwork.service;

import socialNetwork.domain.Entity;
import socialNetwork.domain.FriendDTO;
import socialNetwork.domain.Friendship;
import socialNetwork.domain.User;
import socialNetwork.repository.FriendshipRepository;
import socialNetwork.repository.UserRepository;
import socialNetwork.service.networkUtils.Graph;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkServiceClass<ID, E extends Entity<ID>> implements NetworkService<ID, E> {
    public final UserRepository<ID, E> userRepository;
    public final FriendshipRepository friendshipRepository;

    public NetworkServiceClass(UserRepository<ID, E> userRepository, FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }


    @Override
    public int getNumberOfCommunities() {
        Iterable<User> allUser = (Iterable<User>) userRepository.getAllUsers();
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

        Iterable<Friendship> allFriendships = (Iterable<Friendship>) friendshipRepository.getAllFriendships();

        for(Friendship friendship : allFriendships){
            int firstFriendId = friendship.getId().getLeft().intValue();
            int secondFriendId = friendship.getId().getRight().intValue();
            graph.addEdge(firstFriendId, secondFriendId);
        }

        return graph.connectedComponents(validId);
    }

    @Override
    public List<Integer> getTheMostSocialCommunity() {
        Iterable<User> allUser = (Iterable<User>) userRepository.getAllUsers();
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

        Iterable<Friendship> allFriendships = (Iterable<Friendship>) friendshipRepository.getAllFriendships();

        for(Friendship friendship : allFriendships){
            int firstFriendId = friendship.getId().getLeft().intValue();
            int secondFriendId = friendship.getId().getRight().intValue();
            graph.addEdge(firstFriendId, secondFriendId);
        }

        return graph.maxConnectedComponents(validId);
    }

    @Override
    public List<Friendship> getAllFriendShipsAsList(){
        Iterable<Friendship> friendships = (Iterable<Friendship>)friendshipRepository.getAllFriendships();
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
        Long idUser = (Long)userRepository.getUserByUsername(username).getId();
        return getAllFriendShipsAsList().stream()
                .filter( friendship -> friendship.getId().getLeft().equals(idUser) || friendship.getId().getRight().equals(idUser))
                .map(friendship -> {
                    User user;
                    if(friendship.getId().getRight().equals(idUser)){
                        user = (User) userRepository.getUserByID((ID) friendship.getId().getLeft());
                    }
                    else{   // friendship.getId().getLeft().equals(idUser)
                        user = (User) userRepository.getUserByID((ID) friendship.getId().getRight());
                    }
                    return new FriendDTO(user.getFirstName(),user.getLastName(),friendship.getDate());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendDTO> getAllFriendsByMonth(String username, LocalDateTime dateTime) {
        return this.getAllFriends(username).stream()
                .filter(friendDTO -> friendDTO.getDataOfFriendship().getMonth() == dateTime.getMonth())
                .collect(Collectors.toList());
    }

    public List<Friendship> getAllFriendsByStatus(String status){
        Long idUser = (Long)this.userRepository.getCurrentUserId();
        return getAllFriendShipsAsList().stream()
                .filter( friendship -> friendship.getId().getLeft().equals(idUser) || friendship.getId().getRight().equals(idUser))
                .filter(friendship -> friendship.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
