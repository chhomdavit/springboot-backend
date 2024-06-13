package com.davit.springblog.service;

import java.io.IOException;
import java.util.List;

import com.davit.springblog.dto.CommentDto;

public interface CommentService {

        CommentDto createComment (CommentDto commentDto, Integer userId, Integer postId) throws IOException;

        CommentDto updateComment (Integer commentId, CommentDto commentDto, Integer userId, Integer postId) throws IOException;

        List <CommentDto> getComments();
        
        void  deleteComment (Integer commentId, Integer userId);

        List<CommentDto> getCommentsByPostId(Integer postId);

        void deleteCommentByAdminAndUserId(Integer commentId, Integer userId) throws IOException;

        // CategoryDto getCategory (Integer categoryId);

        // List<CategoryDto> getCategories ();

        // CategoryResponse getPaginationAndSearch(String keyword, Integer pageNumber, Integer pageSize);
}
