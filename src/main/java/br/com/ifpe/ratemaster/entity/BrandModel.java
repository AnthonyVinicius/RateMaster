package br.com.ifpe.ratemaster;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class BrandModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductModel> productModels;

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

    public List<ProductModel> getProducts() {
        return productModels;
    }

    public void setProducts(List<ProductModel> productModels) {
        this.productModels = productModels;
    }
}
