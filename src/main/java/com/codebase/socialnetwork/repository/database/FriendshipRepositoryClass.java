package com.codebase.socialnetwork.repository.database;

import com.codebase.socialnetwork.domain.Friendship;
import com.codebase.socialnetwork.domain.Tuple;
import com.codebase.socialnetwork.domain.exception.EntityNullException;
import com.codebase.socialnetwork.domain.exception.IdNullException;
import com.codebase.socialnetwork.domain.validator.Validator;
import com.codebase.socialnetwork.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;

import static com.codebase.socialnetwork.utils.Constants.DATE_TIME_FORMATTER;

public class FriendshipRepositoryClass implements Repository<Tuple<Long, Long>, Friendship> {
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final Validator<Friendship> validator;

    public FriendshipRepositoryClass(String dbUrl, String dbUsername, String dbPassword, Validator<Friendship> validator) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.validator = validator;
    }

    @Override
    public Long getCount() {
        Long friendshipsCount = 0L;
        for(Friendship ignored : this.findAll()){
            friendshipsCount++;
        }

        return friendshipsCount;
    }

    @Override
    public Friendship findOne(Tuple<Long, Long> id) {
        if(id == null){
            throw new IdNullException();
        }

        String queryFind = "select * from friendships where id_first_user = (?) and id_second_user = (?) or id_first_user = (?) and id_second_user = (?)";
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(queryFind)) {

            ps.setLong(1, id.getLeft());
            ps.setLong(2, id.getRight());
            ps.setLong(3, id.getRight());
            ps.setLong(4, id.getLeft());

            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()){
                return null;
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

            return friendship;

        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public HashSet<Friendship> findAll() {
        HashSet<Friendship> friendships = new HashSet<>();

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
                friendships.add(friendship);
            }

            return friendships;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friendships;
    }

    @Override
    public Friendship save(Friendship friendship) {
        if(friendship == null){
            throw new EntityNullException();
        }

        validator.validate(friendship);

        if(this.findOne(friendship.getId()) != null){
            return friendship;
        }

        String queryAdd = "insert into friendships (id_first_user, id_second_user, date_friendship, status_friendship) values (?,?,?,?)";
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement  ps = connection.prepareStatement(queryAdd)) {

            ps.setLong(1, friendship.getId().getLeft());
            ps.setLong(2, friendship.getId().getRight());
            ps.setTimestamp(3, Timestamp.valueOf(friendship.getDate()));
            ps.setString(4, friendship.getStatus());

            ps.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Friendship delete(Tuple<Long, Long> id) {
        Friendship friendship = this.findOne(id);

        if(friendship == null) {
            throw new EntityNullException();
        }

        String queryDelete = "delete from friendships where id_first_user = (?) and id_second_user = (?) or id_first_user = (?) and id_second_user = (?)";
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement  ps = connection.prepareStatement(queryDelete)) {

            ps.setLong(1, friendship.getId().getLeft());
            ps.setLong(2, friendship.getId().getRight());
            ps.setLong(3, friendship.getId().getRight());
            ps.setLong(4, friendship.getId().getLeft());

            ps.executeUpdate();

            return friendship;

        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Friendship update(Friendship friendship) {
        String sql = "update friendships set status_friendship = ? where id_first_user = (?) and id_second_user = (?) or id_first_user = (?) and id_second_user = (?)";
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
