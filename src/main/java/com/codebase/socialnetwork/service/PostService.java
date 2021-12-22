package com.codebase.socialnetwork.service;

import com.codebase.socialnetwork.domain.Entity;

import java.io.InputStream;
import java.util.HashSet;

public interface PostService <ID, E extends Entity<ID>> {
    HashSet<E> getAllPosts();
    void addPost(InputStream imageStream, String description, int likes, String type, String username);
    void updatePost(InputStream imageStream, String description, int likes, Long idPost);
}
