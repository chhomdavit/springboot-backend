package com.davit.springblog.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="category")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Integer categoryId;
	
	@Column(name ="title", length =100, nullable =false)
	private String title;

	@Column(name ="description")
	private String description;

	@ManyToOne
	private Users users;
	
	@OneToMany(mappedBy ="category" ,cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<Post>();
}
