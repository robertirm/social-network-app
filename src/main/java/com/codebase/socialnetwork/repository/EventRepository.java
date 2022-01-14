package com.codebase.socialnetwork.repository;

import com.codebase.socialnetwork.domain.Event;

import java.util.List;

public interface EventRepository extends Repository<Long, Event>{
    List<Long> getAllEventsIds();
    void addParticipant(Long idUser, Long idEvent);
    void deleteParticipant(Long idUser, Long idEvent);
    List<Long> getAttendedEvents(Long idUser);
    List<Long> getEventsByName(String text);
}
