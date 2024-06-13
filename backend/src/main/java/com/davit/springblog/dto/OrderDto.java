package com.davit.springblog.dto;

import java.time.LocalDateTime;

import com.davit.springblog.entity.Users;

import lombok.Data;

@Data
public class OrderDto {

    private Integer orderId; 
	
	private Users users;
	
	private Double subTotal = 0.0;
	
	private Double bill = 0.0;
	
	private Double discount = 0.0;
	
	private Double tax = 0.0;
	
//    private List<OrderItem> orderItems = new ArrayList<>();
	
    private LocalDateTime orderDate;
}
