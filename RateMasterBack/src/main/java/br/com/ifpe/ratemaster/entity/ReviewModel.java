
package br.com.ifpe.ratemaster.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
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

	public ReviewModel(String comment, Long id, String name, ProductModel productModel, Integer rating, String createdAt, ResponseModel response) {
		this.comment = comment;
		this.id = id;
		this.name = name;
		this.productModel = productModel;
		this.rating = rating;
		this.createdAt = createdAt;
		this.response = response;
	}

	public ReviewModel() {

	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductModel getProductModel() {
		return productModel;
	}

	public void setProductModel(ProductModel productModel) {
		this.productModel = productModel;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {this.rating = rating;}

	public String getCreatedAt() {return createdAt;}

	public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}

	public ResponseModel getResponse() {
		return response;
	}

	public void setResponse(ResponseModel response) {
		this.response = response;
	}
}
