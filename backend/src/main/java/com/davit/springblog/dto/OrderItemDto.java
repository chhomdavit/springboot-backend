package com.davit.springblog.dto;


import lombok.Data;

@Data
public class OrderItemDto {

	private Integer orderItemId;
    
    private OrderDto order;
    
	private Integer postId;
	
	private Integer userId;
    
    private Integer quantityOrder;
    
    private Double totalPrice;
}
