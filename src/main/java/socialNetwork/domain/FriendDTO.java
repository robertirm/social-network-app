package socialNetwork.domain;

import java.time.LocalDateTime;

import static socialNetwork.utils.Constants.DATE_TIME_FORMATTER;

public class FriendDTO {
    private User user;
    private LocalDateTime dataOfFriendship;
    private String friendshipStatus;

    public FriendDTO(User user, LocalDateTime dataOfFriendship, String friendshipStatus) {
        this.user= user;
        this.dataOfFriendship = dataOfFriendship;
        this.friendshipStatus = friendshipStatus;
    }

    public User getUser() { return user; }

    public LocalDateTime getDataOfFriendship() {
        return dataOfFriendship;
    }

    public String getFriendshipStatus() {
        return friendshipStatus;
    }

    @Override
    public String toString() {
        return "FriendDTO{" +
                "username=" + user.getUsername() +
                ", dataOfFriendship=" + dataOfFriendship +
                ", friendshipStatus='" + friendshipStatus + '\'' +
                '}';
    }
}
