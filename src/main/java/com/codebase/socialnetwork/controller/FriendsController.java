package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.FriendDTO;
import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FriendsController {
    ObservableList<FriendDTO> observableListFriendDTO = FXCollections.observableArrayList();
    List<FriendDTO> friendDTOList;
    public Controller backEndController;

    public void setBackEndController(Controller backEndController) {
        this.backEndController = backEndController;
        usernameLabel.setText(backEndController.getCurrentUsername());
        observableListFriendDTO.setAll(backEndController.getAllFriendsForUser(usernameLabel.getText()));
        friendDTOList = new ArrayList<>(observableListFriendDTO);
    }

    @FXML
    public Label usernameLabel;

   @FXML
    TextField textFieldName;

   @FXML
    GridPane gridPaneFriends;

   @FXML
    ScrollPane scrollPaneFriends;

   @FXML
   public void initialize() {
       observableListFriendDTO.addListener(new ListChangeListener<FriendDTO>() {
           @Override
           public void onChanged(Change<? extends FriendDTO> change) {
               updateGridPaneFriends();
           }
       });
       textFieldName.textProperty().addListener(o -> handleSearch());
       scrollPaneFriends.setContent(gridPaneFriends);
   }

   private void handleSearch() {
       Predicate<FriendDTO> p = f -> f.getUser().getUsername().contains(textFieldName.getText());

       observableListFriendDTO.setAll(friendDTOList.stream()
               .filter(p)
               .collect(Collectors.toList())
       );
   }

   private void updateGridPaneFriends(){
       int numberOfColumns = gridPaneFriends.getColumnCount();
       int currentColumn = 0;
       int currentRow = 0;

       gridPaneFriends.getChildren().clear();
       for (FriendDTO friendDTO : observableListFriendDTO) {
           Label friendUsername = new Label(friendDTO.getUser().getUsername());
           gridPaneFriends.add(friendUsername, currentColumn, currentRow);
           currentColumn++;
           if (currentColumn >= numberOfColumns) {
               currentRow++;
               currentColumn = 0;
           }
       }

       scrollPaneFriends.setContent(gridPaneFriends);
   }


    @FXML
    public void switchToProfilePage(ActionEvent event) throws IOException {
        URL url = Paths.get("./src/main/resources/com/codebase/socialnetwork/views/profilePage.fxml").toUri().toURL();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(url);

        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        ProfileController controller = loader.getController();
        controller.setBackEndController(backEndController);

        Stage window = (Stage)( (Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void switchToFriendsPage(ActionEvent event) throws IOException {
        URL url = Paths.get("./src/main/resources/com/codebase/socialnetwork/views/friendsPage.fxml").toUri().toURL();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(url);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        FriendsController controller = loader.getController();
        controller.setBackEndController(backEndController);


        Stage window = (Stage)( (Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void switchToMessagesPage(ActionEvent event) throws IOException {
        URL url = Paths.get("./src/main/resources/com/codebase/socialnetwork/views/messagesPage.fxml").toUri().toURL();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(url);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);


        MessagesController controller = loader.getController();
        controller.setBackEndController(backEndController);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void switchToSettingsPage(ActionEvent event) throws IOException {
        URL url = Paths.get("./src/main/resources/com/codebase/socialnetwork/views/settingsPage.fxml").toUri().toURL();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(url);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);


        SettingsController controller = loader.getController();
        controller.setBackEndController(backEndController);


        Stage window = (Stage)( (Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
