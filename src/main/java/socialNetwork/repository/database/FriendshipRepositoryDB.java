package socialNetwork.repository.database;

import socialNetwork.domain.Entity;
import socialNetwork.domain.Friendship;
import socialNetwork.domain.Tuple;
import socialNetwork.domain.User;
import socialNetwork.domain.exception.EntityNullException;
import socialNetwork.domain.validator.Validator;
import socialNetwork.repository.FriendshipRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;

import static socialNetwork.utils.Constants.DATE_TIME_FORMATTER;

public class FriendshipRepositoryDB <ID, E extends Entity<ID>> implements FriendshipRepository<ID, E> {
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private Validator<E> validator;

    public FriendshipRepositoryDB(String dbUrl, String dbUsername, String dbPassword, Validator<E> validator) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.validator = validator;
    }

    @Override
    public void writeToFile() {

    }

    @Override
    public Long getFriendshipsCount() {
        Long friendshipsCount = 0L;
        for(E entity : this.getAllFriendships()){
            friendshipsCount++;
        }
        return friendshipsCount;
    }

    @Override
    public E getFriendshipByID(ID id) {
        String queryFind = "select * from friendships where id_first_user = (?) and id_second_user = (?) or id_first_user = (?) and id_second_user = (?)";

        Tuple<Long, Long> idFriendship = (Tuple) id;

        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(queryFind)) {

            ps.setLong(1, idFriendship.getLeft());
            ps.setLong(2, idFriendship.getRight());
            ps.setLong(3, idFriendship.getRight());
            ps.setLong(4, idFriendship.getLeft());

            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()){
                throw new EntityNullException();
            }

            Long idFirstUser = resultSet.getLong("id_first_user");
            Long idSecondUser = resultSet.getLong("id_second_user");
            Timestamp dateFriendship = resultSet.getTimestamp("date_friendship");
            String statusFriendship = resultSet.getString("status_friendship");

            String dateTimeString = dateFriendship.toString().strip().substring(0, 16);
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
            Friendship friendship = new Friendship(dateTime);
            friendship.setId(new Tuple<>(idFirstUser, idSecondUser));
            friendship.setStatus(statusFriendship);

            return (E)friendship;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Iterable<E> getAllFriendships() {
        HashSet<E> friendships = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idFirstUser = resultSet.getLong("id_first_user");
                Long idSecondUser = resultSet.getLong("id_second_user");
                Timestamp dateFriendship = resultSet.getTimestamp("date_friendship");
                String statusFriendship = resultSet.getString("status_friendship");

                String dateTimeString = dateFriendship.toString().strip().substring(0, 16);
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
                Friendship friendship = new Friendship(dateTime);
                friendship.setId(new Tuple<>(idFirstUser, idSecondUser));
                friendship.setStatus(statusFriendship);
                friendships.add((E)friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public E addFriendship(E friendshipEntity) {
        if(friendshipEntity == null){
            throw new EntityNullException();
        }

        validator.validate(friendshipEntity);
        Friendship friendship = (Friendship) friendshipEntity;

        String queryFind = "select * from friendships where id_first_user = (?) and id_second_user = (?) or id_first_user = (?) and id_second_user = (?)";

        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(queryFind)) {

            ps.setLong(1, friendship.getId().getLeft());
            ps.setLong(2, friendship.getId().getRight());
            ps.setLong(3, friendship.getId().getRight());
            ps.setLong(4, friendship.getId().getLeft());

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                return friendshipEntity;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        String queryAdd = "insert into friendships (id_first_user, id_second_user, date_friendship, status_friendship) values (?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement  ps = connection.prepareStatement(queryAdd)) {

            ps.setLong(1, friendship.getId().getLeft());
            ps.setLong(2, friendship.getId().getRight());
            ps.setTimestamp(3, Timestamp.valueOf(friendship.getDate()));
            ps.setString(4, friendship.getStatus());

            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public E removeFriendship(E friendshipEntity) {
        if(friendshipEntity == null){
            throw new EntityNullException();
        }

        validator.validate(friendshipEntity);
        Friendship friendship = (Friendship) friendshipEntity;

        String queryFind = "select * from friendships where id_first_user = (?) and id_second_user = (?) or id_first_user = (?) and id_second_user = (?)";

        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(queryFind)) {

            ps.setLong(1, friendship.getId().getLeft());
            ps.setLong(2, friendship.getId().getRight());
            ps.setLong(3, friendship.getId().getRight());
            ps.setLong(4, friendship.getId().getLeft());

            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()){
                throw new EntityNullException();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }


        String queryDelete = "delete from friendships where id_first_user = (?) and id_second_user = (?) or id_first_user = (?) and id_second_user = (?)";

        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement  ps = connection.prepareStatement(queryDelete)) {

            ps.setLong(1, friendship.getId().getLeft());
            ps.setLong(2, friendship.getId().getRight());
            ps.setLong(3, friendship.getId().getRight());
            ps.setLong(4, friendship.getId().getLeft());

            ps.executeUpdate();

            return friendshipEntity;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public E updateFriendship(E friendshipEntity) {
        String sql = "update friendships set status_friendship = ? where id_first_user = (?) and id_second_user = (?) or id_first_user = (?) and id_second_user = (?)";

        Friendship friendship = (Friendship) friendshipEntity;
        try (Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, friendship.getStatus());
            ps.setLong(2, friendship.getId().getLeft());
            ps.setLong(3, friendship.getId().getRight());
            ps.setLong(4, friendship.getId().getRight());
            ps.setLong(5, friendship.getId().getLeft());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
