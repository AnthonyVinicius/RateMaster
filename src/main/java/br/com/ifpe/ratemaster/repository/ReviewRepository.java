package br.com.ifpe.ratemaster.repository;

import br.com.ifpe.ratemaster.entity.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewModel, Long> {
}
