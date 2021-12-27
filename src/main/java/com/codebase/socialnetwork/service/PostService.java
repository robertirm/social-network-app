package com.codebase.socialnetwork.service;

import com.codebase.socialnetwork.domain.Entity;
import com.codebase.socialnetwork.domain.Post;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public interface PostService <ID, E extends Entity<ID>> {
    HashSet<E> getAllPosts(String username);
    E getProfilePost(String username);
    void addPost(InputStream imageStream, String description, int likes, String type, String username);
    void updatePost(InputStream imageStream, String description, int likes, Long idPost, String username);
    Set<Post> getPrevPosts(String username);
    Set<Post> getNextPosts(String username);
    Set<Post> getPostsOnCurrentPage(int page, String username);
}
