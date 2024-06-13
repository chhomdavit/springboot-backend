package com.davit.springblog.service.Impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.davit.springblog.dto.CommentDto;
import com.davit.springblog.entity.Comment;
import com.davit.springblog.entity.Post;
import com.davit.springblog.entity.Role;
import com.davit.springblog.entity.Users;
import com.davit.springblog.execption.ResourceNotFoundException;
import com.davit.springblog.repository.CommentRepository;
import com.davit.springblog.repository.PostRepository;
import com.davit.springblog.repository.UserRepository;
import com.davit.springblog.service.CommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

        private final UserRepository userRepository;
        private final PostRepository postRepository;
        private final CommentRepository commentRepository;
        private final ModelMapper modelMapper;

        @Override
        public CommentDto createComment(CommentDto commentDto, Integer userId, Integer postId) throws IOException {

                Users user = this.userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

                Comment comment = modelMapper.map(commentDto, Comment.class);
                comment.setUsers(user);
                comment.setPost(post);
                Comment saved = commentRepository.save(comment);
                
                post.setCommentCount(post.getCommentCount() + 1);
                postRepository.save(post);
                
                CommentDto response = modelMapper.map(saved, CommentDto.class);
                return response;
        }

        @Override
        public CommentDto updateComment(Integer commentId, CommentDto commentDto, Integer userId, Integer postId) {
                Users user = this.userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

                Comment existingComment = commentRepository.findById(commentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Comment not found with id: " + commentId));

                existingComment.setContent(commentDto.getContent());
                existingComment.setPost(post);
                existingComment.setUsers(user);
                Comment updatedComment = commentRepository.save(existingComment);
                CommentDto response = modelMapper.map(updatedComment, CommentDto.class);
                return response;
        }

        @Override
        public List<CommentDto> getComments() {
               List<Comment> comments = commentRepository.findAll();
               List<CommentDto> commentDtos = comments.stream()
                                .map(comment -> modelMapper.map(comment, CommentDto.class))
                                .collect(Collectors.toList());
                return commentDtos;
        }

        @Override
        public void deleteComment(Integer commentId, Integer userId) {
                Comment existingComment = commentRepository.findById(commentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Category not found with id: " + commentId));

                Users user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "userId not found with id: " + userId));

                if (!existingComment.getUsers().equals(user)) {
                        throw new ResourceNotFoundException("userId not found with id: " + userId);
                }
                this.commentRepository.delete(existingComment);
        }

        @Override
        public List<CommentDto> getCommentsByPostId(Integer postId) {
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

                List<Comment> comments = commentRepository.findByPost(post);
                List<CommentDto> commentDtos = comments.stream()
                                .map(comment -> modelMapper.map(comment, CommentDto.class))
                                .collect(Collectors.toList());
                return commentDtos;
        }

        @Override
        public void deleteCommentByAdminAndUserId(Integer commentId, Integer userId) throws IOException {
                Comment existingComment = commentRepository.findById(commentId)
                                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

                Users user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

                if (!existingComment.getUsers().equals(user) && !user.getRole().equals(Role.ADMIN)) {
                        throw new ResourceNotFoundException("User not authorized to delete this comment");
                }
               
                Post post = existingComment.getPost();
                if (post != null) {
                    post.setCommentCount(post.getCommentCount() - 1);
                    postRepository.save(post);
                }
                
                commentRepository.delete(existingComment);
        }

}
