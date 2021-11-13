package socialNetwork.repository.database;

import socialNetwork.domain.Entity;
import socialNetwork.domain.User;
import socialNetwork.domain.exception.EntityNullException;
import socialNetwork.domain.exception.IdNullException;
import socialNetwork.domain.exception.LogInException;
import socialNetwork.domain.exception.WrongUsernameException;
import socialNetwork.domain.validator.Validator;
import socialNetwork.repository.UserRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserRepositoryDB<ID, E extends Entity<ID>> implements UserRepository<ID, E> {
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private Validator<E> validator;

    private Object currentUserId;
    private String username;

    public UserRepositoryDB(String dbUrl, String dbUsername, String dbPassword, Validator<E> validator) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.validator = validator;
    }

    @Override
    public void writeToFile() {

    }

    @Override
    public ID getCurrentUserId() {
        if(currentUserId == null){
            throw new LogInException();
        }
        return (ID) currentUserId;
    }

    @Override
    public void setCurrentUser(String username) {
        boolean valid = false;
        for(E entity : this.getAllUsers()){
            User user = (User) entity;
            if(user.getUsername().equals(username)){
                this.currentUserId = user.getId();
                this.username = username;
                valid = true;
                break;
            }
        }
        if(!valid){
            throw new WrongUsernameException();
        }
    }

    @Override
    public String getCurrentUsername() {
        return this.username;
    }

    @Override
    public Long getUsersCount() {
        Long usersCount = 0L;
        for(E entity : this.getAllUsers()){
            usersCount++;
        }
        return usersCount;
    }

    @Override
    public E getUserByID(ID id) {
        String queryFind = "select * from users where id = (?)";
        User user = null;
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(queryFind)) {
            ps.setLong(1, (long)id);
            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()){
                throw new EntityNullException();
            }

            Long idUser = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String username = resultSet.getString("username");

            user = new User(firstName, lastName, username);
            user.setId(idUser);
            return (E)user;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<E> getAllUsers() {
        HashSet<E> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idUser = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");

                User user = new User(firstName, lastName, username);
                user.setId(idUser);
                users.add((E)user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public E selectUser(E userEntity) {
        User user = (User) userEntity;
        String queryFind = "select * from users where username = (?)";

        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(queryFind)) {
            ps.setString(1, user.getUsername());
            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                return userEntity;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public E addUser(E userEntity) {
        if(userEntity == null){
            throw new EntityNullException();
        }
        validator.validate(userEntity);
        User user = (User) userEntity;

        if(selectUser(userEntity) != null) {
            return userEntity;
        }
        

        String queryAdd = "insert into users (first_name, last_name, username) values (?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement  ps = connection.prepareStatement(queryAdd)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getUsername());

            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public E removeUser(ID id) {
        if(id == null){
            throw new IdNullException();
        }

        E user = this.getUserByID(id);
        if(user == null){
            throw new EntityNullException();
        }

        String queryDelete = "delete from users where id = (?)";

        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement  ps = connection.prepareStatement(queryDelete)) {

            ps.setLong(1, (long) id);

            ps.executeUpdate();

            this.username = null;
            this.currentUserId = null;
            return user;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public E getUserByUsername(String username) {
        String queryFind = "select * from users where username = (?)";
        User user = null;
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(queryFind)) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()){
                throw new EntityNullException();
            }


            Long idUser = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String userUsername = resultSet.getString("username");

            user = new User(firstName, lastName, userUsername);
            user.setId(idUser);
            return (E)user;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
