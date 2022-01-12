package com.codebase.socialnetwork.service;

import com.codebase.socialnetwork.domain.Event;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    void addEvent(String nameEvent, LocalDateTime startingDate, LocalDateTime endingDate, String location, String description,
                  String tags, String host, InputStream imageStream,Long creatorId);
    Event findEvent(Long id);
    List<Long> getAllEventsIds();
    void addParticipant(Long idUser, Long idEvent);
    List<Long> getAttendedEvents(Long idUser);
    void deleteParticipant(Long idUser, Long idEvent);
}
