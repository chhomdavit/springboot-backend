package com.davit.springblog.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name ="news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer newsId;

	@Column(name ="news_title" ,length =100 ,nullable =false)
	private String title;

	@Column(length =150000)
	private String content;

	@Column(name ="news_image")
	private String newsImage;

	@Column(name = "created", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated", insertable = false)
    private LocalDateTime updatedAt;

	private boolean isDeleted = false;

	@ManyToOne
	private Category category;

	@ManyToOne
	private Users users;

	private Integer likeCount = 0;

	// @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    // private Collection<Comment> comments = new HashSet<>();
}
