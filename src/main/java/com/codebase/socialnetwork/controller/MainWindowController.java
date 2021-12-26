package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainWindowController {

    public static Controller backEndController;

    @FXML
    private StackPane pageHolder;

    @FXML
    protected VBox mainPageButtonLayout;

    @FXML
    protected HBox mainPageTopLayout;

    @FXML
    protected Pane coloredLoginPane;

    @FXML
    private Label usernameLabel;

    @FXML
    protected Label currentPageLabel;

    public void setPage(Node node){
        pageHolder.getChildren().setAll(node);
    }

    public void setUsername(String username){
        usernameLabel.setText(username);
    }

    public void setButtonsLayoutVisible(boolean value){
        mainPageButtonLayout.setVisible(value);
        mainPageTopLayout.setVisible(value);
        coloredLoginPane.setVisible(!value);
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
                if(currentPageLabel != null)currentPageLabel.setText("/profile");
            }
            case MainWindow.FRIENDS -> {
                MainWindow.loadPage(MainWindow.FRIENDS);
                currentPageLabel.setText("/friends");
            }
            case MainWindow.MESSAGES -> {
                MainWindow.loadPage(MainWindow.MESSAGES);
                currentPageLabel.setText("/messages");

            }
            case MainWindow.EVENTS -> {
                MainWindow.loadPage(MainWindow.EVENTS);
                currentPageLabel.setText("/events");
            }
            case MainWindow.STATISTICS -> {
                MainWindow.loadPage(MainWindow.STATISTICS);
                currentPageLabel.setText("/statistics");

            }
            case MainWindow.SETTINGS -> {
                MainWindow.loadPage(MainWindow.SETTINGS);
                currentPageLabel.setText("/settings");
            }
            case MainWindow.WELCOME -> {
                MainWindow.loadPage(MainWindow.WELCOME);
               // currentPageLabel.setText("");
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
    public void switchToWelcomePage(ActionEvent event) throws IOException{
        switchPage(event, MainWindow.WELCOME);
    }

    @FXML
    public void logoutAction(ActionEvent event) throws IOException{
        usernameLabel.setText("no user");
        backEndController.logout();
        switchPage(event, MainWindow.LOGIN);
    }
}
