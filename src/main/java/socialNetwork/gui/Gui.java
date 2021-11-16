package socialNetwork.gui;

public interface Gui {
    void startGui();
    void exitApp();
    void printAll();
    void login();
    void logout();
    void signup();
    void deleteAccount();
    void addFriend();
    void removeFriend();
    void printNumberOfCommunities();
    void printTheMostSocialCommunity();
    void printFriendListForUser();
    void printFriendListForUserByMonth();
    void printFriendListByStatus();
    void approveFriendship();
    void rejectFriendship();
}
