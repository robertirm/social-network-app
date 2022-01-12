package com.codebase.socialnetwork.domain;

import java.io.InputStream;
import java.time.LocalDateTime;

public class Event extends Entity<Long>{
    private String nameEvent;
    private LocalDateTime startingDate;
    private LocalDateTime endingDate;
    private String location;
    private String description;
    private String tags;
    private String host;
    private InputStream imageStream;
    private Long creatorId;



    public Event(String nameEvent, LocalDateTime startingDate, LocalDateTime endingDate, String location, String description,
                 String tags, String host, InputStream imageStream, Long creatorId) {
        this.nameEvent = nameEvent;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.location = location;
        this.description = description;
        this.tags = tags;
        this.host = host;
        this.imageStream = imageStream;
        this.creatorId = creatorId;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public LocalDateTime getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDateTime startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDateTime getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDateTime endingDate) {
        this.endingDate = endingDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public InputStream getImageStream() {
        return imageStream;
    }

    public void setImageStream(InputStream imageStream) {
        this.imageStream = imageStream;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "nameEvent='" + nameEvent + '\'' +
                ", startingDate=" + startingDate +
                ", endingDate=" + endingDate +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", host='" + host + '\'' +
                ", imageStream=" + imageStream +
                '}';
    }
}
