package com.codebase.socialnetwork.controller;

import static com.codebase.socialnetwork.utils.Constants.DATE_TIME_FORMATTER;

import com.codebase.socialnetwork.Main;
import com.codebase.socialnetwork.MainWindow;
import com.codebase.socialnetwork.domain.FriendDTO;
import com.codebase.socialnetwork.domain.Post;
import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.repository.paging.Pageable;
import com.codebase.socialnetwork.utils.observer.Observer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import org.controlsfx.control.action.Action;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.*;
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
    AnchorPane anchorPaneFriendsMain;

   @FXML
    TextField textFieldName;

   @FXML
    ListView listViewFriendType;

   @FXML
    GridPane gridPaneFriends;

   @FXML
    ScrollPane scrollPaneFriends;

    private int numberOfPhotos;
    private String friendUsername;

    @FXML
    ImageView imageViewFriendProfilePicture;

    @FXML
    AnchorPane anchorPaneFriendsProfile;

    @FXML
    GridPane gridPaneFriendProfile;

    @FXML
    TextArea textAreaFriendProfileDescription;

    @FXML
    Label labelUsernameFriendProfilePage;

    @FXML
    Label labelFirstNameFriendProfilePage;

    @FXML
    Label labelLastNameFriendProfilePage;

    @FXML
    Button buttonPrevPageFriend;

    @FXML
    Button buttonNextPageFriend;

    @FXML
    Button buttonBackToFriends;

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

//       anchorPane.setId("anchorPaneFriend");
//       anchorPane.setPrefHeight(0.50 * gridPaneFriends.getPrefHeight());
       anchorPane.setPrefHeight(0.40 * gridPaneFriends.getPrefHeight());
       anchorPane.setPrefWidth(0.26 * gridPaneFriends.getPrefWidth());
       GridPane.setMargin(anchorPane, new Insets(5));


       VBox vBox = new VBox();
       vBox.setPrefHeight(0.90 * anchorPane.getPrefHeight());
       vBox.setPrefWidth(0.90 * anchorPane.getPrefWidth());
       vBox.setId("vboxFriend");
       vBox.setAlignment(Pos.CENTER);
//       vBox.setPadding(new Insets(20));

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
       imageView.setId("imageViewFriendController");
//       imageView.setFitHeight(0.40 * gridPaneFriends.getPrefHeight());
//       imageView.setFitWidth(0.20 * gridPaneFriends.getPrefWidth());
       imageView.setFitHeight(0.50 * vBox.getPrefHeight());
       imageView.setFitWidth(0.50 * vBox.getPrefWidth());
       imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent event) {
               try {
                   goToFriendsPage(friendDTO.getUser().getUsername());
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       });

       vBox.getChildren().add(imageView);

       Label labelFriendUsername = new Label(friendDTO.getUser().getUsername());
       labelFriendUsername.setId("labelFriendUsername");
       vBox.getChildren().add(labelFriendUsername);

       Button buttonAdd = new Button("Add");
       buttonAdd.setId(labelFriendUsername.getText());
       buttonAdd.setStyle("-fx-background-radius:  100;");

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
//        anchorPane.setId("anchorPaneFriend");
        anchorPane.setPrefHeight(0.40 * gridPaneFriends.getPrefHeight());
        anchorPane.setPrefWidth(0.26 * gridPaneFriends.getPrefWidth());
        GridPane.setMargin(anchorPane, new Insets(5));

        VBox vBox = new VBox();
        vBox.setPrefHeight(0.90 * anchorPane.getPrefHeight());
        vBox.setPrefWidth(0.90 * anchorPane.getPrefWidth());
        vBox.setId("vboxFriend");
        vBox.setAlignment(Pos.CENTER);
//        vBox.setPadding(new Insets(20));

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
        imageView.setId("imageViewFriendController");
