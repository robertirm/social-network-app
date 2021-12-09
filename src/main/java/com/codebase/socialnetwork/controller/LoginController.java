package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.domain.exception.WrongUsernameException;
import com.codebase.socialnetwork.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class LoginController {
    public UserService<Long, User> userService;

    public void setService(UserService<Long, User> userService) {
        this.userService = userService;

    }


    @FXML
    TextField usernameLoginTextField;
    @FXML
    TextField passwordLoginTextField;
    @FXML
    Label errorLoginLabel;

    @FXML void loginAction(ActionEvent event){
        String username = usernameLoginTextField.getText();
        try{
            userService.login(username);
            switchToProfilePage(event);
        }catch (WrongUsernameException | IOException e){
            errorLoginLabel.setText("The user doesn't exist!");
        }
    }

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
    public void switchToSignUp(ActionEvent event) throws IOException {
        URL url = Paths.get("./src/main/resources/com/codebase/socialnetwork/views/signupPage.fxml").toUri().toURL();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(url);

        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        SignupController controller = loader.getController();
        controller.setService(userService);

        Stage window = (Stage)( (Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

}
