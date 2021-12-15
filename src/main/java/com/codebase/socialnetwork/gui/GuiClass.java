package com.codebase.socialnetwork.gui;

import com.codebase.socialnetwork.controller.Controller;
import com.codebase.socialnetwork.domain.Friendship;
import com.codebase.socialnetwork.domain.Message;
import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.domain.exception.*;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static com.codebase.socialnetwork.utils.Constants.DATE_TIME_FORMATTER;

public class GuiClass implements Gui {

    public final Controller controller;
    Scanner in;

    public GuiClass(Controller controller) {
        this.controller = controller;
        in = new Scanner(System.in);
    }

    private void printOptions() {
        System.out.println();
        System.out.println("Current user: " + controller.getCurrentUsername());
        System.out.println();
        System.out.println("Choose an option");
        System.out.println("exit");
        System.out.println("1. Print all");
        System.out.println("2. Log In");
        System.out.println("3. Log Out");
        System.out.println("4. Sign Up");
        System.out.println("5. Delete account");
        System.out.println("6. Add friend");
        System.out.println("7. Remove friend");
        System.out.println("8. Print number of communities");
        System.out.println("9. Print the most social community");
        System.out.println("10. Get friend list for a user");
        System.out.println("11. Get friend list for a user by month");
        System.out.println("12. Get friend list by status");
        System.out.println("13. Approve friendship");
        System.out.println("14. Reject friendship");
        System.out.println("15. Send Message");
        System.out.println("16. Show Inbox");
        System.out.println("17. Show Conversations");
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
                        this.logout();
                        break;
                    case "4":
                        this.signup();
                        break;
                    case "5":
                        this.deleteAccount();
                        break;
                    case "6":
                        this.addFriend();
                        break;
                    case "7":
                        this.removeFriend();
                        break;
                    case "8":
                        this.printNumberOfCommunities();
                        break;
                    case "9":
                        this.printTheMostSocialCommunity();
                        break;
                    case "10":
                        this.printFriendListForUser();
                        break;
                    case "11":
                        this.printFriendListForUserByMonth();
                        break;
                    case "12":
                        this.printFriendListByStatus();
                        break;
                    case  "13":
                        this.approveFriendship();
                        break;
                    case "14":
                        this.rejectFriendship();
                        break;
                    case "15":
                        this.sendMessage();
                        break;
                    case "16":
                        this.showInbox();
                        break;
                    case "17":
                        this.showConversations();
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
        Iterable<User> allUsers = controller.getAllUsers();
        for(User user : allUsers){
            System.out.println(user.toString());
        }

        System.out.println();

        System.out.println("Friendships: ");
        System.out.println();
        Iterable<Friendship> allFriendships = controller.getAllFriendships();
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
    public void logout() {
        this.controller.logout();
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
        controller.getAllFriendsForUser(username).forEach(
                x -> System.out.println(x.getUser().getLastName() + " | " +
                                        x.getUser().getFirstName() + " | " +
                                        x.getDataOfFriendship().format(DATE_TIME_FORMATTER))
        );
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
        this.controller.getAllFriendsForUserByMonth(username, dateTime).forEach(
                x -> System.out.println(x.getUser().getLastName() + " | " +
                        x.getUser().getFirstName() + " | " +
                        x.getDataOfFriendship().format(DATE_TIME_FORMATTER))
        );
    }

    @Override
    public void printFriendListByStatus() {
        System.out.println("Please enter the status");
        System.out.println("Choose 'pending' or 'approved' or 'rejected'");
        String status = in.nextLine();
        this.controller.getAllFriendsByStatus(status).forEach(System.out::println);
    }

    @Override
    public void approveFriendship() {
        this.controller.getAllFriendsByStatus("pending").forEach(System.out::println);
        System.out.println("Please enter the friend username:");
        String friendUsername = in.nextLine();
        this.controller.setFriendshipStatus(friendUsername, "approved");
    }

    @Override
    public void rejectFriendship() {
        this.controller.getAllFriendsByStatus("pending").forEach(System.out::println);
        System.out.println("Please enter the friend username:");
        String friendUsername = in.nextLine();
        this.controller.setFriendshipStatus(friendUsername, "rejected");
    }

    @Override
    public void sendMessage() {
        if(this.controller.getCurrentUsername() == null)
            System.out.println("Please login first");
        else{
            System.out.println("Write your message:");
            String text = in.nextLine();
            System.out.println("Send to: (usernames separated by comma)");
            String receivers = in.nextLine();
            String[] users = receivers.split(",");
            Message message = new Message(controller.getUserById(controller.getCurrentUserId()),LocalDateTime.now(),text, false);
            for(String user: users){
                message.addReceiver(controller.getUserByUsername(user));
            }
            controller.sendMessage(message);
            System.out.println("Message sent!");
        }
    }
    @Override
    public void showInbox() {
        if(this.controller.getCurrentUsername() == null)
            System.out.println("Please login first");
        else{
            List<Message> inbox = controller.getReceivedMessages(controller.getCurrentUsername());
            System.out.println("You have " + inbox.size()+" message(s)!");
            for (Message message: inbox){
                System.out.println("ID:"+message.getId() + " | " + "DATE: " + message.getMessageDate().format(DATE_TIME_FORMATTER) + " | " + "FROM: "+message.getSender().getUsername() + " | " + "MESSAGE: " + message.getMessageContent());
            }
            System.out.println("\n Do you want to reply to any message? (type 'yes' or 'no)");
            String answer = in.nextLine();
            if(answer.equals("yes")){
                System.out.println("Enter the id of the message to reply to:");
                Long id = in.nextLong();
                in.nextLine();
                Message message = controller.getMessageById(id);
                if(!canReply(id, inbox)) {
                    System.out.println("You can't reply to this message");
                }
                else{
                    System.out.println("Reply all? (type 'yes' or 'no)");
                    String answer2 = in.nextLine();
                    if(answer2.equals("yes")){
                        System.out.println("Write your message:");
                        String text = in.nextLine();

                        Message replyMessage = new Message(controller.getUserByUsername(controller.getCurrentUsername()), LocalDateTime.now(),text, true);

                        replyMessage.addReceiver(message.getSender());
                        for(User receiver: message.getReceivers()){
                            if(!receiver.getId().equals(controller.getCurrentUserId()))
                                replyMessage.addReceiver(receiver);
                        }

                        message.addReply(replyMessage);

                        controller.sendMessage(replyMessage);
                        controller.sendMessage(message);
                    }
                    else{
                        System.out.println("Write your message:");
                        String text = in.nextLine();

                        Message replyMessage = new Message(controller.getUserByUsername(controller.getCurrentUsername()), LocalDateTime.now(),text, true);
                        replyMessage.addReceiver(message.getSender());

                        message.addReply(replyMessage);

                        controller.sendMessage(replyMessage);
                        controller.sendMessage(message);
                    }
                }
            }
        }
    }
    private boolean canReply(Long id,List<Message> inbox){
        for (Message message: inbox){
            if(message.getId().equals(id))
                return true;
        }
        return false;
    }


    @Override
    public void showConversations() {
        if(this.controller.getCurrentUsername() == null)
            System.out.println("Please login first");
        else{
            System.out.println("Enter username:");
            String username = in.nextLine();
            if(controller.getUserByUsername(username) == null){
                System.out.println("This username doesn't exist");
            }
            else {
                controller.getConversations(controller.getCurrentUsername(), username)
                        .stream()
                        .sorted((o1, o2) -> o2.get(0).getMessageDate().compareTo(o1.get(0).getMessageDate()))
                        .forEach(list -> {
                            System.out.println("=====================================");
                            list.forEach(el -> {
                                System.out.println("ID:" + el.getId() + " | " + "DATE: " + el.getMessageDate().format(DATE_TIME_FORMATTER) + " | " + "FROM: " + el.getSender().getUsername() + " | " + "MESSAGE: " + el.getMessageContent());
                            });
                            System.out.println("=====================================");
                        });
            }
        }
    }

}
