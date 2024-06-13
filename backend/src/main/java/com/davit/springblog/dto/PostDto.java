package com.davit.springblog.dto;

import java.util.Collection;
import java.util.Date;

import com.davit.springblog.entity.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDto {

    private Integer postId;

	private String title;

	private String content;

	private String postImage;

	private String postImageUrl;

	private Date addedDate;
	
	private boolean isDeleted = false;
	
    private Integer postQuality = 0;
	
	private Double price = 0.0;

	private Integer likeCount = 0;
	
	private Integer commentCount = 0;

	private CategoryDto category;

	private Users users;

	private Collection<CommentDto> comments;

}
