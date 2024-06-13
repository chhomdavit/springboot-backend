package com.davit.springblog.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Carts")
public class Cart {

	 @Id 
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer cartId; 
	
	 private Integer quantityCart;  
	 
	 private Double totalPrice;

	 @ManyToOne
	 private Users users;

	 @ManyToOne(fetch = FetchType.LAZY)
	 private Post post;
    
}
