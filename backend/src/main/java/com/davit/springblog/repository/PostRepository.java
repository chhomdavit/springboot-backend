package com.davit.springblog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davit.springblog.entity.Category;
import com.davit.springblog.entity.Post;



@Repository
public interface PostRepository extends JpaRepository<Post,Integer>{

    List<Post> findByCategory(Category category);

	Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Post> findAllByIsDeletedFalse(Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);
    
}
