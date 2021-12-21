package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;

public class SettingsController extends MainWindowController {

    @FXML
    public void initialize() {
       MainWindow.mainWindowController.setUsername(backEndController.getCurrentUsername());
    }

    private void changeTheme(ActionEvent event, String css){
        Scene scene = (Scene)((Node) event.getSource()).getScene();
        MainWindow.setStylesheet(scene,css);
    }


    @FXML
    public void changeTheme1(ActionEvent event){
        changeTheme(event,MainWindow.THEME_1);
    }
    @FXML
    public void changeTheme2(ActionEvent event){
        changeTheme(event,MainWindow.THEME_2);
    }
    @FXML
    public void changeTheme3(ActionEvent event){
        changeTheme(event,MainWindow.THEME_3);
    }
    @FXML
    public void changeTheme4(ActionEvent event){
        changeTheme(event,MainWindow.THEME_4);
    }
    @FXML
    public void changeTheme5(ActionEvent event){
        changeTheme(event,MainWindow.THEME_5);
    }
    @FXML
    public void changeTheme6(ActionEvent event){
        changeTheme(event,MainWindow.THEME_6);
    }


}
