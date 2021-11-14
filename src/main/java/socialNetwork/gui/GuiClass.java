package socialNetwork.gui;

import socialNetwork.controller.Controller;
import socialNetwork.domain.Entity;
import socialNetwork.domain.Friendship;
import socialNetwork.domain.User;
import socialNetwork.domain.exception.*;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static socialNetwork.utils.Constants.DATE_TIME_FORMATTER;

public class GuiClass<ID, E extends Entity<ID>> implements Gui<ID, E>{

    public final Controller<ID, E> controller;
    Scanner in;

    public GuiClass(Controller<ID, E> controller) {
        this.controller = controller;
        in = new Scanner(System.in);
    }

    private void printOptions() {
        System.out.println();
        System.out.println("Current user: " + String.valueOf(controller.getCurrentUsername()));
        System.out.println();
        System.out.println("Choose an options.");
        System.out.println("exit");
        System.out.println("1. Print all.");
        System.out.println("2. Log In.");
        System.out.println("3. Sign Up.");
        System.out.println("4. Delete account.");
        System.out.println("5. Add friend.");
        System.out.println("6. Remove friend.");
        System.out.println("7. Print number of communities.");
        System.out.println("8. Print the most social community.");
        System.out.println("9. Get friend list for a user");
        System.out.println("10. Get friend list for a user by month");
        System.out.println("11. Get friend list by status");
        System.out.println();
    }

    @Override
    public void startGui() {
        // this.controller.startApp();
        boolean running = true;
        while (running){
            try{
                this.printOptions();
                String input = in.nextLine();
                switch (input){
                    case "exit":
                        // this.exitApp();
                        running = false;
                        break;
                    case "1":
                        this.printAll();
                        break;
                    case "2":
                        this.login();
                        break;
                    case "3":
                        this.signup();
                        break;
                    case "4":
                        this.deleteAccount();
                        break;
                    case "5":
                        this.addFriend();
                        break;
                    case "6":
                        this.removeFriend();
                        break;
                    case "7":
                        this.printNumberOfCommunities();
                        break;
                    case "8":
                        this.printTheMostSocialCommunity();
                        break;
                    case "9":
                        this.printFriendListForUser();
                        break;
                    case "10":
                        this.printFriendListForUserByMonth();
                        break;
                    case  "11":
                        this.printFriendListByStatus();
                        break;
                    default:
                        System.out.println("Your option seem to be not exists, please try another.");
                }

            }
            catch (ValidationException exception){
                System.out.println(exception.getMessage());
            }
            catch (IdNullException exception){
                System.out.println(exception.getMessage());
            }
            catch (EntityNullException exception){
                System.out.println(exception.getMessage());
            }
            catch (WrongUsernameException exception){
                System.out.println(exception.getMessage());
            }
            catch (LogInException exception){
                System.out.println(exception.getMessage());
            }
            catch (DateTimeException exception){
                System.out.println(exception.getMessage());
            }
        }
    }

    @Override
    public void exitApp() {
        this.controller.exitApp();
    }

    @Override
    public void printAll() {
        System.out.println("Users: ");
        System.out.println();
        Iterable<User> allUsers = (Iterable<User>) controller.getAllUsers();
        for(User user : allUsers){
            System.out.println(user.toString());
//            System.out.println("Friends: ");
//            for(User friend : user.getFriendList()){
//                System.out.println("    " + friend.toString());
//            }
//            System.out.println();
        }

        System.out.println();

        System.out.println("Friendships: ");
        System.out.println();
        Iterable<Friendship> allFriendships = (Iterable<Friendship>) controller.getAllFriendships();
        for(Friendship friendship : allFriendships){
            System.out.println(friendship.toString());
        }

        System.out.println();
    }

    @Override
    public void login() {
        this.printAll();
        System.out.println("Please enter the username:");
        String username = in.nextLine();
        this.controller.login(username);
    }

    @Override
    public void signup() {
        System.out.println("Please enter the first name:");
        String firstName = in.nextLine();
        System.out.println("Please enter the last name:");
        String lastName = in.nextLine();
        System.out.println("Please enter the username:");
        String username = in.nextLine();
        this.controller.signup(firstName, lastName, username);
    }

    @Override
    public void deleteAccount() {
        System.out.println("Are you sure want to delete this account?");
        System.out.println("Please enter 'yes' or 'no'");

        String answer = in.nextLine();
        if(answer.equals("yes")){
            this.controller.deleteAccount();
        }
    }

    @Override
    public void addFriend() {
        System.out.println("Please enter the username:");
        String username = in.nextLine();
        this.controller.addFriend(username);
    }

    @Override
    public void removeFriend() {
        System.out.println("Please enter the username:");
        String username = in.nextLine();
        this.controller.removeFriend(username);
    }

    @Override
    public void printNumberOfCommunities() {
        System.out.print("Number of communities: ");
        System.out.println(this.controller.getNumberOfCommunities());
    }

    @Override
    public void printTheMostSocialCommunity() {
        List<Integer> mostSocialCommunity =  this.controller.getTheMostSocialCommunity();
        for(Integer id : mostSocialCommunity){
            System.out.print(id.toString() + " ");
        }
    }

    @Override
    public void printFriendListForUser() {
        System.out.println("Please enter the username:");
        String username = in.nextLine();
        controller.getAllFriendsForUser(username).forEach(System.out::println);
    }

    @Override
    public void printFriendListForUserByMonth() {
        System.out.println("Please enter the username:");
        String username = in.nextLine();
        System.out.println("Please select month:");
        String month = in.nextLine();
        if(month.length() == 1){
            month = "0" + month;
        }
        LocalDateTime dateTime = LocalDateTime.parse("1000-" + month + "-01 00:00", DATE_TIME_FORMATTER);
        this.controller.getAllFriendsForUserByMonth(username, dateTime).forEach(System.out::println);
    }

    @Override
    public void printFriendListByStatus() {
        System.out.println("Please enter the status");
        System.out.println("Choose 'pending' or 'approved' or 'rejected'");
        String status = in.nextLine();
        this.controller.getAllFriendsByStatus(status).forEach(System.out::println);
    }
}
