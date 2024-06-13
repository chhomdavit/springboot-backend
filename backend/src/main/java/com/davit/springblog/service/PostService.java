package com.davit.springblog.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.davit.springblog.dto.PostDto;
import com.davit.springblog.dto.PostResponse;

public interface PostService {
   
	PostDto createPost(PostDto postDto ,MultipartFile file ,Integer categoryId, Integer userId) throws IOException;

	PostDto updatePost(Integer postId, PostDto postDto, MultipartFile file ,Integer categoryId ,Integer userId) throws IOException;

	PostResponse getAllPost(String keyword, Integer pageNumber, Integer pageSize);

	PostResponse getPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	PostResponse getSearchAndPagination(String keyword, Integer pageNumber, Integer pageSize);

	void deletePost(Integer postId ,Integer userId) throws IOException;

	void deletePostNotUser(Integer postId) throws IOException;

	void softDeletePost(Integer postId ,Integer userId);
	
	PostDto getPostById(Integer postId) throws IOException;
}
