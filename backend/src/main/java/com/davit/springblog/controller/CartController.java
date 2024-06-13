package com.davit.springblog.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.davit.springblog.dto.CartDto;
import com.davit.springblog.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@PostMapping("/auth/post/{postId}/user/{userId}/carts")
	public ResponseEntity<CartDto> createCart(@PathVariable Integer postId, @PathVariable Integer userId)
			throws IOException {

		try {
			CartDto createdCart = cartService.createCart(userId, postId);
			return ResponseEntity.ok().body(createdCart);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/auth/carts")
	public ResponseEntity<List<CartDto>> getLikes() {
		List<CartDto> carts = cartService.getCarts();
		return ResponseEntity.ok(carts);
	}

	@PutMapping("/auth/user/{userId}/carts/{cartId}")
	public ResponseEntity<CartDto> updateCart(@PathVariable Integer cartId, @PathVariable Integer userId,
			@RequestParam Integer quantityCart) {
		try {
			CartDto updatedCart = cartService.updateCart(cartId, userId, quantityCart);
			return ResponseEntity.ok().body(updatedCart);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("auth/user/{userId}/carts/{cartId}")
	public ResponseEntity<Void> deleteCart(@PathVariable Integer cartId, @PathVariable Integer userId) {
		try {
			cartService.deleteCart(cartId, userId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	  @GetMapping("/auth/user/{userId}/carts")
	  public ResponseEntity<List<CartDto>> getCartsByUserId(@PathVariable Integer userId) {
	    List<CartDto> userCarts = cartService.getCartsByUserId(userId);
	    return ResponseEntity.ok(userCarts);
	  }
}
