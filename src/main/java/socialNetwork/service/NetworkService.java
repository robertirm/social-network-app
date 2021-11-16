package socialNetwork.service;

import socialNetwork.domain.Entity;
import socialNetwork.domain.FriendDTO;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public interface NetworkService<ID, E extends Entity<ID>> {
    void addFriendship(String username);
    void removeFriendship(String username);
    int getNumberOfCommunities();
    HashSet<E> getAllFriendships();
    List<Integer> getTheMostSocialCommunity();
    List<FriendDTO> getAllFriends(String username);
    List<FriendDTO> getAllFriendsByMonth(String username, LocalDateTime dateTime);
    List<FriendDTO> getAllFriendsByStatus(String status);
    void setFriendshipStatus(String friendUsername, String status);
}
