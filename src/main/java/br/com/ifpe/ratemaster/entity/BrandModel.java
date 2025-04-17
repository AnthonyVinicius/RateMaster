package br.com.ifpe.ratemaster.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "tb_brand")
public class BrandModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(mappedBy = "brandModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductModel> productModels;
}
