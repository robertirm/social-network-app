package com.codebase.socialnetwork;

import com.codebase.socialnetwork.controller.Controller;
import com.codebase.socialnetwork.controller.BackendController;
import com.codebase.socialnetwork.controller.MainWindowController;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
        String username = "postgres";
        String password = "";

        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();

        Repository<Long, User> userRepository = new UserRepositoryClass(url, username, password, userValidator);
        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipRepositoryClass(url, username, password, friendshipValidator);
        Repository<Long, Message> messageRepository = new MessageRepositoryClass(url,username,password,userRepository);

        LoginSystem<Long, User> loginSystem = new LoginSystemClass();
        UserService<Long, User> userService = new UserServiceClass(userRepository, friendshipRepository, loginSystem);
        NetworkService<Tuple<Long, Long>, Friendship> statisticsService = new NetworkServiceClass(userRepository, friendshipRepository, messageRepository, loginSystem);

        Controller controller = new BackendController(userService, statisticsService);


        stage.setTitle("App");
        stage.setScene(
                createScene(
                        loadMainPane(controller)
                )
        );
        stage.setResizable(false);
        stage.show();
    }

    private Pane loadMainPane(Controller controller) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        Pane mainPane = loader.load(
                getClass().getResourceAsStream(
                        MainWindow.MAIN
                )
        );

        MainWindowController.setBackEndController(controller);
        MainWindowController mainWindowController = loader.getController();


        MainWindow.setMainWindowController(mainWindowController);
        MainWindow.loadPage(MainWindow.LOGIN);

        return mainPane;
    }


    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(
                mainPane
        );

        scene.getStylesheets().setAll(
                getClass().getResource("css/theme4.css").toExternalForm()
        );

        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
