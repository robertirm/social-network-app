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

public class ProfileController extends SceneController{

    public void setBackEndController(Controller backEndController) {
        this.backEndController = backEndController;
        usernameLabel.setText(backEndController.getCurrentUsername());
    }

    @FXML
    public Label usernameLabel;

}
