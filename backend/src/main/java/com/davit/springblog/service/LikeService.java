package com.davit.springblog.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.davit.springblog.dto.LikeDto;

public interface LikeService {

    ResponseEntity<String> likePost(Integer postId, Integer userId);

    List<LikeDto> getLikes();
}
