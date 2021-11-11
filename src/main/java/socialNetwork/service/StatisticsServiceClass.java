package socialNetwork.service;

import socialNetwork.domain.Entity;
import socialNetwork.domain.Friendship;
import socialNetwork.domain.User;
import socialNetwork.repository.FriendshipRepository;
import socialNetwork.repository.UserRepository;
import socialNetwork.service.statisticsUtils.Graph;

import java.util.List;

public class StatisticsServiceClass <ID, E extends Entity<ID>> implements StatisticsService<ID, E> {
    public final UserRepository<ID, E> userRepository;
    public final FriendshipRepository friendshipRepository;

    public StatisticsServiceClass(UserRepository<ID, E> userRepository, FriendshipRepository friendshipRepository) {
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
}
