package socialNetwork.gui;

import socialNetwork.domain.Entity;

public interface Gui<ID, E extends Entity<ID>> {
    void startGui();
    void exitApp();
    void printAll();
    void login();
    void signup();
    void deleteAccount();
    void addFriend();
    void removeFriend();
    void printNumberOfCommunities();
    void printTheMostSocialCommunity();
}
