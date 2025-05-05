package br.com.ifpe.ratemaster.repository;

import br.com.ifpe.ratemaster.entity.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
}
