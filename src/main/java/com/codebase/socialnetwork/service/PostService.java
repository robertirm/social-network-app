package com.codebase.socialnetwork.service;

import com.codebase.socialnetwork.domain.Entity;
import com.codebase.socialnetwork.domain.Post;
import com.codebase.socialnetwork.repository.paging.Pageable;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface PostService <ID, E extends Entity<ID>> {
    List<E> getAllPosts(String username);
    E getProfilePost(String username);
    void addPost(InputStream imageStream, String description, int likes, String type, String username);
    void updatePost(InputStream imageStream, String description, int likes, Long idPost, String username);
    List<Post> getPrevPosts(String username);
    List<Post> getNextPosts(String username);
    List<Post> getFirstPagePosts(String username);
    List<Post> getPostsOnCurrentPage(int page, String username);
    Long getPostsCount(String username);
    Pageable getPageable();
}
