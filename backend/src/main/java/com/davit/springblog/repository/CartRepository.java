package com.davit.springblog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davit.springblog.entity.Cart;
import com.davit.springblog.entity.Post;
import com.davit.springblog.entity.Users;



@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>{

	List<Cart> findByPost(Post post);

	Optional<Cart> findByUsersAndPost(Users users, Post post);
	
	List<Cart> findByUsersId(Integer userId);
}
