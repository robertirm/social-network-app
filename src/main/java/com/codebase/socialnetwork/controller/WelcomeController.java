package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.MainWindow;
import com.codebase.socialnetwork.domain.Post;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WelcomeController extends MainWindowController{

    @FXML
    Label welcomeLabel;

    @FXML
    public void initialize() {
        MainWindow.setVisibility(true);
        MainWindow.mainWindowController.setUsername(backEndController.getCurrentUsername());
        Image image;
        Post post = backEndController.getProfilePost(backEndController.getCurrentUsername());
        if(post != null){
            image = new Image(post.getImageStream());
        }
        else {
            String imgURL = "./src/main/resources/com/codebase/socialnetwork/images/hacker.png";
            Path imagePath = Paths.get(imgURL);
            File imageFile = imagePath.toFile();
            image = new Image(imageFile.toURI().toString());
        }
        MainWindow.mainWindowController.setMainImage(image);
        welcomeLabel.setText("Welcome,\n   " + backEndController.getCurrentUsername() + "!");
    }
}
