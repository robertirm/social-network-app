package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.exception.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;


public class SignupController extends MainWindowController {

    @FXML
    public void initialize() {
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
