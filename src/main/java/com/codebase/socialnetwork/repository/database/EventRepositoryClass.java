package com.codebase.socialnetwork.repository.database;

import com.codebase.socialnetwork.domain.Event;
import com.codebase.socialnetwork.domain.Message;
import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.domain.exception.EntityNullException;
import com.codebase.socialnetwork.domain.validator.EventValidator;
import com.codebase.socialnetwork.domain.validator.Validator;
import com.codebase.socialnetwork.repository.EventRepository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codebase.socialnetwork.utils.Constants.DATE_TIME_FORMATTER;

public class EventRepositoryClass implements EventRepository {
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final Validator<Event> eventValidator;

    public EventRepositoryClass(String dbUrl, String dbUsername, String dbPassword,Validator<Event> eventValidator) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.eventValidator = eventValidator;
    }

    @Override
    public Long getCount() {
        return null;
    }

    @Override
    public Event findOne(Long aLong) {
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM events WHERE id_event = " + aLong);
            ResultSet resultSet = ps.executeQuery()){

            if(resultSet.next()){
                String name = resultSet.getString("name_event");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location_event");
                String tags = resultSet.getString("tags");
                String host = resultSet.getString("host");
                InputStream imageStream = resultSet.getBinaryStream("image");

                Timestamp startingDate = resultSet.getTimestamp("starting_date");
                String startingDateString = startingDate.toString().strip().substring(0, 16);
                LocalDateTime dateOfStart = LocalDateTime.parse(startingDateString, DATE_TIME_FORMATTER);

                Timestamp endingDate = resultSet.getTimestamp("ending_date");
                String endingDateString = endingDate.toString().strip().substring(0, 16);
                LocalDateTime dateOfEnd = LocalDateTime.parse(endingDateString, DATE_TIME_FORMATTER);

                Long creatorId = resultSet.getLong("id_creator");
                return new Event(name,dateOfStart,dateOfEnd,location,description,tags,host,imageStream,creatorId);
            }

        }catch (SQLException e){
            System.out.println("da");

            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HashSet<Event> findAll() {
        HashSet<Event> events = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM events");
            ResultSet resultSet = ps.executeQuery()){

            while(resultSet.next()){
                Long idEvent = resultSet.getLong("id_event");
                String name = resultSet.getString("name_event");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location_event");
                String tags = resultSet.getString("tags");
                String host = resultSet.getString("host");
                InputStream imageStream = resultSet.getBinaryStream("image");

                Timestamp startingDate = resultSet.getTimestamp("starting_date");
                String startingDateString = startingDate.toString().strip().substring(0, 16);
                LocalDateTime dateOfStart = LocalDateTime.parse(startingDateString, DATE_TIME_FORMATTER);

                Timestamp endingDate = resultSet.getTimestamp("ending_date");
                String endingDateString = endingDate.toString().strip().substring(0, 16);
                LocalDateTime dateOfEnd = LocalDateTime.parse(endingDateString, DATE_TIME_FORMATTER);

                Long creatorId = resultSet.getLong("id_creator");
                Event event = new Event(name,dateOfStart,dateOfEnd,location,description,tags,host,imageStream,creatorId);
                event.setId(idEvent);
                events.add(event);
            }

        }catch (SQLException e){
            System.out.println("da");

            e.printStackTrace();
        }
        return events;
    }

    @Override
    public Event save(Event entity) {
        if(entity == null){
            throw new EntityNullException();
        }
        eventValidator.validate(entity);

        String queryAdd = "insert into events(name_event, starting_date, ending_date, location_event, description, tags, host,image,id_creator) values (?,?,?,?,?,?,?,?,?)";
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement(queryAdd)) {

            ps.setString(1,entity.getNameEvent());
            ps.setTimestamp(2,Timestamp.valueOf(entity.getStartingDate()));
            ps.setTimestamp(3,Timestamp.valueOf(entity.getEndingDate()));
            ps.setString(4,entity.getLocation());
            ps.setString(5,entity.getDescription());
            ps.setString(6, entity.getTags());
            ps.setString(7, entity.getHost());
            ps.setBinaryStream(8, entity.getImageStream(), entity.getImageStream().available());
            ps.setLong(9,entity.getCreatorId());
            ps.executeUpdate();

        } catch (SQLException | IOException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Event delete(Long aLong) {
        return null;
    }

    @Override
    public Event update(Event entity) {
        return null;
    }

    @Override
    public List<Long> getAllEventsIds() {
        List<Long> ids = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement("SELECT id_event FROM events");
            ResultSet resultSet = ps.executeQuery()){

            while(resultSet.next()) {
                Long id = resultSet.getLong("id_event");
                ids.add(id);
            }

        }catch (SQLException e){
            System.out.println("da");

            e.printStackTrace();
        }
        return ids;
    }

    @Override
    public void addParticipant(Long idUser, Long idEvent) {
        String queryAdd = "insert into participation_relation (id_event,id_user) values (?,?)";
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement  ps = connection.prepareStatement(queryAdd)) {
            ps.setLong(1, idEvent);
            ps.setLong(2, idUser);
            ps.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteParticipant(Long idUser, Long idEvent) {
        String queryDelete = "delete from participation_relation where id_event = (?) and id_user = (?)";
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement  ps = connection.prepareStatement(queryDelete)) {

            ps.setLong(1,idEvent);
            ps.setLong(2,idUser);

            ps.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Long> getAttendedEvents(Long idUser) {
        List<Long> eventIds = new LinkedList<>();
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement("SELECT id_event FROM participation_relation WHERE id_user = " + idUser);
            ResultSet resultSet = ps.executeQuery()){

            while(resultSet.next()){
                Long id = resultSet.getLong("id_event");
                eventIds.add(id);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return eventIds.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Long> getEventsByName(String text) {
        List<Long> ids = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
            PreparedStatement ps = connection.prepareStatement("SELECT id_event FROM events where position('"+text+"' in name_event)>0;");
            ResultSet resultSet = ps.executeQuery()){

            while(resultSet.next()) {
                Long id = resultSet.getLong("id_event");
                ids.add(id);
            }

        }catch (SQLException e){
            System.out.println("da");

            e.printStackTrace();
        }
        return ids;
    }
}
