package socialNetwork.domain;

import java.time.LocalDateTime;

import static socialNetwork.utils.Constants.DATE_TIME_FORMATTER;

public class FriendDTO {
    private String friendUsername;
    private LocalDateTime dataOfFriendship;
    private String friendshipStatus;

    public FriendDTO(String friendUsername, LocalDateTime dataOfFriendship, String friendshipStatus) {
        this.friendUsername = friendUsername;
        this.dataOfFriendship = dataOfFriendship;
        this.friendshipStatus = friendshipStatus;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public LocalDateTime getDataOfFriendship() {
        return dataOfFriendship;
    }

    public String getFriendshipStatus() {
        return friendshipStatus;
    }

    @Override
    public String toString() {
        return "FriendDTO{" +
                "friendUsername='" + friendUsername + '\'' +
                ", dataOfFriendship=" + dataOfFriendship.format(DATE_TIME_FORMATTER) +
                ", friendshipStatus='" + friendshipStatus + '\'' +
                '}';
    }
}
