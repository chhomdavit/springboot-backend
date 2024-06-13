package com.davit.springblog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davit.springblog.entity.Category;



@Repository
public interface CategoryRepository extends JpaRepository<Category , Integer>{

    Page<Category> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    
    boolean existsByTitle(String title);
}
