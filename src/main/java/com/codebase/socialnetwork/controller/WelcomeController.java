package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.MainWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WelcomeController extends MainWindowController{

    @FXML
    Label welcomeLabel;

    @FXML
    public void initialize() {
        MainWindow.setVisibility(true);
        MainWindow.mainWindowController.setUsername(backEndController.getCurrentUsername());
        welcomeLabel.setText("Welcome back, " + backEndController.getCurrentUsername() + "!");
    }
}
