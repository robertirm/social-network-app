package com.codebase.socialnetwork.controller;


import com.codebase.socialnetwork.domain.Conversation;
import com.codebase.socialnetwork.domain.Message;
import com.codebase.socialnetwork.domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MessagesController extends SceneController{

    ObservableList<Conversation> conversationsObservableList = FXCollections.observableArrayList();
    ObservableList<Message> messageObservableList = FXCollections.observableArrayList();
    ObservableList<User> userObservableList = FXCollections.observableArrayList();
    ObservableList<User> receivers = FXCollections.observableArrayList();


    public void setBackEndController( Controller backEndController) {
        this.backEndController = backEndController;
        usernameLabel.setText(backEndController.getCurrentUsername());
        conversationsObservableList.setAll(backEndController.getConversations(backEndController.getCurrentUsername()));
        userObservableList.setAll(backEndController.getAllUsers());
        this.initialize();
    }

    @FXML
    public Label usernameLabel;
    @FXML
    ListView<Conversation> listViewConversations;
    @FXML
    Button sendMessageButton;
    @FXML
    Button createNewChatButton;
    @FXML
    TextField sendMessageTextField;
    @FXML
    ListView<Message> listViewCurrentConversation;

    @FXML
    public void initialize(){
        createNewChatPane.setVisible(false);

        sendMessageButton.setDisable(true);
        sendMessageTextField.setDisable(true);

        usersListView.setItems(userObservableList);

        listViewConversations.setItems(conversationsObservableList);
        listViewConversations.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    Conversation conversation = listViewConversations.getSelectionModel().getSelectedItem();
                    if(conversation != null) {
                        sendMessageButton.setDisable(false);
                        sendMessageTextField.setDisable(false);
                        messageObservableList.setAll(conversation.getMessageList());
                        listViewCurrentConversation.setItems(messageObservableList);
                        listViewCurrentConversation.getSelectionModel().select(messageObservableList.size() - 1);
                    }
                }
        );

        conversationsObservableList.addListener((ListChangeListener<Conversation>) c -> listViewConversations.setItems(conversationsObservableList));

    }

    public void onSendMessageButtonClick(ActionEvent event) throws IOException {
        String messageText = sendMessageTextField.getText().trim();
        int conversationIndex = listViewConversations.getSelectionModel().getSelectedIndex();
        Message message = listViewCurrentConversation.getSelectionModel().getSelectedItem();
        Conversation conversation = conversationsObservableList.get(conversationIndex);


        if (message != null && conversation!=null) {
            sendMessageTextField.clear();
            Message created = backEndController.createMessage(message, messageText);
            conversation.addMessage(created);
            conversationsObservableList.remove(conversation);
            conversationsObservableList.add(conversationIndex,conversation);
            listViewConversations.getSelectionModel().select(conversationIndex);
            listViewCurrentConversation.scrollTo(conversation.getMessageList().size());
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



    private void changeToListView(){
        listViewCurrentConversation.setVisible(true);
        sendMessageButton.setVisible(true);
        sendMessageTextField.setVisible(true);
        createNewChatPane.setVisible(false);
    }

    private void changeToNewChatView(){
        listViewCurrentConversation.setVisible(false);
        sendMessageButton.setVisible(false);
        sendMessageTextField.setVisible(false);
        createNewChatPane.setVisible(true);
    }


    public void onCreateNewChatButtonClick(ActionEvent event){
        receivers.clear();
        changeToNewChatView();
    }

    public void onCancelNewChatButtonClick(ActionEvent event) {
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
