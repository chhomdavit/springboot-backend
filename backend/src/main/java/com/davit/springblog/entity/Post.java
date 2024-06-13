package com.davit.springblog.entity;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postId;

	@Column(name ="post_title" ,length =100 ,nullable =false)
	private String title;

	private String content;

	@Column(name ="post_image")
	private String postImage;

	private Date addedDate;

	private boolean isDeleted = false;

	@ManyToOne
	private Category category;

	@ManyToOne
	private Users users;
	
	private Integer postQuality = 0;
	
	private Double price = 0.0;

	private Integer likeCount = 0;
	
	private Integer commentCount = 0;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Collection<Comment> comments = new HashSet<>();
}
