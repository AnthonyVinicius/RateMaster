package br.com.ifpe.ratemaster.entity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_brand")
public class BrandModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(mappedBy = "brandModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductModel> productModels;

    public BrandModel(long id, String name, List<ProductModel> productModels) {
        this.id = id;
        this.name = name;
        this.productModels = productModels;
    }

    public BrandModel() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ProductModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(List<ProductModel> productModels) {
        this.productModels = productModels;
    }

    public void setName(String name) {
        this.name = name;
    }
}
