package com.davit.springblog.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Orders")
public class Order {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderId; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Users users;
	
	private Double subTotal = 0.0;
	
	private Double bill = 0.0;
	
	private Double discount = 0.0;
	
	private Double tax = 0.0;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
	
	@Column(name = "orderDate", updatable = false)
    private LocalDateTime orderDate;
	
}
