package com.codebase.socialnetwork.service;

import com.codebase.socialnetwork.domain.Post;
import com.codebase.socialnetwork.repository.Repository;

import java.io.InputStream;
import java.util.HashSet;

public class PostServiceClass implements PostService<Long, Post>{
    public final Repository<Long, Post> postRepository;

    public PostServiceClass(Repository<Long, Post> postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public HashSet<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void addPost(InputStream imageStream, String description, int likes, String type, String username) {
        Post post = new Post(imageStream, description, likes, type, username);
        postRepository.save(post);

    }

    @Override
    public void updatePost(InputStream imageStream, String description, int likes, Long idPost) {
        for(Post post : postRepository.findAll()){
            if(post.getId().equals(idPost)){
                post.setImageStream(imageStream);
                post.setDescription(description);
                post.setLikes(likes);
                postRepository.update(post);
                break;
            }
        }
    }
}
