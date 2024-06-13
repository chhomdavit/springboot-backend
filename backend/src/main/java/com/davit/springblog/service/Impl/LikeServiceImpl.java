package com.davit.springblog.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.davit.springblog.dto.LikeDto;
import com.davit.springblog.entity.Like;
import com.davit.springblog.entity.Post;
import com.davit.springblog.entity.Users;
import com.davit.springblog.repository.LikeRepository;
import com.davit.springblog.repository.PostRepository;
import com.davit.springblog.repository.UserRepository;
import com.davit.springblog.service.LikeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    
    @Override
    public ResponseEntity<String> likePost(Integer postId, Integer userId) {
        Optional<Users> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(postId);

        if (user.isPresent() && post.isPresent()) {
            Optional<Like> existingLike = likeRepository.findByUserIdAndPostId(userId, postId);
            Post postEntity = post.get();
            if (existingLike.isPresent()) {
                likeRepository.delete(existingLike.get());
                postEntity.setLikeCount(postEntity.getLikeCount() - 1);
                postRepository.save(postEntity); // Save the updated like count
                return ResponseEntity.ok("Post unliked successfully");
            } else {
                Like like = new Like();
                like.setPostId(postId);
                like.setUserId(userId);
                likeRepository.save(like);
                postEntity.setLikeCount(postEntity.getLikeCount() + 1);
                postRepository.save(postEntity); // Save the updated like count
                return ResponseEntity.ok("Post liked successfully");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Post not found");
        }
    }


    @Override
    public List<LikeDto> getLikes() {
        List<Like> likes = likeRepository.findAll();
        List<LikeDto> likeDtos = likes.stream()
                        .map(like -> modelMapper.map(like, LikeDto.class))
                        .collect(Collectors.toList());
                return likeDtos;
    }

}
