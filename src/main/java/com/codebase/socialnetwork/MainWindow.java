package com.codebase.socialnetwork;

import com.codebase.socialnetwork.controller.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public class MainWindow {
    public static final String MAIN = "views/mainPage.fxml";
    public static final String LOGIN = "views/loginPage.fxml";
    public static final String SIGNUP = "views/signupPage.fxml";
    public static final String PROFILE = "views/profilePage.fxml";
    public static final String FRIENDS = "views/friendsPage.fxml";
    public static final String MESSAGES = "views/messagesPage.fxml";
    public static final String EVENTS = "views/eventsPage.fxml";
    public static final String STATISTICS = "views/statisticsPage.fxml";
    public static final String SETTINGS = "views/settingsPage.fxml";

    public static final String THEME_1 = "css/theme1.css";
    public static final String THEME_2 = "css/theme2.css";
    public static final String THEME_3 = "css/theme3.css";
    public static final String THEME_4 = "css/theme4.css";
    public static final String THEME_5 = "css/theme5.css";
    public static final String THEME_6 = "css/theme6.css";

    public static MainWindowController mainWindowController;

    public static void setMainWindowController(MainWindowController mainWindowController){
        MainWindow.mainWindowController = mainWindowController;
    }

    public static void loadPage(String fxml){
        try{
            mainWindowController.setPage(
                    FXMLLoader.load(
                            Objects.requireNonNull(MainWindow.class.getResource(
                                    fxml
                            ))
                    )
            );
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void setStylesheet(Scene scene, String css){
        scene.getStylesheets().clear();
        scene.getStylesheets().setAll(
                MainWindow.class.getResource(css).toExternalForm()
        );
    }
}
