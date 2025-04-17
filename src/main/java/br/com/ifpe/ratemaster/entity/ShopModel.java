package br.com.ifpe.ratemaster.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tb_shop")
@Data
public class ShopModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String email;
    private String name;
    private String userType;

    @OneToMany(mappedBy = "shopModel", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<ProductModel> productsModel;

}
