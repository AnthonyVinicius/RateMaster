package br.com.ifpe.ratemaster.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_shop")
public class ShopModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String email;
    private String name;
    private String userType;

    @OneToMany(mappedBy = "shopModel", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ProductModel> productsModel;


    public ShopModel(String userType, List<ProductModel> productsModel, String name, Long id, String email) {
        this.userType = userType;
        this.productsModel = productsModel;
        this.name = name;
        Id = id;
        this.email = email;
    }

    public ShopModel() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductModel> getProductsModel() {
        return productsModel;
    }

    public void setProductsModel(List<ProductModel> productsModel) {
        this.productsModel = productsModel;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
