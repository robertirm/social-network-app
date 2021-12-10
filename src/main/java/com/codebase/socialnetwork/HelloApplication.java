package com.codebase.socialnetwork;

import com.codebase.socialnetwork.controller.Controller;
import com.codebase.socialnetwork.controller.ControllerClass;
import com.codebase.socialnetwork.controller.LoginController;
import com.codebase.socialnetwork.domain.Friendship;
import com.codebase.socialnetwork.domain.Message;
import com.codebase.socialnetwork.domain.Tuple;
import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.domain.validator.FriendshipValidator;
import com.codebase.socialnetwork.domain.validator.UserValidator;
import com.codebase.socialnetwork.domain.validator.Validator;
import com.codebase.socialnetwork.repository.Repository;
import com.codebase.socialnetwork.repository.database.FriendshipRepositoryClass;
import com.codebase.socialnetwork.repository.database.MessageRepositoryClass;
import com.codebase.socialnetwork.repository.database.UserRepositoryClass;
import com.codebase.socialnetwork.repository.memory.LoginSystem;
import com.codebase.socialnetwork.repository.memory.LoginSystemClass;
import com.codebase.socialnetwork.service.NetworkService;
import com.codebase.socialnetwork.service.NetworkServiceClass;
import com.codebase.socialnetwork.service.UserService;
import com.codebase.socialnetwork.service.UserServiceClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
        String username = "postgres";
        String password = "GMsuper12";

        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();

        Repository<Long, User> userRepository = new UserRepositoryClass(url, username, password, userValidator);
        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipRepositoryClass(url, username, password, friendshipValidator);
        Repository<Long, Message> messageRepository = new MessageRepositoryClass(url,username,password,userRepository);

        LoginSystem<Long, User> loginSystem = new LoginSystemClass();
        UserService<Long, User> userService = new UserServiceClass(userRepository, friendshipRepository, loginSystem);
        NetworkService<Tuple<Long, Long>, Friendship> statisticsService = new NetworkServiceClass(userRepository, friendshipRepository, messageRepository, loginSystem);

        Controller controller = new ControllerClass(userService, statisticsService);


        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("views/loginPage.fxml"));

        AnchorPane root=loader.load();

        LoginController guiController = loader.getController();
        guiController.setBackEndController(controller);

        Scene scene = new Scene(root, 1280, 720);

        stage.setTitle("Print All!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}