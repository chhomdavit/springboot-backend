package com.davit.springblog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity
@Table(name = "order-item")
public class OrderItem {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    
     @ManyToOne(fetch = FetchType.LAZY)
	 private Post post;
     
     @ManyToOne(fetch = FetchType.LAZY)
	 private Users users;
    
     private Integer quantityOrder;
    
     private Double totalPrice;
}
