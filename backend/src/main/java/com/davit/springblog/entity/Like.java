package com.davit.springblog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity
@Table(name = "Likes")
public class Like {

    @Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer likeId; 

    private Integer userId;

    private Integer postId;
}
