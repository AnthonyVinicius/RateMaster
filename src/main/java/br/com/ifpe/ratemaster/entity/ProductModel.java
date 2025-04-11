package br.com.ifpe.ratemaster;
import br.com.ifpe.ratemaster.shop.ShopModel;
import jakarta.persistence.*;
import java.util.List;

@Entity
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
	@JoinColumn(name = "type_id")
	private TypeModel typeModel;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private CategoryModel categoryModel;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "shop_id")
	private ShopModel shop;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ReviewModel> reviewModels;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public BrandModel getBrand() {
		return brandModel;
	}

	public void setBrand(BrandModel brandModel) {
		this.brandModel = brandModel;
	}

	public TypeModel getType() {
		return typeModel;
	}

	public void setType(TypeModel typeModel) {
		this.typeModel = typeModel;
	}

	public CategoryModel getCategory() {
		return categoryModel;
	}

	public void setCategory(CategoryModel categoryModel) {
		this.categoryModel = categoryModel;
	}

	public ShopModel getShop() {
		return shop;
	}

	public void setShop(ShopModel shop) {
		this.shop = shop;
	}

	public List<ReviewModel> getReviews() {
		return reviewModels;
	}

	public void setReviews(List<ReviewModel> reviewModels) {
		this.reviewModels = reviewModels;
	}
}
