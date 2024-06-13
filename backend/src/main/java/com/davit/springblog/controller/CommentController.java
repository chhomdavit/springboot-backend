package com.davit.springblog.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.davit.springblog.dto.CommentDto;
import com.davit.springblog.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/auth/post/{postId}/user/{userId}")
    public ResponseEntity<CommentDto> createComment (
            @RequestBody CommentDto commentDto,
            @PathVariable Integer postId,
            @PathVariable Integer userId
        ) throws IOException {

        try {
            CommentDto createComment = commentService.createComment(commentDto, userId, postId);
            return ResponseEntity.ok().body(createComment);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/auth/post/{postId}/user/{userId}/comment/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Integer commentId,
            @RequestBody CommentDto commentDto,
            @PathVariable Integer userId,
            @PathVariable Integer postId) throws IOException {
        try {
            CommentDto updateComment = commentService.updateComment(commentId, commentDto, userId, postId);
            return ResponseEntity.ok().body(updateComment);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/auth/comments")
    public ResponseEntity<List<CommentDto>> getComments() {
        List<CommentDto> comments = commentService.getComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/auth/post/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable Integer postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // @DeleteMapping("/auth/user/{userId}/comment/{commentId}")
    // public ResponseEntity<String> deleteComment(
    //         @PathVariable Integer commentId,
    //         @PathVariable Integer userId) {
    //     commentService.deleteComment(commentId, userId);
    //     return ResponseEntity.ok("Category deleted successfully");
    // }


    @DeleteMapping("/auth/user/{userId}/comment/{commentId}")
    public ResponseEntity<String> deleteCommentByAdminAndUserId(
            @PathVariable Integer commentId,
            @PathVariable Integer userId) throws IOException {
  
            commentService.deleteCommentByAdminAndUserId(commentId, userId);
            return ResponseEntity.ok("Comment deleted successfully");
    }

    

}
