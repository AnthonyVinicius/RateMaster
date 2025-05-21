package br.com.ifpe.ratemaster.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_brand")
public class BrandModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;


    public BrandModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public BrandModel() {
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
}
