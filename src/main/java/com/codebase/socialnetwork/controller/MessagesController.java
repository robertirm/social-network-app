package com.codebase.socialnetwork.controller;


import com.codebase.socialnetwork.domain.Conversation;
import com.codebase.socialnetwork.domain.Message;
import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.utils.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.kordamp.ikonli.javafx.FontIcon;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class MessagesController extends MainWindowController {

    ObservableList<Conversation> conversationsObservableList = FXCollections.observableArrayList();
    ObservableList<Message> messageObservableList = FXCollections.observableArrayList();
    ObservableList<User> userObservableList = FXCollections.observableArrayList();
    ObservableList<User> receivers = FXCollections.observableArrayList();

    ObservableList<HBox> paneMessages = FXCollections.observableArrayList();

    @FXML
    ListView<Conversation> listViewConversations;
    @FXML
    Button sendMessageButton;
    @FXML
    Button createNewChatButton;
    @FXML
    TextField sendMessageTextField;
    @FXML
    ListView<HBox> listViewCurrentConversation;

    @FXML
    Label chatTitleLabel;
    @FXML
    AnchorPane replyPane;
    @FXML
    Label replyingLabel;
    @FXML
    Button stopReplyButton;

    @FXML
    Pane coloredMessagePane;
    @FXML
    Label labelNoConversations;

    public void onStopReplyButton(){
        replyPane.setVisible(false);
    }

    private void initializePaneMessages(Conversation conversation){
        List<HBox> hBoxList = new ArrayList<>();

        for(var x : conversation.getMessageList()){
            HBox hBox = new HBox();
            Label text =  new Label(x.getMessageContent());
            VBox vBox = new VBox();
            Label dateText = new Label("");
            Label replyText = new Label("");
            Button replyButton = new Button(">");
            replyButton.setId("replyButtonOnMessage");
            replyButton.setOnAction(e -> {
                replyPane.setVisible(true);
                replyingLabel.setText("replying to :" + x.getMessageContent());
            });
            replyText.setId("messageReplyText");
            dateText.setId("messageDateText");
            dateText.setText(x.getMessageDate().format(Constants.DATE_TIME_FORMATTER));
            vBox.setPadding(new Insets(10,10,10,10));
            if(x.getRepliedTo() == null) {
                vBox.getChildren().addAll(dateText,text);
            }else if (x.getRepliedTo().equals("")){
                vBox.getChildren().addAll(dateText,text);
            }
            else
            {
                replyText.setText("replied to: " + x.getRepliedTo());
                vBox.getChildren().addAll(replyText,dateText,text);
            }
            vBox.setId("vBoxMessage");
            if(x.getSender().getUsername().equals(backEndController.getCurrentUsername())){
                Region region = new Region();
                text.setContentDisplay(ContentDisplay.RIGHT);
                hBox.getChildren().addAll(region,replyButton,vBox);
                HBox.setHgrow(region, Priority.ALWAYS);
            }
            else{
                Label sender = new Label(x.getSender().getUsername() + " ");
                FontIcon userIcon = new FontIcon("fas-user-circle");
                userIcon.setId("messageUserIcon");
                userIcon.setIconSize(40);
                VBox userBox = new VBox();
                userBox.getChildren().addAll(userIcon,sender);
                hBox.getChildren().addAll(userBox,vBox,replyButton);
            }

            hBoxList.add(hBox);
        }

        paneMessages.setAll(hBoxList);
    }

    @FXML
    public void initialize(){


        messageSearchBar.textProperty().addListener(o -> handleSearch());
        createNewChatPane.setVisible(false);
        replyPane.setVisible(false);
        chatTitleLabel.setVisible(false);
        coloredMessagePane.setVisible(false);

        sendMessageButton.setDisable(true);
        sendMessageTextField.setDisable(true);
        
        usersListView.setItems(userObservableList);

        listViewConversations.setItems(conversationsObservableList);

        if(conversationsObservableList.size()==0)
            labelNoConversations.setVisible(true);
        else
            labelNoConversations.setVisible(false);


        listViewConversations.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    Conversation conversation = listViewConversations.getSelectionModel().getSelectedItem();
                    if(conversation != null) {
                        labelNoConversations.setVisible(false);
                        sendMessageButton.setDisable(false);
                        sendMessageTextField.setDisable(false);
                        initializePaneMessages(conversation);
                        chatTitleLabel.setVisible(true);
                        coloredMessagePane.setVisible(true);
                        chatTitleLabel.setText("Chat with : " + conversation.toString());
                        messageObservableList.setAll(conversation.getMessageList());
                        listViewCurrentConversation.setItems(paneMessages);
                        listViewCurrentConversation.getSelectionModel().select(messageObservableList.size() - 1);
                        listViewCurrentConversation.scrollTo(conversation.getMessageList().size());

                    }
                }
        );

        conversationsObservableList.addListener((ListChangeListener<Conversation>) c -> listViewConversations.setItems(conversationsObservableList));

