package com.davit.springblog.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davit.springblog.entity.Users;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Page<Users> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    // Optional<Users> findByEmail(String email);
    
    Optional<Users> findByEmail(String email);

    Optional<Users> findByName(String name);
}
