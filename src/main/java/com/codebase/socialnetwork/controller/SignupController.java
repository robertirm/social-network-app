package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class SignupController {
    public UserService<Long, User> userService;

    public void setService(UserService<Long, User> userService) {
        this.userService = userService;
    }

    @FXML
    TextField usernameTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    TextField confirmPasswordTextField;
    @FXML
    TextField firstNameTextField;
    @FXML
    TextField lastNameTextField;

    public void signUpAction(ActionEvent event){
        //TODO
    }

    @FXML
    public void switchToLogin(ActionEvent event) throws IOException {
        URL url = Paths.get("./src/main/resources/com/codebase/socialnetwork/views/loginPage.fxml").toUri().toURL();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(url);

        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        LoginController controller = loader.getController();
        controller.setService(userService);

        Stage window = (Stage)( (Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
