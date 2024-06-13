package com.davit.springblog.service.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.davit.springblog.dto.PostDto;
import com.davit.springblog.dto.PostResponse;
import com.davit.springblog.entity.Category;
import com.davit.springblog.entity.Post;
import com.davit.springblog.entity.Users;
import com.davit.springblog.execption.EmptyOrNotNullException;
import com.davit.springblog.execption.ResourceNotFoundException;
import com.davit.springblog.repository.CategoryRepository;
import com.davit.springblog.repository.PostRepository;
import com.davit.springblog.repository.UserRepository;
import com.davit.springblog.service.FileUploadService;
import com.davit.springblog.service.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final ModelMapper modelMapper;

    @Value("${project.upload}")
    private String path;
    @Value("${base.url}")
    private String baseUrl;

    
    @Override
    public PostDto createPost(PostDto postDto, MultipartFile file, Integer categoryId, Integer userId)
            throws IOException {
        // Check if title is null or empty
        if (postDto.getTitle() == null || postDto.getTitle().isEmpty()) {
            throw new EmptyOrNotNullException("Post title cannot be null or empty");
        }

        // Save file if provided
        String newFileName = null;
        if (file != null && !file.isEmpty()) {
            newFileName = fileUploadService.saveFile(file, path);
            postDto.setPostImage(newFileName);
        }

        // Retrieve category
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + categoryId));

        // Retrieve category
        Users users = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + userId));

        // Map DTO to entity
        Post post = modelMapper.map(postDto, Post.class);
        post.setUsers(users);
        post.setCategory(category);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setPrice(0.00);
        post.setPostQuality(0);

        // Save post
        Post saved = postRepository.save(post);

        // Construct response DTO
        PostDto response = modelMapper.map(saved, PostDto.class);
        if (newFileName != null) {
            String postImageUrl = baseUrl + "/auth/" + newFileName;
            response.setPostImageUrl(postImageUrl);
        }
        return response;
    }

  @Override
  public PostDto updatePost(Integer postId, PostDto postDto, MultipartFile file, Integer categoryId ,Integer userId)
          throws IOException {
      
      Users user = this.userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
              
      Post existingPost = postRepository.findById(postId)
              .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

      if (!existingPost.getUsers().equals(user)) {
              throw new ResourceNotFoundException("User not found with id: " + userId);
      }

      String newSaveFile = null;
      try {
          if (file != null && !file.isEmpty()) {
              if (existingPost.getPostImage() != null && !existingPost.getPostImage().isEmpty()) {
                  Files.deleteIfExists(Paths.get(path, existingPost.getPostImage()));
              }
              newSaveFile = fileUploadService.saveFile(file, path);
          } else {
              newSaveFile = existingPost.getPostImage();
          }
      } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException("Error occurred while updating post", e);
      }
      postDto.setPostImage(newSaveFile);

      Post updated = modelMapper.map(postDto, Post.class);
      updated.setPostId(existingPost.getPostId());
      updated.setLikeCount(existingPost.getLikeCount()); 
      updated.setCommentCount(existingPost.getCommentCount()); 
      updated.setUsers(user);
      if (categoryId != null) {
          Category category = categoryRepository.findById(categoryId)
                  .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
          updated.setCategory(category);
      } else {
          updated.setCategory(existingPost.getCategory());
      }

      Post saved = postRepository.save(updated);
      PostDto response = modelMapper.map(saved, PostDto.class);
      if (newSaveFile != null) {
          String postImageUrl = baseUrl + "/auth/" + newSaveFile;
          response.setPostImageUrl(postImageUrl);
      }
      return response;
  }

    @Override
    public PostResponse getPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        
        Pageable pageable = (keyword == null || keyword.trim().isEmpty()) ? PageRequest.of(pageNumber, pageSize, sort) : PageRequest.of(pageNumber, pageSize);

        Page<Post> postPages;
        if (keyword == null || keyword.trim().isEmpty()) {
            postPages = postRepository.findAllByIsDeletedFalse(pageable); 
        } else {
            postPages = postRepository.findByTitleContainingIgnoreCaseAndIsDeletedFalse(keyword, pageable);
        }

        List<PostDto> postDtos = postPages.getContent().stream()
            .map(post -> {
                PostDto postDto = modelMapper.map(post, PostDto.class);
                postDto.setPostImageUrl(baseUrl + "/auth/" + post.getPostImage());
                return postDto;
            })
            .collect(Collectors.toList());
        
        return new PostResponse(postDtos, pageNumber, pageSize, 
                postPages.getTotalElements(),
                postPages.getTotalPages(), 
                postPages.isLast()
        );
    }

    @Override
    public PostResponse getSearchAndPagination(String keyword, Integer pageNumber, Integer pageSize) {
        
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Post> postPages;
        if (keyword == null || keyword.trim().isEmpty()) {
            postPages = postRepository.findAllByIsDeletedFalse(pageable); 
        } else {
            postPages = postRepository.findByTitleContainingIgnoreCaseAndIsDeletedFalse(keyword, pageable);
        }

        List<PostDto> postDtos = postPages.getContent().stream()
            .map(post -> {
                PostDto postDto = modelMapper.map(post, PostDto.class);
                postDto.setPostImageUrl(baseUrl + "/auth/" + post.getPostImage());
                return postDto;
            })
            .collect(Collectors.toList());

        return new PostResponse(postDtos, pageNumber, pageSize, 
                    postPages.getTotalElements(),
                    postPages.getTotalPages(), 
                    postPages.isLast()
        );
    }

    @Override
    public void deletePost(Integer postId, Integer userId) throws IOException {
    
        Post existingPost = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        if (!existingPost.getUsers().getId().equals(userId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        String uploadDirectory = path;
        String photoFileName = existingPost.getPostImage();

        if (photoFileName != null && !photoFileName.isEmpty()) {
            try {
                Files.deleteIfExists(Paths.get(uploadDirectory, photoFileName));
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Failed to delete photo", e);
            }
        }
        postRepository.deleteById(postId);
    }

    @Override
    public void softDeletePost(Integer postId, Integer userId) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        if (!existingPost.getUsers().getId().equals(userId)) {
            throw new ResourceNotFoundException("Users not found with id: " + userId);
        }

        existingPost.setDeleted(true);
        postRepository.save(existingPost);
    }

    @Override
    public PostResponse getAllPost(String keyword, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Post> postPages = postRepository.findByTitleContainingIgnoreCase(keyword, pageable);

        List<PostDto> postDtos = postPages.getContent().stream().map(post -> {
            PostDto postDto = modelMapper.map(post, PostDto.class);
            return postDto;
        }).collect(Collectors.toList());

        return new PostResponse(postDtos, pageNumber, pageSize, postPages.getTotalElements(),
                postPages.getTotalPages(), postPages.isLast());
    }

    @Override
    public void deletePostNotUser(Integer postId) throws IOException {
     
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        String uploadDirectory = path;
        String photoFileName = existingPost.getPostImage();

        if (photoFileName != null && !photoFileName.isEmpty()) {
            try {
                Files.deleteIfExists(Paths.get(uploadDirectory, photoFileName));
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Failed to delete photo", e);
            }
        }
        postRepository.deleteById(postId);
    }


	@Override
	public PostDto getPostById(Integer postId) throws IOException {
		  Optional<Post> postOptional = postRepository.findById(postId);
	        Post post = postOptional.orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
	        return modelMapper.map(post, PostDto.class);
	}

    

}
