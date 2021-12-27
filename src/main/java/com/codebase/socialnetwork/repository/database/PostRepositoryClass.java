package com.codebase.socialnetwork.repository.database;

import com.codebase.socialnetwork.domain.Post;
import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.domain.exception.EntityNullException;
import com.codebase.socialnetwork.repository.Repository;
import com.codebase.socialnetwork.repository.paging.Page;
import com.codebase.socialnetwork.repository.paging.Pageable;
import com.codebase.socialnetwork.repository.paging.Paginator;
import com.codebase.socialnetwork.repository.paging.PagingRepository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashSet;

public class PostRepositoryClass implements PagingRepository<Long, Post> {
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;

    public PostRepositoryClass(String dbUrl, String dbUsername, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    @Override
    public Long getCount(String username) {
        HashSet<Post> posts = findAll(username);
        return (long)posts.size();
    }

    @Override
    public Post findOne(Long aLong) {
        return null;
    }

    @Override
    public HashSet<Post> findAll(String username) {
        HashSet<Post> posts = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement statement = connection.prepareStatement("SELECT * from posts WHERE username=? and type=?")) {

            statement.setString(1, username);
            statement.setString(2, "other");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long idPost = resultSet.getLong("id_post");
                InputStream imageStream = resultSet.getBinaryStream("image");
                String description = resultSet.getString("description");
                int likes = resultSet.getInt("likes");
                String type = resultSet.getString("type");
                String usernamePost = resultSet.getString("username");

                Post post = new Post(imageStream, description, likes, type, usernamePost);
                post.setId(idPost);
                posts.add(post);
            }

            return posts;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    @Override
    public Page<Post> findAll(Pageable pageable, String username) {
        Paginator<Post> paginator = new Paginator<Post>(pageable, this.findAll(username));
        return paginator.paginate();
    }

    @Override
    public Post findProfilePost(String username){

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement statement = connection.prepareStatement("SELECT * from posts WHERE username=? and type=?")) {

            statement.setString(1, username);
            statement.setString(2, "profile");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long idPost = resultSet.getLong("id_post");
                InputStream imageStream = resultSet.getBinaryStream("image");
                String description = resultSet.getString("description");
                int likes = resultSet.getInt("likes");
                String type = resultSet.getString("type");
                String usernamePost = resultSet.getString("username");

                Post post = new Post(imageStream, description, likes, type, usernamePost);
                post.setId(idPost);
                return post;
            }

            return null;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Post save(Post post) {
        if(post == null){
            throw new EntityNullException();
        }


        String queryAdd = "insert into posts (image, description, likes, type, username) values (?,?,?,?,?)";
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(queryAdd)) {

            ps.setBinaryStream(1, post.getImageStream(), post.getImageStream().available());
            ps.setString(2, post.getDescription());
            ps.setInt(3, post.getLikes());
            ps.setString(4, post.getType());
            ps.setString(5, post.getUsername());

            ps.executeUpdate();

        } catch (SQLException | IOException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Post delete(Long aLong) {
        return null;
    }

    @Override
    public Post update(Post post) {
        if (post.getImageStream() == null){
            String sql = "update posts set description = ?, likes = ? where id_post = ?";
            try (Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, post.getDescription());
                ps.setInt(2, post.getLikes());
                ps.setLong(3, post.getId());

                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            String sql = "update posts set image = ?, description = ?, likes = ? where id_post = ?";
            try (Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setBinaryStream(1, post.getImageStream(), post.getImageStream().available());
                ps.setString(2, post.getDescription());
                ps.setInt(3, post.getLikes());
                ps.setLong(4, post.getId());

                ps.executeUpdate();

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }
}
