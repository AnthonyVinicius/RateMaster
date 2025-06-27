package br.com.ifpe.ratemaster.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_review")
public class ReviewModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String comment;
	private Integer rating;
	private String createdAt;

	@OneToOne(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonManagedReference
	private ResponseModel response;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@JsonBackReference
	private ProductModel productModel;

}
