package com.davit.springblog.dto;

import com.davit.springblog.entity.Users;

import lombok.Data;

@Data
public class CartDto {

	private Integer cartId; 
	
	private Integer quantityCart;  
	
	private Double totalPrice;

	private Users users;

	private PostDto post;
    
}
