package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.MainWindow;
import javafx.fxml.FXML;

//public class SettingsController extends SceneController {
public class SettingsController extends MainWindowController {

//    public void setBackEndController(Controller backEndController) {
//        this.backEndController = backEndController;
//        usernameLabel.setText(backEndController.getCurrentUsername());
//
//    }


    @FXML
    public void initialize() {

//        usernameLabel.setText(backEndController.getCurrentUsername());
        MainWindow.mainWindowController.setUsername(backEndController.getCurrentUsername());
    }

//    @FXML
//    public Label usernameLabel;

}
