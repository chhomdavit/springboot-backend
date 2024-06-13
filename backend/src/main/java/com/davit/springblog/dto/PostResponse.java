package com.davit.springblog.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private  List<PostDto> postDtoList;

    private  Integer pageNumber;

    private  Integer pageSize;

    private  Long totalElements;

    private  int totalPages;
	
    private  boolean isLast;

}
