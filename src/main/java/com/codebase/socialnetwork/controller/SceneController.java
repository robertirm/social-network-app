package com.codebase.socialnetwork.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class SceneController {
    public Controller backEndController;

    public enum PageName {
        LOGIN("./src/main/resources/com/codebase/socialnetwork/views/loginPage.fxml"),
        SIGNUP("./src/main/resources/com/codebase/socialnetwork/views/signupPage.fxml"),
        PROFILE("./src/main/resources/com/codebase/socialnetwork/views/profilePage.fxml"),
        FRIENDS("./src/main/resources/com/codebase/socialnetwork/views/friendsPage.fxml"),
        MESSAGES("./src/main/resources/com/codebase/socialnetwork/views/messagesPage.fxml"),
        SETTINGS("./src/main/resources/com/codebase/socialnetwork/views/settingsPage.fxml");

        private final String url;

        public String getUrl() {
            return url;
        }

        PageName(String s) {
            this.url = s;
        }
    }

    private void switchPage(ActionEvent event, PageName pageName) throws IOException {
        URL url = Paths.get(pageName.getUrl()).toUri().toURL();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(url);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        switch (pageName){
            case LOGIN -> {
                LoginController controller = loader.getController();
                controller.setBackEndController(backEndController);
            }
            case SIGNUP -> {
                SignupController controller = loader.getController();
                controller.setBackEndController(backEndController);
            }
            case PROFILE -> {
                ProfileController controller = loader.getController();
                controller.setBackEndController(backEndController);
            }
            case FRIENDS -> {
                FriendsController controller = loader.getController();
                controller.setBackEndController(backEndController);

            }
            case MESSAGES -> {
                MessagesController controller = loader.getController();
                controller.setBackEndController(backEndController);

            }
            case SETTINGS -> {
                SettingsController controller = loader.getController();
                controller.setBackEndController(backEndController);
            }
        }

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void switchToLogin(ActionEvent event) throws IOException{
        switchPage(event, PageName.LOGIN);
    }

    @FXML
    public void switchToSignUp(ActionEvent event) throws IOException{
        switchPage(event, PageName.SIGNUP);
    }

    @FXML
    public void switchToProfilePage(ActionEvent event) throws IOException{
        switchPage(event, PageName.PROFILE);
    }

    @FXML
    public void switchToFriendsPage(ActionEvent event) throws IOException{
        switchPage(event, PageName.FRIENDS);
    }

    @FXML
    public void switchToMessagesPage(ActionEvent event) throws IOException{
        switchPage(event, PageName.MESSAGES);
    }

    @FXML
    public void switchToSettingsPage(ActionEvent event) throws IOException{
        switchPage(event, PageName.SETTINGS);
    }
}
