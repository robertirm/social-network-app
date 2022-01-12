package com.codebase.socialnetwork.service;

import com.codebase.socialnetwork.domain.Event;
import com.codebase.socialnetwork.repository.EventRepository;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

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
}
