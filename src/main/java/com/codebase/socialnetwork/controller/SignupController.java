package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.domain.exception.ValidationException;
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

public class SignupController extends SceneController{

    public void setBackEndController(Controller backEndController) {
        this.backEndController = backEndController;
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

    @FXML
    Label errorSignUpLabel;

    public void signUpAction(ActionEvent event) throws IOException {
        String username = usernameTextField.getText().trim();
        String firstName = firstNameTextField.getText().trim();
        String lastName = lastNameTextField.getText().trim();

        try{
            backEndController.signup(firstName,lastName,username);
            backEndController.login(username);
            switchToProfilePage(event);
        }catch (ValidationException e){
            errorSignUpLabel.setText(e.getMessage());
        }

    }

}