//        imageView.setFitHeight(0.40 * gridPaneFriends.getPrefHeight());
//        imageView.setFitWidth(0.20 * gridPaneFriends.getPrefWidth());
        imageView.setFitHeight(0.50 * vBox.getPrefHeight());
        imageView.setFitWidth(0.50 * vBox.getPrefWidth());
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    goToFriendsPage(friendDTO.getUser().getUsername());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        vBox.getChildren().add(imageView);

        Label labelDate = new Label(friendDTO.getDataOfFriendship().format(DATE_TIME_FORMATTER));
        labelDate.setId("labelDateFriendship");
        vBox.getChildren().add(labelDate);

        Label labelFriendUsername = new Label(friendDTO.getUser().getUsername());
        labelFriendUsername.setId("labelFriendUsername");
        vBox.getChildren().add(labelFriendUsername);

        Button buttonRemove = new Button("Remove");
        buttonRemove.setId(labelFriendUsername.getText());
        buttonRemove.setStyle("-fx-background-radius:  100;");

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
//        anchorPane.setId("anchorPaneFriend");
        anchorPane.setPrefHeight(0.40 * gridPaneFriends.getPrefHeight());
        anchorPane.setPrefWidth(0.26 * gridPaneFriends.getPrefWidth());
        GridPane.setMargin(anchorPane, new Insets(5));

        VBox vBox = new VBox();
        vBox.setPrefHeight(0.90 * anchorPane.getPrefHeight());
        vBox.setPrefWidth(0.90 * anchorPane.getPrefWidth());
        vBox.setId("vboxFriend");
        vBox.setAlignment(Pos.CENTER);
//        vBox.setPadding(new Insets(20));

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
        imageView.setId("imageViewFriendController");
