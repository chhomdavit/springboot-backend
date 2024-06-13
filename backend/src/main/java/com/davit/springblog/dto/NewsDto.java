package com.davit.springblog.dto;

import java.time.LocalDateTime;

import com.davit.springblog.entity.Users;

import lombok.Data;

@Data
public class NewsDto {

	private Integer newsId;

	private String title;

	private String content;

	private String newsImage;

	private String newsImageUrl;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

	private boolean isDeleted = false;

	private CategoryDto category;

	private Users users;

	private Integer likeCount = 0;

	// @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    // private Collection<Comment> comments = new HashSet<>();
}
