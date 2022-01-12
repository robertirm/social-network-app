package com.codebase.socialnetwork.domain;

import java.io.InputStream;
import java.time.LocalDateTime;

public class Post extends Entity<Long>{
    private InputStream imageStream;
    private String description;
    private int likes;
    private final String type;
    private final String username;
    private final LocalDateTime date;

    public Post(InputStream imageStream, String description, int likes, String type, String username, LocalDateTime date) {
        this.imageStream = imageStream;
        this.description = description;
        this.likes = likes;
        this.type = type;
        this.username = username;
        this.date = date;
    }

    public InputStream getImageStream() {
        return imageStream;
    }

    public String getDescription() {
        return description;
    }

    public int getLikes() {
        return likes;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getDate(){return this.date; }

    public void setImageStream(InputStream imageStream) {
        this.imageStream = imageStream;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
