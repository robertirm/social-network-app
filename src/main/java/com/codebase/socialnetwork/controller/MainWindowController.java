package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.MainWindow;
import com.codebase.socialnetwork.domain.Event;
import com.codebase.socialnetwork.domain.exception.LogInException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainWindowController {

    public static Controller backEndController;

    static Thread thread;

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

    @FXML
    protected ImageView mainPageProfileImage;

    public void setPage(Node node){
        pageHolder.getChildren().setAll(node);
    }

    public void setUsername(String username){
        usernameLabel.setText(username);
    }

    public void setMainImage(Image image){
        mainPageProfileImage.setFitWidth(50);
        mainPageProfileImage.setFitHeight(60);
        mainPageProfileImage.setImage(image);
    }

    public void setButtonsLayoutVisible(boolean value){
        mainPageButtonLayout.setVisible(value);
        mainPageTopLayout.setVisible(value);
        coloredLoginPane.setVisible(!value);
    }

    public static void setBackEndController(Controller backEndController) {
        MainWindowController.backEndController = backEndController;
        startThread();
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

    public static void startThread(){
        thread = new Thread(() -> {
            List<Event> notifiedEventsTenMinutes = new ArrayList<>();

            while(backEndController.getCurrentUsername() != null){
                try{
                    Thread.sleep(100);
                    List<Event> events = backEndController.getAllUserEvents(backEndController.getCurrentUserId());

                    for(Event event: events){
                        if(!notifiedEventsTenMinutes.contains(event)){
                            long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(),event.getStartingDate());
                            if(minutes < 60 && minutes > 0){
                                notifiedEventsTenMinutes.add(event);

                                Platform.runLater(() -> {
                                    String text = event.getNameEvent().toUpperCase(Locale.ROOT) + " will start in less than an hour!!!";

                                    Stage dialog = new Stage();
                                    dialog.setResizable(false);
                                    VBox dialogVbox = new VBox(20);
                                    dialogVbox.setStyle("-fx-background-color: #F2003C");

                                    Label label = new Label(text);
                                    label.setStyle("-fx-font-weight: bold;" +
                                            "-fx-font-family: 'Times New Roman';" +
                                            "-fx-text-fill: white;"+
                                            "-fx-font-size: 20px;"+
                                            "-fx-padding: 10 10 10 10;"
                                            );

                                    dialogVbox.getChildren().add(label);
                                    Scene dialogScene = new Scene(dialogVbox, 600, 50);

                                    dialog.setScene(dialogScene);
                                    dialog.show();
                                });
                            }
                        }
                    }

                } catch (InterruptedException | LogInException e) {
                    System.out.println(e.getMessage());

                }

            }
        });
        thread.start();
    }

    public void     shutdown(){
        thread.stop();
    }
}
