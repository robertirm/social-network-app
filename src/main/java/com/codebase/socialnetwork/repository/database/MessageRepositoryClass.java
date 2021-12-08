package com.codebase.socialnetwork.repository.database;

import com.codebase.socialnetwork.domain.Message;
import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;

import static com.codebase.socialnetwork.utils.Constants.DATE_TIME_FORMATTER;

public class MessageRepositoryClass implements Repository<Long, Message> {
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final Repository<Long,User> userRepository;

    public MessageRepositoryClass(String dbUrl, String dbUsername, String dbPassword, Repository<Long, User> userRepository) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.userRepository = userRepository;
    }

    @Override
    public Long getCount() {
        //TODO
        return null;
    }

    @Override
    public Message findOne(Long idMessage) {
        for(Message message: this.findAll()){
            if(message.getId().equals(idMessage))
                return message;
        }
        return null;
    }

    @Override
    public HashSet<Message> findAll() {
        HashSet<Message> messagesEntities = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM messages");
            ResultSet resultSet = ps.executeQuery()){

            while(resultSet.next()){
                    Long idMessage = resultSet.getLong("id_message");
                    Long idSender = resultSet.getLong("id_sender");
                    String messageContent =resultSet.getString("message_content");
                    Timestamp messageDate = resultSet.getTimestamp("message_date");
                    int isAReply = resultSet.getInt("is_a_reply");
                    Boolean isAReplyValue = isAReply != 0;
                    String messageDateString = messageDate.toString().strip().substring(0, 16);
                    LocalDateTime dateOfMessage = LocalDateTime.parse(messageDateString, DATE_TIME_FORMATTER);

                    Message message = new Message(userRepository.findOne(idSender),dateOfMessage,messageContent, isAReplyValue);
                    message.setId(idMessage);
                    messagesEntities.add(message);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        for(Message currentMessage: messagesEntities){
            try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM sending_relation WHERE id_message=" + currentMessage.getId());
                ResultSet resultSet = ps.executeQuery()){

                while(resultSet.next()){
                    Long idReceiver = resultSet.getLong("id_receiver");
                    User foundUser = userRepository.findOne(idReceiver);
                    if(foundUser != null)
                        currentMessage.addReceiver(foundUser);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

            try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM reply_relation WHERE id_message=" + currentMessage.getId());
                ResultSet resultSet = ps.executeQuery()){

                while(resultSet.next()){
                    Long idReceiver = resultSet.getLong("id_reply");
                    Message foundMessage = getMessageById(idReceiver,messagesEntities);
                    if(foundMessage!=null)
                        currentMessage.addReply(foundMessage);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        return messagesEntities;
    }

    private Message getMessageById(Long id, HashSet<Message> messageHashSet){
        for (Message message : messageHashSet){
            if(message.getId().equals(id))
                return message;
        }
        return null;
    }

    @Override
    public Message save(Message message) {
        Message searchedMessage = findOne(message.getId());
        if(searchedMessage == null) {
            message.setId(1L);
            String querySaveMessage = "INSERT INTO messages(id_sender,message_content,message_date,is_a_reply) VALUES (?,?,?,?) returning *";

            try (Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
                 PreparedStatement ps = connection.prepareStatement(querySaveMessage))
            {
                ps.setLong(1,message.getSender().getId());
                ps.setString(2,message.getMessageContent());
                ps.setTimestamp(3,Timestamp.valueOf(message.getMessageDate()));
                ps.setInt(4,((message.isAReply()) ? 1 : 0));

                try(ResultSet resultSet = ps.executeQuery()){
                    if (resultSet.next()) {
                        Long generatedId = resultSet.getLong("id_message");
                        message.setId(generatedId);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String querySaveReceivers = "INSERT INTO sending_relation (id_message, id_receiver) VALUES (?,?)";
            for (User receiver : message.getReceivers()) {
                try (Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
                     PreparedStatement ps = connection.prepareStatement(querySaveReceivers)) {
                    ps.setLong(1, message.getId());
                    ps.setLong(2, receiver.getId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        else{
            String querySaveReplies = "INSERT INTO reply_relation(id_message, id_reply) VALUES (?,?)";
            try (Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
                 PreparedStatement ps = connection.prepareStatement(querySaveReplies)) {
                ps.setLong(1, message.getId());
                ps.setLong(2, message.getReplies().get(message.getReplies().size()-1).getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return findOne(message.getId());
    }

    @Override
    public Message delete(Long idMessage) {
        //TODO
        return null;
    }

    @Override
    public Message update(Message entity) {
        //TODO
        return null;
    }
}
