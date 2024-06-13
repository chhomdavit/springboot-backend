package com.davit.springblog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davit.springblog.entity.Category;
import com.davit.springblog.entity.News;


@Repository
public interface NewsRepository extends JpaRepository<News, Integer>{

    List<News> findByCategory(Category category);

	Page<News> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    Page<News> findAllByIsDeletedFalse(Pageable pageable);

    Page<News> findByTitleContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);
}
