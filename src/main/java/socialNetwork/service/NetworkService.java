package socialNetwork.service;

import socialNetwork.domain.Entity;
import socialNetwork.domain.FriendDTO;
import socialNetwork.domain.Friendship;

import java.time.LocalDateTime;
import java.util.List;

public interface NetworkService<ID, E extends Entity<ID>> {
    int getNumberOfCommunities();
    List<Integer> getTheMostSocialCommunity();
    List<Friendship> getAllFriendShipsAsList();
    List<FriendDTO> getAllFriends(String username);
    List<FriendDTO> getAllFriendsByMonth(String username, LocalDateTime dateTime);
    List<FriendDTO> getAllFriendsByStatus(String status);
    void setFriendshipStatus(String friendUsername, String status);
}
