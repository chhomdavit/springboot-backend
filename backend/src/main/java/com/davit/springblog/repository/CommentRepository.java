package com.davit.springblog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davit.springblog.entity.Comment;
import com.davit.springblog.entity.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment , Integer>{

    List<Comment> findByPost(Post post);
}
