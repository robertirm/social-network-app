package com.codebase.socialnetwork.domain;

import java.time.LocalDateTime;

import static com.codebase.socialnetwork.utils.Constants.DATE_TIME_FORMATTER;

public class FriendDTO {
    private final User user;
    private final LocalDateTime dataOfFriendship;
    private final String friendshipStatus;

    public FriendDTO(User user, LocalDateTime dataOfFriendship, String friendshipStatus) {
        this.user = user;
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
                ", dataOfFriendship=" + dataOfFriendship.format(DATE_TIME_FORMATTER) +
                ", friendshipStatus='" + friendshipStatus + '\'' +
                '}';
    }
}
