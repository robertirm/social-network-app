package com.codebase.socialnetwork.service;

import com.codebase.socialnetwork.domain.Post;
import com.codebase.socialnetwork.repository.paging.Page;
import com.codebase.socialnetwork.repository.paging.Pageable;
import com.codebase.socialnetwork.repository.paging.PageableImplementation;
import com.codebase.socialnetwork.repository.paging.PagingRepository;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostServiceClass implements PostService<Long, Post>{
    public final PagingRepository<Long, Post> postRepository;
    private int page = 0;
    private final int size = 9;

    public PostServiceClass(PagingRepository<Long, Post> postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getAllPosts(String username) {
        return postRepository.findAll(username);
    }

    @Override
    public Post getProfilePost(String username) {
        return postRepository.findProfilePost(username);
    }

    @Override
    public void addPost(InputStream imageStream, String description, int likes, String type, String username) {
        Post post = new Post(imageStream, description, likes, type, username, LocalDateTime.now());
        postRepository.save(post);

    }

    @Override
    public void updatePost(InputStream imageStream, String description, int likes, Long idPost, String username) {
        Post profilePost = postRepository.findProfilePost(username);
        if(profilePost != null && profilePost.getId().equals(idPost)){
            profilePost.setImageStream(imageStream);
            profilePost.setDescription(description);
            profilePost.setLikes(likes);
            postRepository.update(profilePost);
            return;
        }

        for(Post post : postRepository.findAll(username)){
            if(post.getId().equals(idPost)){
                post.setImageStream(imageStream);
                post.setDescription(description);
                post.setLikes(likes);
                postRepository.update(post);
                break;
            }
        }

    }

    @Override
    public List<Post> getPrevPosts(String username) {
        if(this.page - 1 >= 0) {
            this.page--;
        }
        return getPostsOnCurrentPage(this.page, username);
    }

    @Override
    public List<Post> getNextPosts(String username) {
        if(this.page + 1 <= this.postRepository.getCount(username) / this.size){
            this.page++;
        }
        return getPostsOnCurrentPage(this.page, username);
    }

    @Override
    public List<Post> getFirstPagePosts(String username) {
        this.page = 0;
        return getPostsOnCurrentPage(this.page, username);
    }

    @Override
    public List<Post> getPostsOnCurrentPage(int page, String username) {
        if(page >= 0) {
            this.page = page;
        }

        Pageable pageable = new PageableImplementation(this.page, this.size);
        Page<Post> postPage = postRepository.findAll(pageable, username);
        return postPage.getContent().collect(Collectors.toList());
    }

    @Override
    public Long getPostsCount(String username){
        return postRepository.getCount(username);
    }

    @Override
    public Pageable getPageable(){
        return new PageableImplementation(this.page, this.size);
    }

}
