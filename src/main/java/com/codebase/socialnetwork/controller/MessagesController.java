package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class MessagesController {
    public UserService<Long, User> userService;

    public void setService(UserService<Long, User> userService) {
        this.userService = userService;
        usernameLabel.setText(userService.getCurrentUsername());

    }

    @FXML
    public Label usernameLabel;


    @FXML
    public void switchToProfilePage(ActionEvent event) throws IOException {
        URL url = Paths.get("./src/main/resources/com/codebase/socialnetwork/views/profilePage.fxml").toUri().toURL();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(url);

        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        ProfileController controller = loader.getController();
        controller.setService(userService);

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
        controller.setService(userService);


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
        controller.setService(userService);

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
        controller.setService(userService);


        Stage window = (Stage)( (Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
