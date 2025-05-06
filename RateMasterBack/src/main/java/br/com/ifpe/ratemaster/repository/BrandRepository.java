package br.com.ifpe.ratemaster.repository;

import br.com.ifpe.ratemaster.entity.BrandModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<BrandModel, Long> {
}
