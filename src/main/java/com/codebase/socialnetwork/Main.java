package com.codebase.socialnetwork;

import com.codebase.socialnetwork.controller.Controller;
import com.codebase.socialnetwork.controller.ControllerClass;
import com.codebase.socialnetwork.domain.Friendship;
import com.codebase.socialnetwork.domain.Message;
import com.codebase.socialnetwork.domain.Tuple;
import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.domain.validator.FriendshipValidator;
import com.codebase.socialnetwork.domain.validator.UserValidator;
import com.codebase.socialnetwork.domain.validator.Validator;
import com.codebase.socialnetwork.gui.Gui;
import com.codebase.socialnetwork.gui.GuiClass;
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


public class Main {
    public static void main(String[] args) {

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

        Controller controller = new ControllerClass(userService, statisticsService);

        Gui gui = new GuiClass(controller);
        gui.startGui();
    }
}
