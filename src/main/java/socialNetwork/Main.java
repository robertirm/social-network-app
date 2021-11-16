package socialNetwork;

import socialNetwork.controller.Controller;
import socialNetwork.controller.ControllerClass;
import socialNetwork.domain.Friendship;
import socialNetwork.domain.Tuple;
import socialNetwork.domain.User;
import socialNetwork.domain.validator.FriendshipValidator;
import socialNetwork.domain.validator.UserValidator;
import socialNetwork.domain.validator.Validator;
import socialNetwork.gui.Gui;
import socialNetwork.gui.GuiClass;
import socialNetwork.repository.FriendshipRepository;
import socialNetwork.repository.UserRepository;
import socialNetwork.repository.database.FriendshipRepositoryClass;
import socialNetwork.repository.database.UserRepositoryClass;
import socialNetwork.repository.memory.LoginSystem;
import socialNetwork.repository.memory.LoginSystemClass;
import socialNetwork.service.NetworkService;
import socialNetwork.service.NetworkServiceClass;
import socialNetwork.service.UserService;
import socialNetwork.service.UserServiceClass;


public class Main {
    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
        String username = "postgres";
        String password = "";

        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        UserRepository<Long, User> userRepository = new UserRepositoryClass(url, username, password, userValidator);
        FriendshipRepository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipRepositoryClass(url, username, password, friendshipValidator);
        LoginSystem<Long, User> loginSystem = new LoginSystemClass();
        UserService<Long, User> userService = new UserServiceClass(userRepository, friendshipRepository, loginSystem);
        NetworkService<Tuple<Long, Long>, Friendship> statisticsService = new NetworkServiceClass(userRepository, friendshipRepository, loginSystem);
        Controller controller = new ControllerClass(userService, statisticsService);
        Gui gui = new GuiClass(controller);
        gui.startGui();
    }
}
