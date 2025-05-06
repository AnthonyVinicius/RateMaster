package br.com.ifpe.ratemaster.repository;

import br.com.ifpe.ratemaster.entity.ShopModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<ShopModel, Long> {}
