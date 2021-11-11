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
import socialNetwork.repository.database.FriendshipRepositoryDB;
import socialNetwork.repository.database.UserRepositoryDB;
import socialNetwork.repository.file.FriendshipRepositoryFile;
import socialNetwork.repository.file.UserRepositoryFile;
import socialNetwork.repository.UserRepository;
import socialNetwork.service.StatisticsService;
import socialNetwork.service.StatisticsServiceClass;
import socialNetwork.service.UserService;
import socialNetwork.service.UserServiceClass;


public class Main {
    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
        String username = "postgres";
        String password = "";
//        Validator<User> userValidator = new UserValidator();
//        UserRepository<Long, User> repository = new UserRepositoryDB(url, username, password, userValidator);

//        User user = new User("a","b","first");
//        repository.addUser(user);
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        // UserRepository<Long, User> userRepository = new UserRepositoryMemory<>(userValidator);
        //String userFilename = "data/users.csv";
        //UserRepository<Long, User> userRepository = new UserRepositoryFile<>(userFilename, userValidator);
        UserRepository<Long, User> userRepository = new UserRepositoryDB<>(url, username, password, userValidator);
        // FriendshipRepository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipRepositoryMemory<>(friendValidator, userRepository);
        String friendshipFilename = "data/friendship.csv";
//        FriendshipRepository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipRepositoryFile<>(friendshipFilename, friendshipValidator);
        FriendshipRepository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipRepositoryDB<>(url, username, password, friendshipValidator);
        UserService<Long, User> userService = new UserServiceClass<>(userRepository, friendshipRepository);
        StatisticsService<Long, User> statisticsService = new StatisticsServiceClass<>(userRepository, friendshipRepository);
        Controller<Long, User> controller = new ControllerClass<>(userService, statisticsService);
        Gui<Long, User> gui = new GuiClass<>(controller);
        gui.startGui();
    }
}
