package br.com.ifpe.ratemaster.repository;

import br.com.ifpe.ratemaster.entity.ResponseModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<ResponseModel, Long> {
}
