package br.com.ifpe.ratemaster.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "tb_products")
public class ProductModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;
	private String description;
	private Double price;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "brand_id")
	private BrandModel brandModel;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "shop_id")
	private ShopModel shopModel;

	@OneToMany(mappedBy = "productModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ReviewModel> reviewModels;


}
