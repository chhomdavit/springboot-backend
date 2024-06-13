package com.davit.springblog.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davit.springblog.dto.LikeDto;
import com.davit.springblog.entity.Users;
import com.davit.springblog.service.LikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/auth/{postId}/likes")
    public ResponseEntity<String> likePost(
            @PathVariable(value = "postId") Integer postId,
            Authentication authentication) {
        Users user = (Users) authentication.getPrincipal();
        Integer userId = user.getId();

        return likeService.likePost(postId, userId);
    }

    @GetMapping("/auth/likes")
    public ResponseEntity<List<LikeDto>> getLikes() {
        List<LikeDto> likes = likeService.getLikes();
        return ResponseEntity.ok(likes);
    }
}
