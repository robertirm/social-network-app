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
//        LocalDateTime st = LocalDateTime.of(2021, Month.DECEMBER, 15, 12,0,0,0);
//        LocalDateTime sf = LocalDateTime.of(2021, Month.DECEMBER, 30, 12,0,0,0);
//
//        System.out.println("messages date size : " + this.getNumberOfSentMessages(4L,st,sf));
//        System.out.println("messages date size : " + this.getNumberOfReceivedMessages(4L,st,sf));
//
//        this.getMessagesFromFriend(userRepository.findOne(10L), userRepository.findOne(4L), st,sf).forEach(x->{
//            System.out.println(x.getMessageContent() + " | " + x.getMessageDate() + " | ");
//        });
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
                Boolean isAReplyValue = isAReply != 0;
                String messageDateString = messageDate.toString().strip().substring(0, 16);
                LocalDateTime dateOfMessage = LocalDateTime.parse(messageDateString, DATE_TIME_FORMATTER);

                message = new Message(userRepository.findOne(idSender),dateOfMessage,messageContent, isAReplyValue);
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

    @Override
    public Integer getNumberOfSentMessages(Long idUser,LocalDateTime startDate, LocalDateTime endDate) {
        String start = startDate.format(DATE_TIME_FORMATTER);
        String end = endDate.format(DATE_TIME_FORMATTER);

        int counter = 0;
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement("select count(messages.id_message) from messages " +
                                                "where id_sender = "+ idUser +"   and message_date >= '" + start + "' and message_date <= '" +end + "'");
            ResultSet resultSet = ps.executeQuery()){

            if (resultSet.next()){
                counter = resultSet.getInt("count");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return counter;
    }

    @Override
    public Integer getNumberOfReceivedMessages(Long idUser, LocalDateTime startDate, LocalDateTime endDate) {
        String start = startDate.format(DATE_TIME_FORMATTER);
        String end = endDate.format(DATE_TIME_FORMATTER);

        int counter = 0;
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(
                    "select count(messages.id_message) from messages " +
                        "inner join sending_relation on messages.id_message = sending_relation.id_message " +
                        "where messages.id_sender !="+ idUser + " and sending_relation.id_receiver =" + idUser +
                        " and message_date >= '" + start + "' and message_date <= '" +end + "'"
            );
            ResultSet resultSet = ps.executeQuery()){

            if (resultSet.next()){
                counter = resultSet.getInt("count");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return counter;
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

                Message message = new Message(friend,dateOfMessage,messageContent, true);
                message.setId(idMessage);
                messages.add(message);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return messages;
    }
}
