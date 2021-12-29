package com.codebase.socialnetwork.controller;

import static com.codebase.socialnetwork.utils.Constants.DATE_TIME_FORMATTER;

import com.codebase.socialnetwork.MainWindow;
import com.codebase.socialnetwork.domain.FriendDTO;
import com.codebase.socialnetwork.domain.Post;
import com.codebase.socialnetwork.utils.observer.Observer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class FriendsController extends MainWindowController implements Observer {
    ObservableList<FriendDTO> observableListFriendDTO = FXCollections.observableArrayList();
    List<FriendDTO> friendDTOList;

   @FXML
    TextField textFieldName;

   @FXML
    ListView listViewFriendType;

   @FXML
    GridPane gridPaneFriends;

   @FXML
    ScrollPane scrollPaneFriends;

   @FXML
   public void initialize() {
       backEndController.addObserver(this);
       textFieldName.textProperty().addListener(o -> handleSearch());

       List<String> type = Arrays.asList("Others", "Friends", "Request", "Sent Request");
       ObservableList<String> observableListType = FXCollections.observableArrayList(type);
       listViewFriendType.setItems(observableListType);

       listViewFriendType.getSelectionModel().selectedItemProperty().addListener(
               new ChangeListener() {
                   @Override
                   public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                       updateFriendDTOList();
                       handleSearch();
                   }
               }
       );

       observableListFriendDTO.addListener(new ListChangeListener<FriendDTO>() {
           @Override
           public void onChanged(Change<? extends FriendDTO> change) {
               updateGridPaneFriends();
           }
       });

       scrollPaneFriends.setContent(gridPaneFriends);

       // usernameLabel.setText(backEndController.getCurrentUsername());
//       MainWindow.mainWindowController.setUsername(backEndController.getCurrentUsername());
       //observableListFriendDTO.setAll(backEndController.getAllFriendsForUser(usernameLabel.getText()));
       observableListFriendDTO.setAll(backEndController.getAllFriendsByStatus("Others"));
       friendDTOList = new ArrayList<>(observableListFriendDTO);
       listViewFriendType.getSelectionModel().select(0);
   }

   private void updateFriendDTOList() {
       String friendType = listViewFriendType.getSelectionModel().getSelectedItem().toString();
       observableListFriendDTO.setAll(backEndController.getAllFriendsByStatus(friendType));
       friendDTOList = new ArrayList<>(observableListFriendDTO);
   }

   private void handleSearch() {
       String friendType = listViewFriendType.getSelectionModel().getSelectedItem().toString();
       System.out.println(friendType);

       Predicate<FriendDTO> p = f -> f.getUser().getUsername().contains(textFieldName.getText());

       observableListFriendDTO.setAll(friendDTOList.stream()
               .filter(p)
               .collect(Collectors.toList())
       );
   }

   private Pane createAddFriendPane(FriendDTO friendDTO){
       AnchorPane anchorPane = new AnchorPane();
       anchorPane.setPrefHeight(0.33 * scrollPaneFriends.getPrefHeight());

       VBox vBox = new VBox();

       // <Image url="@../images/logo2.jpg" />
       //URL url = Paths.get("./src/main/resources/com/codebase/socialnetwork/views/profilePage.fxml").toUri().toURL();

       Image image;
       Post post = backEndController.getProfilePost(friendDTO.getUser().getUsername());
       if(post != null){
           image = new Image(post.getImageStream());
       }
       else {
           String imgURL = "./src/main/resources/com/codebase/socialnetwork/images/hacker.png";
           Path imagePath = Paths.get(imgURL);
           File imageFile = imagePath.toFile();
           image = new Image(imageFile.toURI().toString());
       }

       ImageView imageView = new ImageView(image);
       imageView.setFitHeight(0.27 * gridPaneFriends.getPrefHeight());
       imageView.setFitWidth(0.32 * gridPaneFriends.getPrefWidth());
       vBox.getChildren().add(imageView);

       Label labelFriendUsername = new Label(friendDTO.getUser().getUsername());
       vBox.getChildren().add(labelFriendUsername);

       Button buttonAdd = new Button("Add");

       buttonAdd.setId(labelFriendUsername.getText());

       buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               System.out.println("Hey bro eu sunt: "  + buttonAdd.getId());
               backEndController.addFriend(buttonAdd.getId());
//               updateFriendDTOList();
//               handleSearch();
           }
       });

       vBox.getChildren().add(buttonAdd);

       anchorPane.getChildren().add(vBox);
       return anchorPane;
   }

    private Pane createRemoveFriendPane(FriendDTO friendDTO){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(0.33 * scrollPaneFriends.getPrefHeight());

        VBox vBox = new VBox();

        // <Image url="@../images/logo2.jpg" />
        //URL url = Paths.get("./src/main/resources/com/codebase/socialnetwork/views/profilePage.fxml").toUri().toURL();

        Image image;
        Post post = backEndController.getProfilePost(friendDTO.getUser().getUsername());
        if(post != null){
            image = new Image(post.getImageStream());
        }
        else {
            String imgURL = "./src/main/resources/com/codebase/socialnetwork/images/hacker.png";
            Path imagePath = Paths.get(imgURL);
            File imageFile = imagePath.toFile();
            image = new Image(imageFile.toURI().toString());
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(0.27 * gridPaneFriends.getPrefHeight());
        imageView.setFitWidth(0.32 * gridPaneFriends.getPrefWidth());
        vBox.getChildren().add(imageView);

        Label labelFriendUsername = new Label(friendDTO.getUser().getUsername());
        vBox.getChildren().add(labelFriendUsername);

        Button buttonRemove = new Button("Remove");

        buttonRemove.setId(labelFriendUsername.getText());

        buttonRemove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hey bro eu sunt: "  + buttonRemove.getId());
                backEndController.removeFriend(buttonRemove.getId());
//                updateFriendDTOList();
//                handleSearch();
            }
        });

        vBox.getChildren().add(buttonRemove);

        anchorPane.getChildren().add(vBox);
        return anchorPane;
    }

    private Pane createRequestFriendPane(FriendDTO friendDTO){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(0.33 * scrollPaneFriends.getPrefHeight());

        VBox vBox = new VBox();

        // <Image url="@../images/logo2.jpg" />
        //URL url = Paths.get("./src/main/resources/com/codebase/socialnetwork/views/profilePage.fxml").toUri().toURL();

        Image image;
        Post post = backEndController.getProfilePost(friendDTO.getUser().getUsername());
        if(post != null){
            image = new Image(post.getImageStream());
        }
        else {
            String imgURL = "./src/main/resources/com/codebase/socialnetwork/images/hacker.png";
            Path imagePath = Paths.get(imgURL);
            File imageFile = imagePath.toFile();
            image = new Image(imageFile.toURI().toString());
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(0.27 * gridPaneFriends.getPrefHeight());
        imageView.setFitWidth(0.32 * gridPaneFriends.getPrefWidth());
        vBox.getChildren().add(imageView);

        Label labelDate = new Label(friendDTO.getDataOfFriendship().format(DATE_TIME_FORMATTER));
        vBox.getChildren().add(labelDate);

        Label labelFriendUsername = new Label(friendDTO.getUser().getUsername());
        vBox.getChildren().add(labelFriendUsername);

        HBox hBox = new HBox();

        Button buttonDecline = new Button("Decline");
        buttonDecline.setId(labelFriendUsername.getText());

        buttonDecline.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hey bro eu sunt: "  + buttonDecline.getId());
                backEndController.setFriendshipStatus(buttonDecline.getId(), "rejected");
//                updateFriendDTOList();
//                handleSearch();
            }
        });

        hBox.getChildren().add(buttonDecline);

        Button buttonAccept = new Button("Accept");
        buttonAccept.setId(labelFriendUsername.getText());

        buttonAccept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hey bro eu sunt: "  + buttonAccept.getId());
                backEndController.setFriendshipStatus(buttonAccept.getId(), "approved");
