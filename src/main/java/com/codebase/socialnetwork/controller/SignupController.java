package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.exception.ValidationException;
import com.codebase.socialnetwork.domain.exception.WrongUsernameException;
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
        String password = passwordTextField.getText().trim();
        String confirmPassword = confirmPasswordTextField.getText().trim();
        if(!password.equals(confirmPassword)){
            errorSignUpLabel.setText("Password doesn't match");
            return;
        }

        if ("".equals(password)) {
            errorSignUpLabel.setText("Password can't be empty. \n");
            return;
        }

        try{
            backEndController.signup(firstName,lastName,username, password);
            backEndController.login(username, password);
            switchToWelcomePage(event);
        }catch (ValidationException | WrongUsernameException e){
            errorSignUpLabel.setText(e.getMessage());
        }

    }
}
