package socialNetwork.domain;

import java.util.ArrayList;
import java.util.List;

public class User extends Entity<Long>{
    private final String firstName;
    private final String lastName;
    private final String username;
    private List<User> friendList;

    public User(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.friendList = new ArrayList<User>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() { return username; }

    public List<User> getFriendList() {
        return friendList;
    }

    public void addFriend(User friend) {
        this.friendList.add(friend);
    }

    public void removeFriend(User friend) {
        this.friendList.remove(friend);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
