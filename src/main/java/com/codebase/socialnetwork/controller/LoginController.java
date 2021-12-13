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

public class LoginController extends SceneController{

    public void setBackEndController(Controller backEndController) {
        this.backEndController = backEndController;
    }

    @FXML
    TextField usernameLoginTextField;
    @FXML
    TextField passwordLoginTextField;
    @FXML
    Label errorLoginLabel;

    @FXML void loginAction(ActionEvent event){
        String username = usernameLoginTextField.getText().trim();
        try{
            backEndController.login(username);
            switchToProfilePage(event);
        }catch (WrongUsernameException | IOException e){
            errorLoginLabel.setText("The user doesn't exist!");
        }
    }

}
