package com.davit.springblog.service;

import java.io.IOException;

import com.davit.springblog.dto.OrderDto;

public interface OrderService {

//	CartDto createCart(Integer userId, Integer postId) throws IOException;
	
//	 OrderDto createOrder(Integer userId) throws IOException;
	
	OrderDto createOrder(OrderDto orderDto, Integer userId) throws IOException;
}
