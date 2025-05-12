package br.com.ifpe.ratemaster.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_products")
public class ProductModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;
	private String description;
	private Double price;
	private String image;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "brand_id")
	private BrandModel brandModel;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "shop_id")
	private ShopModel shopModel;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private CategoryModel categoryModel;

	@OneToMany(mappedBy = "productModel", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<ReviewModel> reviewModels;

	public ProductModel(BrandModel brandModel, String description, long id, String name, Double price, List<ReviewModel> reviewModels, ShopModel shopModel, CategoryModel categoryModel, String image) {
		this.brandModel = brandModel;
		this.description = description;
		this.id = id;
		this.name = name;
		this.price = price;
		this.reviewModels = reviewModels;
		this.shopModel = shopModel;
		this.categoryModel = categoryModel;
		this.image = image;
	}

	public ProductModel() {
	}

	public BrandModel getBrandModel() {
		return brandModel;
	}

	public void setBrandModel(BrandModel brandModel) {
		this.brandModel = brandModel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public List<ReviewModel> getReviewModels() {
		return reviewModels;
	}

	public void setReviewModels(List<ReviewModel> reviewModels) {
		this.reviewModels = reviewModels;
	}

	public ShopModel getShopModel() {
		return shopModel;
	}

	public void setShopModel(ShopModel shopModel) {
		this.shopModel = shopModel;
	}

	public CategoryModel getCategoryModel() {return categoryModel;}

	public void setCategoryModel(CategoryModel categoryModel) {this.categoryModel = categoryModel;}

	public String getImage() {return image;}

	public void setImage(String image) {this.image = image;}
}
