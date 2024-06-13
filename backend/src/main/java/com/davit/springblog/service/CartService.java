package com.davit.springblog.service;

import java.io.IOException;
import java.util.List;

import com.davit.springblog.dto.CartDto;




public interface CartService {

	CartDto createCart(Integer userId, Integer postId) throws IOException;
	
	CartDto updateCart(Integer cartId, Integer userId, Integer quantityCart) throws IOException;
	
    List<CartDto> getCarts();
    
    List<CartDto> getCartsByUserId(Integer userId);
    
    void deleteCart(Integer cartId, Integer userId) throws IOException;
}
