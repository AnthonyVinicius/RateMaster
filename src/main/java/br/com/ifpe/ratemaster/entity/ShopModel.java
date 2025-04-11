package br.com.ifpe.ratemaster.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tb_shop")
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long Id;
    private String email;
    private String name;
    private String userType;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<ProductModel> products;

}
