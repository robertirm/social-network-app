package socialNetwork.domain;

import java.time.LocalDateTime;

import static socialNetwork.utils.Constants.DATE_TIME_FORMATTER;

public class FriendDTO {
    private String friendFirstName;
    private String friendLastName;
    private LocalDateTime dataOfFriendship;

    public FriendDTO(String friendFirstName, String friendLastName, LocalDateTime dataOfFriendship) {
        this.friendFirstName = friendFirstName;
        this.friendLastName = friendLastName;
        this.dataOfFriendship = dataOfFriendship;
    }

    public String getFriendFirstName() {
        return friendFirstName;
    }

    public void setFriendFirstName(String friendFirstName) {
        this.friendFirstName = friendFirstName;
    }

    public String getFriendLastName() {
        return friendLastName;
    }

    public void setFriendLastName(String friendLastName) {
        this.friendLastName = friendLastName;
    }

    public LocalDateTime getDataOfFriendship() {
        return dataOfFriendship;
    }

    public void setDataOfFriendship(LocalDateTime dataOfFriendship) {
        this.dataOfFriendship = dataOfFriendship;
    }

    @Override
    public String toString() {
        return friendFirstName + " | " +
                friendLastName + " | " +
                dataOfFriendship.format(DATE_TIME_FORMATTER);
    }
}
