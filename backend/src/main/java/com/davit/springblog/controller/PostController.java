package com.davit.springblog.controller;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.davit.springblog.constant.AppConstants;
import com.davit.springblog.dto.PostDto;
import com.davit.springblog.dto.PostResponse;
import com.davit.springblog.entity.Users;
import com.davit.springblog.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/auth/post")
    public ResponseEntity<PostDto> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam(value = "price", defaultValue = "0.0") Double price,
            @RequestParam(value = "postQuality", defaultValue = "0") Integer postQuality,
            Authentication authentication
            ) {

        PostDto postDto = new PostDto();
        postDto.setTitle(title);
        postDto.setContent(content);
        postDto.setAddedDate(new Date()); 
        postDto.setDeleted(false); 
        postDto.setPrice(price);
        postDto.setPostQuality(postQuality);

        try {
            Users users = (Users) authentication.getPrincipal();
            PostDto createdPost = postService.createPost(postDto, file ,categoryId , users.getId());
            return ResponseEntity.ok().body(createdPost);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/auth/post/{postId}")
    public ResponseEntity<PostDto> updatePoast(
            @PathVariable(value = "postId") Integer postId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("postQuality") Integer postQuality,
            @RequestParam("price") Double price,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            Authentication authentication
            ) {
       
            PostDto postDto = new PostDto();
            postDto.setTitle(title);
            postDto.setContent(content);
            postDto.setPostQuality(postQuality);
            postDto.setPrice(price);
            postDto.setAddedDate(new Date());
            
        try {
            Users users = (Users) authentication.getPrincipal();
            PostDto updatedPostDto = postService.updatePost(postId, postDto, file, categoryId ,users.getId());
            return ResponseEntity.ok().body(updatedPostDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @GetMapping("/auth/posts")
    public ResponseEntity<PostResponse> getPosts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {

        PostResponse postResponse = postService.getPosts(keyword, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok().body(postResponse);
    }

    @GetMapping("/auth/post")
    public ResponseEntity<PostResponse> getSearchAndPagination(
            @RequestParam String keyword,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {

        PostResponse postResponse = postService.getSearchAndPagination(keyword, pageNumber,pageSize);
        return ResponseEntity.ok().body(postResponse);
    }

    @DeleteMapping("/auth/post/{postId}")
    public ResponseEntity<String> deletePost(
        @PathVariable(value = "postId") Integer postId,
        Authentication authentication
        ) {
        try {
            Users user = (Users) authentication.getPrincipal();
            postService.deletePost(postId , user.getId());
            return ResponseEntity.ok("Post deleted successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete Post: " + e.getMessage());
        }
    }

    @DeleteMapping("auth/post/soft-delete/{postId}")
    public ResponseEntity<Void> softDeletePost(
        @PathVariable Integer postId,
        Authentication authentication
        ) {
        Users user = (Users) authentication.getPrincipal();
        postService.softDeletePost(postId , user.getId());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/auth/get_post")
    public ResponseEntity<PostResponse> getAllPost(
            @RequestParam String keyword,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {

        PostResponse postResponse = postService.getAllPost(keyword, pageNumber,pageSize);
        return ResponseEntity.ok().body(postResponse);
    }


    @DeleteMapping("/auth/delete_post/{postId}")
    public ResponseEntity<String> deletePostNotUser(@PathVariable(value = "postId") Integer postId) {
        try {
            postService.deletePostNotUser(postId);
            return ResponseEntity.ok("Post deleted successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete Post: " + e.getMessage());
        }
    }
    
    @GetMapping("/auth/post/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(value = "postId") Integer postId) throws IOException {
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok().body(postDto);
    }

}
