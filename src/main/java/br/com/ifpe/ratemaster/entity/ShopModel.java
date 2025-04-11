package br.com.ifpe.ratemaster.shop;
import br.com.ifpe.ratemaster.ProductModel;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_shop")
public class ShopModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long Id;
    private String email;
    private String name;
    private String userType;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<ProductModel> products;

    public ShopModel() {
    }

    public ShopModel(String email, Long id, String name, List<ProductModel> products, String userType) {
        this.email = email;
        Id = id;
        this.name = name;
        this.products = products;
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
}
