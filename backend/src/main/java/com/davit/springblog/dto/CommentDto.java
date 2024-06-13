package com.davit.springblog.dto;

import com.davit.springblog.entity.Users;

import lombok.Data;

@Data
public class CommentDto {

    private Integer commentId;

    private String content;

	private Users users;

	private Integer postId;
}
