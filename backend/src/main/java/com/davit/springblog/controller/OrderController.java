package com.davit.springblog.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.davit.springblog.dto.OrderDto;
import com.davit.springblog.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;

	
//	@PostMapping("/auth/user/{userId}/orders")
//	public ResponseEntity<OrderDto> createOrder(@PathVariable Integer userId) throws IOException {
//        try {
//            OrderDto createdOrder = orderService.createOrder(userId);
//            return ResponseEntity.ok().body(createdOrder);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
	
	@PostMapping("/auth/user/{userId}/orders")
    public ResponseEntity<OrderDto> createOrder(
    		@PathVariable Integer userId,
            @RequestBody OrderDto orderDto) throws IOException {
        try {
            OrderDto createdOrder = orderService.createOrder(orderDto, userId);
            return ResponseEntity.ok().body(createdOrder);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
