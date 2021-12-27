package com.codebase.socialnetwork.repository.paging;


import com.codebase.socialnetwork.domain.Entity;
import com.codebase.socialnetwork.repository.PostRepository;

public interface PagingRepository<ID ,
        E extends Entity<ID>>
        extends PostRepository<ID, E> {

    Page<E> findAll(Pageable pageable, String username);
}
