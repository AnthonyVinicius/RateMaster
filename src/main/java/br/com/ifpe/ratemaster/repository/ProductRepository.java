package br.com.ifpe.ratemaster.repository;

import br.com.ifpe.ratemaster.entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {
}
