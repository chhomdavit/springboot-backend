package com.davit.springblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davit.springblog.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

}
