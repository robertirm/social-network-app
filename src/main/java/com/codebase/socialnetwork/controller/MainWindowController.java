package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainWindowController {

    public static Controller backEndController;

    @FXML
    private StackPane pageHolder;

    @FXML
    private Label usernameLabel;

    public void setPage(Node node){
        pageHolder.getChildren().setAll(node);
    }

    public void setUsername(String username){
        usernameLabel.setText(username);
    }

    public static void setBackEndController(Controller backEndController) {
        MainWindowController.backEndController = backEndController;
    }

    private void switchPage(ActionEvent event, String pageName) throws IOException {
        switch (pageName){
            case MainWindow.LOGIN -> {
                MainWindow.loadPage(MainWindow.LOGIN);
            }
            case MainWindow.SIGNUP -> {
                MainWindow.loadPage(MainWindow.SIGNUP);
            }
            case MainWindow.PROFILE -> {
                MainWindow.loadPage(MainWindow.PROFILE);
            }
            case MainWindow.FRIENDS -> {
                MainWindow.loadPage(MainWindow.FRIENDS);
            }
            case MainWindow.MESSAGES -> {
                MainWindow.loadPage(MainWindow.MESSAGES);
            }
            case MainWindow.EVENTS -> {
                MainWindow.loadPage(MainWindow.EVENTS);
            }
            case MainWindow.STATISTICS -> {
                MainWindow.loadPage(MainWindow.STATISTICS);
            }
            case MainWindow.SETTINGS -> {
                MainWindow.loadPage(MainWindow.SETTINGS);
            }
        }
    }

    @FXML
    public void switchToLoginPage(ActionEvent event) throws IOException {
        switchPage(event, MainWindow.LOGIN);
    }

    @FXML
    public void switchToSignUpPage(ActionEvent event) throws IOException{
        switchPage(event, MainWindow.SIGNUP);
    }

    @FXML
    public void switchToProfilePage(ActionEvent event) throws IOException{
        switchPage(event, MainWindow.PROFILE);
    }

    @FXML
    public void switchToFriendsPage(ActionEvent event) throws IOException{
        switchPage(event, MainWindow.FRIENDS);
    }

    @FXML
    public void switchToMessagesPage(ActionEvent event) throws IOException{
        switchPage(event, MainWindow.MESSAGES);
    }

    @FXML
    public void switchToEventsPage(ActionEvent event) throws IOException{
        switchPage(event, MainWindow.EVENTS);
    }

    @FXML
    public void switchToStatisticsPage(ActionEvent event) throws IOException{
        switchPage(event, MainWindow.STATISTICS);
    }

    @FXML
    public void switchToSettingsPage(ActionEvent event) throws IOException{
        switchPage(event, MainWindow.SETTINGS);
    }

    @FXML
    public void logoutAction(ActionEvent event) throws IOException{
        usernameLabel.setText("no user");
        backEndController.logout();
        switchPage(event, MainWindow.LOGIN);
    }
}
