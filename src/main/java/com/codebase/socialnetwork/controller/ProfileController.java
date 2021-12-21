package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.MainWindow;
import javafx.fxml.FXML;


public class ProfileController extends MainWindowController {

    @FXML
    public void initialize() {
        MainWindow.mainWindowController.setUsername(backEndController.getCurrentUsername());
        MainWindow.setVisibility(true);
    }
}
