package com.codebase.socialnetwork.repository.database;

import com.codebase.socialnetwork.domain.Message;
import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.repository.MessageRepository;
import com.codebase.socialnetwork.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.codebase.socialnetwork.utils.Constants.DATE_TIME_FORMATTER;

//public class MessageRepositoryClass implements Repository<Long, Message>
public class MessageRepositoryClass implements MessageRepository {
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
    public Message findOne(Long id) {
        Message message =null;
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM messages WHERE id_message = " + id);
            ResultSet resultSet = ps.executeQuery()){

            while(resultSet.next()){
                Long idMessage = resultSet.getLong("id_message");
                Long idSender = resultSet.getLong("id_sender");
                String messageContent =resultSet.getString("message_content");
                Timestamp messageDate = resultSet.getTimestamp("message_date");
                int isAReply = resultSet.getInt("is_a_reply");
                String repliedTo = resultSet.getString("replied_to");

                Boolean isAReplyValue = isAReply != 0;
                String messageDateString = messageDate.toString().strip().substring(0, 16);
                LocalDateTime dateOfMessage = LocalDateTime.parse(messageDateString, DATE_TIME_FORMATTER);

                message = new Message(userRepository.findOne(idSender),dateOfMessage,messageContent, isAReplyValue,repliedTo);
                message.setId(idMessage);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        if(message != null) {
            try (Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
                 PreparedStatement ps = connection.prepareStatement("SELECT * FROM sending_relation WHERE id_message=" + message.getId());
                 ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    Long idReceiver = resultSet.getLong("id_receiver");
                    User foundUser = userRepository.findOne(idReceiver);
                    if (foundUser != null)
                        message.addReceiver(foundUser);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try (Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
                 PreparedStatement ps = connection.prepareStatement("SELECT * FROM reply_relation WHERE id_message=" + message.getId());
                 ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    Long idReceiver = resultSet.getLong("id_reply");
                    Message foundMessage = this.findOne(idReceiver);
                    if (foundMessage != null)
                        message.addReply(foundMessage);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return message;
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
                    String repliedTo = resultSet.getString("replied_to");
                    Boolean isAReplyValue = isAReply != 0;
                    String messageDateString = messageDate.toString().strip().substring(0, 16);
                    LocalDateTime dateOfMessage = LocalDateTime.parse(messageDateString, DATE_TIME_FORMATTER);

                    Message message = new Message(userRepository.findOne(idSender),dateOfMessage,messageContent, isAReplyValue,repliedTo);
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
            String querySaveMessage = "INSERT INTO messages(id_sender,message_content,message_date,is_a_reply,replied_to) VALUES (?,?,?,?,?) returning *";

            try (Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
                 PreparedStatement ps = connection.prepareStatement(querySaveMessage))
            {
                ps.setLong(1,message.getSender().getId());
                ps.setString(2,message.getMessageContent());
                ps.setTimestamp(3,Timestamp.valueOf(message.getMessageDate()));
                ps.setInt(4,((message.isAReply()) ? 1 : 0));
                ps.setString(5,message.getRepliedTo());


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

    @Override
    public List<Message> getNumberOfSentMessages(Long idUser,LocalDateTime startDate, LocalDateTime endDate) {
        String start = startDate.format(DATE_TIME_FORMATTER);
        String end = endDate.format(DATE_TIME_FORMATTER);

        List<Message> messages = new ArrayList<>();


        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement("select messages.id_message,message_content,message_date from messages " +
                                                "where id_sender = "+ idUser +"   and message_date >= '" + start + "' and message_date <= '" +end + "'");
            ResultSet resultSet = ps.executeQuery()){

            while (resultSet.next()){
                long idMessage = resultSet.getLong("id_message");
                String content = resultSet.getString("message_content");
                Timestamp messageDate = resultSet.getTimestamp("message_date");
                String messageDateString = messageDate.toString().strip().substring(0, 16);
                LocalDateTime dateOfMessage = LocalDateTime.parse(messageDateString, DATE_TIME_FORMATTER);

                Message message = new Message(null,dateOfMessage,content,false,"");

                try (Connection connection1 = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
                     PreparedStatement ps1 = connection1.prepareStatement("SELECT * FROM sending_relation WHERE id_message=" +idMessage);
                     ResultSet resultSet1 = ps1.executeQuery()) {

                    while (resultSet1.next()) {
                        Long idReceiver = resultSet1.getLong("id_receiver");
                        User foundUser = userRepository.findOne(idReceiver);
                        if (foundUser != null)
                            message.addReceiver(foundUser);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                messages.add(message);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public List<Message> getNumberOfReceivedMessages(Long idUser, LocalDateTime startDate, LocalDateTime endDate) {
        String start = startDate.format(DATE_TIME_FORMATTER);
        String end = endDate.format(DATE_TIME_FORMATTER);

        List<Message> messages = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(
                    "select distinct message_content,message_date,messages.id_sender from messages " +
                        "inner join sending_relation on messages.id_message = sending_relation.id_message " +
                        "where messages.id_sender !="+ idUser + " and sending_relation.id_receiver =" + idUser +
                        " and message_date >= '" + start + "' and message_date <= '" +end + "'"
            );
            ResultSet resultSet = ps.executeQuery()){

            while (resultSet.next()){
                Long idSender = resultSet.getLong("id_sender");
                String content = resultSet.getString("message_content");
                Timestamp messageDate = resultSet.getTimestamp("message_date");
                String messageDateString = messageDate.toString().strip().substring(0, 16);
                LocalDateTime dateOfMessage = LocalDateTime.parse(messageDateString, DATE_TIME_FORMATTER);
                Message message = new Message(userRepository.findOne(idSender), dateOfMessage,content,false,"");
                messages.add(message);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public List<Message> getMessagesFromFriend(User user, User friend, LocalDateTime startDate, LocalDateTime endDate) {
        String start = startDate.format(DATE_TIME_FORMATTER);
        String end = endDate.format(DATE_TIME_FORMATTER);

        List<Message> messages = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(
                    "select distinct messages.id_message,message_content,message_date from messages " +
                        "inner join sending_relation ON messages.id_message = sending_relation.id_message " +
                        "where messages.id_sender = " + friend.getId() + " and sending_relation.id_receiver = " + user.getId() +
                        " and message_date >= '" + start + "' and message_date <= '" +end + "'");

            ResultSet resultSet = ps.executeQuery()){
            while (resultSet.next()){
                Long idMessage = resultSet.getLong("id_message");
                String messageContent =resultSet.getString("message_content");

                Timestamp messageDate = resultSet.getTimestamp("message_date");
                String messageDateString = messageDate.toString().strip().substring(0, 16);
                LocalDateTime dateOfMessage = LocalDateTime.parse(messageDateString, DATE_TIME_FORMATTER);

                Message message = new Message(friend,dateOfMessage,messageContent, true,"");
                message.setId(idMessage);
                messages.add(message);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return messages;
    }
}
