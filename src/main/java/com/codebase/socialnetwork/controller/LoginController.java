package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.MainWindow;
import com.codebase.socialnetwork.domain.exception.WrongUsernameException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController extends MainWindowController {

    @FXML
    TextField usernameLoginTextField;
    @FXML
    TextField passwordLoginTextField;
    @FXML
    Label errorLoginLabel;

    @FXML
    public void initialize() {
        MainWindow.setVisibility(false);
    }

    @FXML
    void loginAction(ActionEvent event){
        String username = usernameLoginTextField.getText().trim();
        try{
            backEndController.login(username);
            switchToProfilePage(event);
        }catch (WrongUsernameException | IOException e){
            errorLoginLabel.setText("The user doesn't exist!");
        }
    }

}
