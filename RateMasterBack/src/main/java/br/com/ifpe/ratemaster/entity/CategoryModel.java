package br.com.ifpe.ratemaster.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_category")
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;

    public CategoryModel() {
    }

    public CategoryModel(long id, String name, List<ProductModel> productModels) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
