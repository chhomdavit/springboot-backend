package com.davit.springblog.controller;

import java.io.IOException;
import java.time.LocalDateTime;

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
import com.davit.springblog.dto.NewsDto;
import com.davit.springblog.dto.NewsResponse;
import com.davit.springblog.service.NewsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping("/auth/user/{userId}/news")
    public ResponseEntity<NewsDto> createNews(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("categoryId") Integer categoryId,
            @PathVariable Integer userId) {
        NewsDto newsDto = new NewsDto();
        newsDto.setTitle(title);
        newsDto.setContent(content);
        newsDto.setCreatedAt(LocalDateTime.now());
        newsDto.setUpdatedAt(LocalDateTime.now());
        newsDto.setDeleted(false);
        try {
            NewsDto createdNews = newsService.createNews(newsDto, file, categoryId, userId);
            return ResponseEntity.ok().body(createdNews);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/auth/user/{userId}/news/{newsId}")
    public ResponseEntity<NewsDto> updatePoast(
            @PathVariable(value = "newsId") Integer newsId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @PathVariable Integer userId
            ) {
       
            NewsDto newsDto = new NewsDto();
            newsDto.setTitle(title);
            newsDto.setContent(content);
            newsDto.setCreatedAt(LocalDateTime.now());
            newsDto.setUpdatedAt(LocalDateTime.now());
            
        try {
            NewsDto updatedNewsDto = newsService.updateNews(newsId, newsDto, file, categoryId ,userId);
            return ResponseEntity.ok().body(updatedNewsDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/auth/news")
    public ResponseEntity<NewsResponse> getAllNews(
            @RequestParam String keyword,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {

        NewsResponse newsResponse = newsService.getAllNews(keyword, pageNumber,pageSize);
        return ResponseEntity.ok().body(newsResponse);
    }


    @DeleteMapping("/auth/user/{userId}/news/{newsId}")
    public ResponseEntity<String> deleteNews(
        @PathVariable(value = "newsId") Integer newsId,
        @PathVariable(value = "userId") Integer userId
        ) {
        try {
            newsService.deleteNews(newsId, userId);
            return ResponseEntity.ok("Post deleted successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete Post: " + e.getMessage());
        }
    }

}
