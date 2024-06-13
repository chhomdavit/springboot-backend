package com.davit.springblog.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.davit.springblog.dto.NewsDto;
import com.davit.springblog.dto.NewsResponse;


public interface NewsService {

    NewsDto createNews(NewsDto newsDto ,MultipartFile file ,Integer categoryId, Integer userId) throws IOException;

	NewsDto updateNews(Integer newsId, NewsDto newsDto, MultipartFile file ,Integer categoryId ,Integer userId) throws IOException;

	NewsResponse getAllNews(String keyword, Integer pageNumber, Integer pageSize);

	// PostResponse getPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	// PostResponse getSearchAndPagination(String keyword, Integer pageNumber, Integer pageSize);

	void deleteNews(Integer newsId ,Integer userId) throws IOException;

	// void deletePostNotUser(Integer postId) throws IOException;

	// void softDeletePost(Integer postId ,Integer userId);
}
