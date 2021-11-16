package socialNetwork.repository.database;

import socialNetwork.domain.User;
import socialNetwork.domain.exception.EntityNullException;
import socialNetwork.domain.exception.IdNullException;
import socialNetwork.domain.validator.Validator;
import socialNetwork.repository.UserRepository;

import java.sql.*;
import java.util.HashSet;

public class UserRepositoryClass implements UserRepository<Long, User> {
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final Validator<User> validator;

    public UserRepositoryClass(String dbUrl, String dbUsername, String dbPassword, Validator<User> validator) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.validator = validator;
    }

    @Override
    public Long getCount() {
        Long usersCount = 0L;
        for(User ignored : this.findAll()){
            usersCount++;
        }

        return usersCount;
    }

    @Override
    public User findOne(Long id) {
        String queryFind = "select * from users where id = (?)";
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(queryFind)) {
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()){
                throw new EntityNullException();
            }

            Long idUser = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String username = resultSet.getString("username");

            User user = new User(firstName, lastName, username);
            user.setId(idUser);

            return user;

        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public HashSet<User> findAll() {
        HashSet<User> users = new HashSet<>();

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
                users.add(user);
            }

            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public User selectUser(User user) {
        String queryFind = "select * from users where username = (?)";
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(queryFind)) {
            ps.setString(1, user.getUsername());
            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                return user;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User save(User user) {
        if(user == null){
            throw new EntityNullException();
        }

        validator.validate(user);

        if(selectUser(user) != null) {
            return user;
        }

        String queryAdd = "insert into users (first_name, last_name, username) values (?,?,?)";
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement  ps = connection.prepareStatement(queryAdd)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getUsername());

            ps.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User delete(Long id) {
        if(id == null){
            throw new IdNullException();
        }

        User user = this.findOne(id);

        if(user == null){
            throw new EntityNullException();
        }

        String queryDelete = "delete from users where id = (?)";
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement  ps = connection.prepareStatement(queryDelete)) {

            ps.setLong(1, id);

            ps.executeUpdate();

            return user;

        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }
}
