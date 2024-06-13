package com.davit.springblog.service.Impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.davit.springblog.dto.CartDto;
import com.davit.springblog.entity.Cart;
import com.davit.springblog.entity.Post;
import com.davit.springblog.entity.Users;
import com.davit.springblog.execption.EmptyOrNotNullException;
import com.davit.springblog.execption.ResourceNotFoundException;
import com.davit.springblog.repository.CartRepository;
import com.davit.springblog.repository.PostRepository;
import com.davit.springblog.repository.UserRepository;
import com.davit.springblog.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final PostRepository postRepository;
	private final ModelMapper modelMapper;


	@Override
	public List<CartDto> getCarts() {
		List<Cart> carts = cartRepository.findAll();
		List<CartDto> cartDtos = carts.stream().map(cart -> modelMapper.map(cart, CartDto.class))
				.collect(Collectors.toList());
		return cartDtos;
	}

	
	@Override
    public CartDto createCart(Integer userId, Integer postId) throws IOException {
        Optional<Users> optionalUser = userRepository.findById(userId);
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalUser.isPresent() && optionalPost.isPresent()) {
            Users user = optionalUser.get();
            Post post = optionalPost.get();

            Optional<Cart> existingCart = cartRepository.findByUsersAndPost(user, post);

            Cart cart;
            if (existingCart.isPresent()) {
                cart = existingCart.get();
                cart.setQuantityCart(cart.getQuantityCart() + 1);
            } else {
                cart = new Cart();
                cart.setUsers(user);
                cart.setPost(post);
                cart.setQuantityCart(1);
            }
            
            cart.setTotalPrice(cart.getQuantityCart() * post.getPrice());
            
            if (post.getPostQuality() <= 0) {
                throw new EmptyOrNotNullException("Post quantity is insufficient");
            }
            post.setPostQuality(post.getPostQuality() - 1); 
            postRepository.save(post);

            cartRepository.save(cart);
            return modelMapper.map(cart, CartDto.class);
        } else {
            throw new ResourceNotFoundException("User or Post not found");
        }
    }
	

	@Override
    public CartDto updateCart(Integer cartId, Integer userId, Integer quantityCart) throws IOException {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            Post post = cart.getPost();
            Users user = cart.getUsers();

            if (!user.getId().equals(userId)) {
                throw new ResourceNotFoundException("User not authorized to update this cart");
            }

            int oldQuantity = cart.getQuantityCart();
            int quantityDifference = quantityCart - oldQuantity;

            if (post.getPostQuality() < quantityDifference) {
                throw new EmptyOrNotNullException("Post quantity is insufficient");
            }

            cart.setQuantityCart(quantityCart);
            cart.setTotalPrice(quantityCart * post.getPrice());

            post.setPostQuality(post.getPostQuality() - quantityDifference);
            postRepository.save(post);

            cartRepository.save(cart);
            return modelMapper.map(cart, CartDto.class);
        } else {
            throw new ResourceNotFoundException("Cart not found");
        }
    }


	@Override
    public void deleteCart(Integer cartId, Integer userId) throws IOException {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            Post post = cart.getPost();
            Users user = cart.getUsers();

            if (!user.getId().equals(userId)) {
                throw new ResourceNotFoundException("User not authorized to delete this cart");
            }

            int cartQuantity = cart.getQuantityCart();
            post.setPostQuality(post.getPostQuality() + cartQuantity);

            postRepository.save(post);
            cartRepository.delete(cart);
        } else {
            throw new ResourceNotFoundException("Cart not found");
        }
    }
	
	
	  @Override
	  public List<CartDto> getCartsByUserId(Integer userId) {
	    List<Cart> userCarts = cartRepository.findByUsersId(userId);
	    return userCarts.stream()
	        .map(cart -> modelMapper.map(cart, CartDto.class))
	        .collect(Collectors.toList());
	  }
}
