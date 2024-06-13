package com.davit.springblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davit.springblog.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like , Integer>{

    Optional<Like> findByUserIdAndPostId(Integer userId, Integer postId);
}