//        usernameLabel.setText(backEndController.getCurrentUsername());
//        MainWindow.mainWindowController.setUsername(backEndController.getCurrentUsername());
        conversationsObservableList.setAll(backEndController.getConversations(backEndController.getCurrentUsername()));
        userObservableList.setAll(backEndController.getAllUsers());
    }

    private void handleSearch() {

        if(messageSearchBar.getText().equals(""))
            userObservableList.setAll(backEndController.getAllUsers());

        Predicate<User> p = u -> u.getUsername().contains(messageSearchBar.getText());
        userObservableList.setAll(userObservableList.stream()
                .filter(p)
                .collect(Collectors.toList()));
    }

    public void onSendMessageButtonClick(ActionEvent event) throws IOException {
        String messageText = sendMessageTextField.getText().trim();
        int conversationIndex = listViewConversations.getSelectionModel().getSelectedIndex();
        HBox message = listViewCurrentConversation.getSelectionModel().getSelectedItem();
        Conversation conversation = conversationsObservableList.get(conversationIndex);


        if (message != null && conversation!=null) {
            sendMessageTextField.clear();
            Message msg = messageObservableList.get(listViewCurrentConversation.getSelectionModel().getSelectedIndex());
            String text = msg.getMessageContent();
            if(!replyPane.isVisible()) msg.setMessageContent("");
            Message created = backEndController.createMessage(msg, messageText);
            conversation.addMessage(created);
            msg.setMessageContent(text);
            conversationsObservableList.remove(conversation);
            conversationsObservableList.add(conversationIndex,conversation);
            listViewConversations.getSelectionModel().select(conversationIndex);
            listViewCurrentConversation.scrollTo(conversation.getMessageList().size());
            replyPane.setVisible(false);
        }
    }


    @FXML
    AnchorPane createNewChatPane;
    @FXML
    TextField sendNewMessageTextField;
    @FXML
    Button sendNewMessageButton;
    @FXML
    Button addUserButton;
    @FXML
    Button removeUserButton;
    @FXML
    Button cancelNewChatButton;
    @FXML
    ListView<User> usersListView;
    @FXML
    ListView<User> receiversListView;
    @FXML
    TextField messageSearchBar;

    @FXML
    Label chatsLabel;

    private void changeToListView(){
        labelNoConversations.setVisible(false);
        chatsLabel.setVisible(true);
        listViewCurrentConversation.setVisible(true);
        sendMessageButton.setVisible(true);
        sendMessageTextField.setVisible(true);
        createNewChatPane.setVisible(false);
    }

    private void changeToNewChatView(){
        if(conversationsObservableList.size()==0)
            labelNoConversations.setVisible(true);
        else
            labelNoConversations.setVisible(false);
        chatsLabel.setVisible(false);
        listViewCurrentConversation.setVisible(false);
        sendMessageButton.setVisible(false);
        sendMessageTextField.setVisible(false);
        createNewChatPane.setVisible(true);
    }


    public void onCreateNewChatButtonClick(ActionEvent event){
        receivers.clear();
        coloredMessagePane.setVisible(false);
        listViewConversations.setVisible(false);
        chatTitleLabel.setVisible(false);
        replyPane.setVisible(false);
        changeToNewChatView();
    }

    public void onCancelNewChatButtonClick(ActionEvent event) {
        coloredMessagePane.setVisible(true);
        chatTitleLabel.setVisible(true);
        listViewConversations.setVisible(true);
        changeToListView();
    }

    public void onSendNewMessageButtonClick(ActionEvent event) {
        List<User> rec = new ArrayList<>(receivers);
        String text = sendNewMessageTextField.getText().trim();
        Message message = backEndController.createMessage(text,rec);
        List<Message> messageList = new ArrayList<>();
        messageList.add(message);
        conversationsObservableList.add(new Conversation(messageList));
        listViewConversations.getSelectionModel().select(conversationsObservableList.size()-1);
        listViewCurrentConversation.scrollTo(conversationsObservableList.size()-1);
        sendNewMessageTextField.clear();
        coloredMessagePane.setVisible(true);
        chatTitleLabel.setVisible(true);
        listViewConversations.setVisible(true);
        changeToListView();
    }

    public void onAddUserButtonClick(ActionEvent event) {
        User user = usersListView.getSelectionModel().getSelectedItem();
        if(user != null && !receivers.contains(user) && !user.getUsername().equals(backEndController.getCurrentUsername())){
            receivers.add(user);
            receiversListView.setItems(receivers);
        }
    }

    public void onRemoveUserButtonClick(ActionEvent event) {
        User user = receiversListView.getSelectionModel().getSelectedItem();
        if(user != null){
            receivers.remove(user);
            receiversListView.setItems(receivers);
        }
    }

}
