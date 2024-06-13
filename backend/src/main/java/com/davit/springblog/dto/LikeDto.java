package com.davit.springblog.dto;

import lombok.Data;

@Data
public class LikeDto {

	private Integer likeId; 

    private Integer userId;

    private Integer postId;
}