//        imageView.setFitHeight(0.40 * gridPaneFriends.getPrefHeight());
//        imageView.setFitWidth(0.20 * gridPaneFriends.getPrefWidth());
        imageView.setFitHeight(0.50 * vBox.getPrefHeight());
        imageView.setFitWidth(0.50 * vBox.getPrefWidth());
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    goToFriendsPage(friendDTO.getUser().getUsername());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        vBox.getChildren().add(imageView);

        Label labelDate = new Label(friendDTO.getDataOfFriendship().format(DATE_TIME_FORMATTER));
        labelDate.setId("labelDateFriendship");
        vBox.getChildren().add(labelDate);

        Label labelFriendUsername = new Label(friendDTO.getUser().getUsername());
        labelFriendUsername.setId("labelFriendUsername");
        vBox.getChildren().add(labelFriendUsername);

        HBox hBox = new HBox();

        Button buttonDecline = new Button("Decline");
        buttonDecline.setId(labelFriendUsername.getText());
        buttonDecline.setStyle("-fx-background-radius:  100;");

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
        buttonAccept.setStyle("-fx-background-radius:  100;");

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
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox);

        anchorPane.getChildren().add(vBox);
        return anchorPane;
    }

    private Pane createSentRequestFriendPane(FriendDTO friendDTO){
        AnchorPane anchorPane = new AnchorPane();
        //anchorPane.setId("anchorPaneFriend");
        anchorPane.setPrefHeight(0.40 * gridPaneFriends.getPrefHeight());
        anchorPane.setPrefWidth(0.26 * gridPaneFriends.getPrefWidth());
        GridPane.setMargin(anchorPane, new Insets(5));

        VBox vBox = new VBox();
        vBox.setPrefHeight(0.90 * anchorPane.getPrefHeight());
        vBox.setPrefWidth(0.90 * anchorPane.getPrefWidth());
        vBox.setId("vboxFriend");
        vBox.setAlignment(Pos.CENTER);
//        vBox.setPadding(new Insets(30));

        // <Image url="@../images/logo2.jpg" />
        //URL url = Paths.get("./src/main/resources/com/codebase/socialnetwork/views/profilePage.fxml").toUri().toURL();

        Image image;
        Post post = backEndController.getProfilePost(friendDTO.getUser().getUsername());
//        double highimg = 0.40 * gridPaneFriends.getPrefHeight();
//        double widthimg = 0.40 * gridPaneFriends.getPrefHeight();
        if(post != null){
//            image = new Image(post.getImageStream(), highimg, widthimg,false,false);
            image = new Image(post.getImageStream());
        }
        else {
            String imgURL = "./src/main/resources/com/codebase/socialnetwork/images/hacker.png";
            Path imagePath = Paths.get(imgURL);
            File imageFile = imagePath.toFile();
            image = new Image(imageFile.toURI().toString());
        }


        ImageView imageView = new ImageView(image);
        imageView.setId("imageViewFriendController");
//        imageView.setFitHeight(0.40 * gridPaneFriends.getPrefHeight());
//        imageView.setFitWidth(0.17 * gridPaneFriends.getPrefWidth());
        imageView.setFitHeight(0.50 * vBox.getPrefHeight());
        imageView.setFitWidth(0.50 * vBox.getPrefWidth());
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    goToFriendsPage(friendDTO.getUser().getUsername());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        vBox.getChildren().add(imageView);

        Label labelDate = new Label(friendDTO.getDataOfFriendship().format(DATE_TIME_FORMATTER));
        labelDate.setId("labelDateFriendship");
        vBox.getChildren().add(labelDate);

        Label labelFriendUsername = new Label(friendDTO.getUser().getUsername());
        labelFriendUsername.setId("labelFriendUsername");
        vBox.getChildren().add(labelFriendUsername);


        Button buttonCancel = new Button("Cancel");
        buttonCancel.setId(labelFriendUsername.getText());
        buttonCancel.setStyle("-fx-background-radius:  100;");

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

    @FXML
    public void goBackToFriendsPage(){
       anchorPaneFriendsMain.setDisable(false);
       anchorPaneFriendsMain.setVisible(true);
       anchorPaneFriendsProfile.setDisable(true);
       anchorPaneFriendsProfile.setVisible(false);
    }

    // Friends profile
    @FXML
    Label labelNoPostFriend;

    private void goToFriendsPage(String username) throws IOException {
        anchorPaneFriendsMain.setDisable(true);
        anchorPaneFriendsMain.setVisible(false);
        anchorPaneFriendsProfile.setDisable(false);
        anchorPaneFriendsProfile.setVisible(true);
        this.friendUsername = username;

        User currentUser = backEndController.getUserByUsername(this.friendUsername);
        labelUsernameFriendProfilePage.setText("@" + this.friendUsername);
        labelFirstNameFriendProfilePage.setText(currentUser.getFirstName());
        labelLastNameFriendProfilePage.setText(currentUser.getLastName());
        numberOfPhotos = 0;

        setProfileInfo();
        initPosts();
//        MainWindow.setVisibility(true);
    }

    public void setProfileInfo(){
        Post post = backEndController.getProfilePost(this.friendUsername);
        if(post != null){
            Image image = new Image(post.getImageStream());
            imageViewFriendProfilePicture.setImage(image);
            textAreaFriendProfileDescription.setText(post.getDescription());
        }
        else{
            String imgURL = "./src/main/resources/com/codebase/socialnetwork/images/hacker.png";
            Path imagePath = Paths.get(imgURL);
            File imageFile = imagePath.toFile();
            Image image = new Image(imageFile.toURI().toString());
            imageViewFriendProfilePicture.setImage(image);
            textAreaFriendProfileDescription.setText("Description...");
        }
    }

    private void initPosts(){
        numberOfPhotos = 0;
        gridPaneFriendProfile.getChildren().clear();
        boolean anyPost = false;
        for(Post post : backEndController.getPostsOnCurrentPage(0, this.friendUsername)){
            createNewPost(post);
            anyPost = true;
        }
        if(anyPost){
            labelNoPostFriend.setVisible(false);
        }
        else{
            labelNoPostFriend.setVisible(true);
        }
        updatePageControllButtons();
    }

    private void updatePosts(){
        numberOfPhotos = 0;
        gridPaneFriendProfile.getChildren().clear();
        for(Post post : backEndController.getPostsOnCurrentPage(-1, this.friendUsername)){
            createNewPost(post);
        }
        updatePageControllButtons();
    }

    private void getFirstPagePosts(){
        numberOfPhotos = 0;
        gridPaneFriendProfile.getChildren().clear();
        for(Post post : backEndController.getFirstPagePosts(this.friendUsername)){
            createNewPost(post);
        }
        updatePageControllButtons();
    }

    private void updatePageControllButtons(){
        Pageable pageable = backEndController.getPageable();
        Long postsCount = backEndController.getPostsCount(this.friendUsername);
        buttonPrevPageFriend.setDisable(pageable.getPageNumber() == 0);
        if((postsCount - 1)/ pageable.getPageSize() == pageable.getPageNumber()){
            buttonNextPageFriend.setDisable(true);
        }
        else{
            buttonNextPageFriend.setDisable(false);
        }
    }

    @FXML
    public void goToPrevPostsPage(){
        numberOfPhotos = 0;
        gridPaneFriendProfile.getChildren().clear();
        for(Post post : backEndController.getPrevPosts(this.friendUsername)){
            createNewPost(post);
        }
        updatePageControllButtons();
    }

    @FXML
    public void goToNextPostsPage(){
        numberOfPhotos = 0;
        gridPaneFriendProfile.getChildren().clear();
        for(Post post : backEndController.getNextPosts(this.friendUsername)){
            createNewPost(post);
        }
        updatePageControllButtons();
    }

    private void createNewPost(Post post){
        Image image = new Image(post.getImageStream());
        AnchorPane anchorPane = new AnchorPane();
//        anchorPane.setPrefHeight(0.33 * gridPaneFriendProfile.getPrefHeight());
        anchorPane.setPrefHeight(0.33 * gridPaneFriendProfile.getPrefHeight());
        anchorPane.setPrefWidth(0.33 * gridPaneFriendProfile.getPrefWidth());

        VBox vBox = new VBox();
        vBox.setPrefHeight(0.90 * anchorPane.getPrefHeight());
        vBox.setPrefWidth(0.90 * anchorPane.getPrefWidth());
        vBox.setId("vboxPost");
        vBox.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView(image);
//        imageView.setFitHeight(0.27 * gridPaneFriendProfile.getPrefHeight());
//        imageView.setFitWidth(0.32 * gridPaneFriendProfile.getPrefWidth());
        imageView.setFitHeight(0.60 * vBox.getPrefHeight());
        imageView.setFitWidth(0.60 * vBox.getPrefWidth());
        vBox.getChildren().add(imageView);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        Label labelPhotoLikes = new Label(String.valueOf(post.getLikes()));
        labelPhotoLikes.setId("labelPhotoLike");

        FontIcon icon = new FontIcon("fas-heart");
        Button buttonLike = new Button();
        icon.setIconColor(Paint.valueOf("red"));
        icon.setIconSize(15);
        buttonLike.setGraphic(icon);
        buttonLike.setId(post.getId().toString());
        buttonLike.setStyle("-fx-background-radius:  100;");

        buttonLike.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hey bro eu sunt: "  + buttonLike.getId());
                String currentOption = buttonLike.getText();
                int numberOfLikes = Integer.parseInt(labelPhotoLikes.getText());
                numberOfLikes += 1;
                labelPhotoLikes.setText(String.valueOf(numberOfLikes));
                backEndController.updatePost(null, post.getDescription(), numberOfLikes, post.getId(), friendUsername);
            }
        });

        hBox.getChildren().add(buttonLike);
        Label labelBeforeLikes = new Label("  ");
        labelBeforeLikes.setId("labelLike");
        hBox.getChildren().add(labelBeforeLikes);
        hBox.getChildren().add(labelPhotoLikes);
        Label labelAfterLikes = new Label(" hearts...");
        labelAfterLikes.setId("labelLike");
        hBox.getChildren().add(labelAfterLikes);

        vBox.getChildren().add(hBox);

        anchorPane.getChildren().add(vBox);

        int newPostRow = numberOfPhotos / 3;
        int newPostColumn = numberOfPhotos % 3;
        numberOfPhotos += 1;
        gridPaneFriendProfile.add(anchorPane, newPostColumn, newPostRow);
    }
}

