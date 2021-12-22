package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.Main;
import com.codebase.socialnetwork.MainWindow;
import com.codebase.socialnetwork.domain.FriendDTO;
import com.codebase.socialnetwork.domain.Post;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ProfileController extends MainWindowController {
    private int numberOfPhotos;

    @FXML
    ImageView imageViewProfilePicture;

    @FXML
    ScrollPane scrollPaneProfile;

    @FXML
    GridPane gridPaneProfile;

    @FXML
    Button buttonProfileDescription;

    @FXML
    TextArea textAreaProfileDescription;

    @FXML
    Label labelUsernameProfilePage;

    @FXML
    Button buttonChange;

    @FXML
    public void initialize() throws IOException {
        MainWindow.mainWindowController.setUsername(backEndController.getCurrentUsername());
        labelUsernameProfilePage.setText(backEndController.getCurrentUsername());
        numberOfPhotos = 0;

        setProfileInfo();
        updatePosts();
//        Image image = new Image("com/codebase/socialnetwork/images/hacker.png");
//        imageViewProfilePicture.setImage(image);

//        Path currentRelativePath = Paths.get("");
//        String s = currentRelativePath.toAbsolutePath().toString();
//        String path = s + "/src/main/resources/com/codebase/socialnetwork/images/hacker.png";
//        File file = new File(path);
//        InputStream fileInputStream = new FileInputStream(file);

    }

    public void setProfileInfo(){
        for(Post post : backEndController.getAllPosts()){
            if(post.getType().equals("profile") && post.getUsername().equals(backEndController.getCurrentUsername())){
                Image image = new Image(post.getImageStream());
                imageViewProfilePicture.setImage(image);
                textAreaProfileDescription.setText(post.getDescription());
                break;
            }
        }
    }

    @FXML
    public void editProfileDescription() throws FileNotFoundException {
        String currentOption = buttonProfileDescription.getText();
        if(currentOption.equals("Edit")){
            buttonProfileDescription.setText("Save");
            textAreaProfileDescription.setEditable(true);
        }
        else if(currentOption.equals("Save")){
            buttonProfileDescription.setText("Edit");
            textAreaProfileDescription.setEditable(false);


            String description = textAreaProfileDescription.getText();
            int likes = 0;
            String type = "profile";
            String username = backEndController.getCurrentUsername();

            // if profile info already exists
            for(Post post : backEndController.getAllPosts()){
                if(post.getType().equals("profile") && post.getUsername().equals(username)){
                    backEndController.updatePost(post.getImageStream(), description, likes, post.getId());
                    setProfileInfo();
                    return;
                }
            }

            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            String path = s + "/src/main/resources/com/codebase/socialnetwork/images/hacker.png";
            File file = new File(path);
            InputStream fileInputStream = new FileInputStream(file);

            backEndController.addPost(fileInputStream, description, likes, "profile", backEndController.getCurrentUsername());
            setProfileInfo();
        }
    }

    @FXML
    public void changeProfilePicture(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File file = fileChooser.showOpenDialog(Main.mainStage);
        if(file != null){
            InputStream fileInputStream = new FileInputStream(file);
            String description = textAreaProfileDescription.getText();
            int likes = 0;
            String type = "profile";
            String username = backEndController.getCurrentUsername();

            // if profile info already exists
            for(Post post : backEndController.getAllPosts()){
                if(post.getType().equals("profile") && post.getUsername().equals(username)){
                    backEndController.updatePost(fileInputStream, description, likes, post.getId());
                    setProfileInfo();
                    return;
                }
            }

            //if not exists profile
            backEndController.addPost(fileInputStream, description, likes, "profile", backEndController.getCurrentUsername());
            setProfileInfo();
        }
    }

    @FXML
    public void sharePhotos(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File file = fileChooser.showOpenDialog(Main.mainStage);
        if(file != null){
//            Image image = new Image(file.toURI().toString());
//            createNewPost(image);

            InputStream fileInputStream = new FileInputStream(file);
            String description = "";
            int likes = 0;
            String type = "other";
            String username = backEndController.getCurrentUsername();

            backEndController.addPost(fileInputStream, description, likes, type, backEndController.getCurrentUsername());
            updatePosts();
        }
    }

    private void updatePosts(){
        numberOfPhotos = 0;
        gridPaneProfile.getChildren().clear();
        for(Post post : backEndController.getAllPosts()){
            if(post.getType().equals("other")){
                createNewPost(post);
            }
        }
    }

    private void createNewPost(Post post){
        Image image = new Image(post.getImageStream());
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(0.33 * scrollPaneProfile.getPrefHeight());

        VBox vBox = new VBox();

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(0.33 * scrollPaneProfile.getPrefHeight());
        imageView.setFitWidth(0.33 * scrollPaneProfile.getPrefWidth());
        vBox.getChildren().add(imageView);

        HBox hBox = new HBox();

        Label labelPhotoLikes = new Label(String.valueOf(post.getLikes()));
        hBox.getChildren().add(labelPhotoLikes);

        Button buttonLike = new Button("Like");
        buttonLike.setId(post.getId().toString());

        buttonLike.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hey bro eu sunt: "  + buttonLike.getId());
                String currentOption = buttonLike.getText();
                int numberOfLikes = Integer.parseInt(labelPhotoLikes.getText());
                numberOfLikes += 1;
                labelPhotoLikes.setText(String.valueOf(numberOfLikes));
                backEndController.updatePost(null, post.getDescription(), numberOfLikes, post.getId());
            }
        });

        hBox.getChildren().add(buttonLike);

        vBox.getChildren().add(hBox);

        anchorPane.getChildren().add(vBox);

        int newPostRow = numberOfPhotos / 3;
        int newPostColumn = numberOfPhotos % 3;
        numberOfPhotos += 1;
        gridPaneProfile.add(anchorPane, newPostColumn, newPostRow);
    }
}
