package com.codebase.socialnetwork.service;

import com.codebase.socialnetwork.domain.Event;
import com.codebase.socialnetwork.repository.EventRepository;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class EventServiceClass implements EventService{

    EventRepository eventRepository;

    public EventServiceClass(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void addEvent(String nameEvent, LocalDateTime startingDate, LocalDateTime endingDate, String location, String description, String tags, String host, InputStream imageStream,Long creatorId) {
        Event event = new Event(nameEvent, startingDate, endingDate, location, description, tags, host, imageStream,creatorId);
        eventRepository.save(event);
    }

    @Override
    public Event findEvent(Long id) {
        return eventRepository.findOne(id);
    }

    @Override
    public List<Long> getAllEventsIds() {
        return eventRepository.getAllEventsIds();
    }

    @Override
    public void addParticipant(Long idUser, Long idEvent) {
        eventRepository.addParticipant(idUser, idEvent);
    }

    @Override
    public List<Long> getAttendedEvents(Long idUser) {
        return eventRepository.getAttendedEvents(idUser);
    }

    @Override
    public void deleteParticipant(Long idUser, Long idEvent) {
        eventRepository.deleteParticipant(idUser, idEvent);
    }

    @Override
    public List<Event> getAllUserEvents(Long id) {
        HashSet<Event> events = eventRepository.findAll();
        List<Long> longs = eventRepository.getAttendedEvents(id);

        List<Event> eventList = new ArrayList<>();
        for(var eve : events){
            for(var l : longs)
                if(Objects.equals(l, eve.getId())){
                    eventList.add(eve);
                    break;
                }
        }

        return eventList;
    }

    @Override
    public List<Long> getEventsByName(String text) {
        return eventRepository.getEventsByName(text);
    }
}