//                updateFriendDTOList();
//                handleSearch();
            }
        });

        hBox.getChildren().add(buttonAccept);
        vBox.getChildren().add(hBox);

        anchorPane.getChildren().add(vBox);
        return anchorPane;
    }

    private Pane createSentRequestFriendPane(FriendDTO friendDTO){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(0.33 * scrollPaneFriends.getPrefHeight());

        VBox vBox = new VBox();

        // <Image url="@../images/logo2.jpg" />
        //URL url = Paths.get("./src/main/resources/com/codebase/socialnetwork/views/profilePage.fxml").toUri().toURL();

        Image image;
        Post post = backEndController.getProfilePost(friendDTO.getUser().getUsername());
        if(post != null){
            image = new Image(post.getImageStream());
        }
        else {
            String imgURL = "./src/main/resources/com/codebase/socialnetwork/images/hacker.png";
            Path imagePath = Paths.get(imgURL);
            File imageFile = imagePath.toFile();
            image = new Image(imageFile.toURI().toString());
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(0.27 * gridPaneFriends.getPrefHeight());
        imageView.setFitWidth(0.32 * gridPaneFriends.getPrefWidth());
        vBox.getChildren().add(imageView);

        Label labelDate = new Label(friendDTO.getDataOfFriendship().format(DATE_TIME_FORMATTER));
        vBox.getChildren().add(labelDate);

        Label labelFriendUsername = new Label(friendDTO.getUser().getUsername());
        vBox.getChildren().add(labelFriendUsername);


        Button buttonCancel = new Button("Cancel");
        buttonCancel.setId(labelFriendUsername.getText());

        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hey bro eu sunt: "  + buttonCancel.getId());
                backEndController.setFriendshipStatus(buttonCancel.getId(), "rejected");
//                updateFriendDTOList();
//                handleSearch();
            }
        });

        vBox.getChildren().add(buttonCancel);
        anchorPane.getChildren().add(vBox);
        return anchorPane;
    }

    // https://stackoverflow.com/questions/45450489/how-to-implement-a-gridview-style-in-javafx
   private void updateGridPaneFriends(){
       int numberOfColumns = gridPaneFriends.getColumnCount();
       int currentColumn = 0;
       int currentRow = 0;

       gridPaneFriends.getChildren().clear();
       for (FriendDTO friendDTO : observableListFriendDTO) {
           String friendType;
           if(listViewFriendType.getSelectionModel().getSelectedItem() == null){
               friendType = "Others";
           }
           else{
               friendType = listViewFriendType.getSelectionModel().getSelectedItem().toString();
           }

           switch (friendType) {
               case "Others" -> gridPaneFriends.add(createAddFriendPane(friendDTO), currentColumn, currentRow);
               case "Friends" -> gridPaneFriends.add(createRemoveFriendPane(friendDTO), currentColumn, currentRow);
               case "Request" -> gridPaneFriends.add(createRequestFriendPane(friendDTO), currentColumn, currentRow);
               case "Sent Request" -> gridPaneFriends.add(createSentRequestFriendPane(friendDTO), currentColumn, currentRow);
           }
           currentColumn++;
           if (currentColumn >= numberOfColumns) {
               currentRow++;
               currentColumn = 0;
           }
       }
       scrollPaneFriends.setContent(gridPaneFriends);
   }

    @Override
    public void update() {
        updateFriendDTOList();
        handleSearch();
    }
}
