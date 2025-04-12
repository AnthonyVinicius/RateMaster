package br.com.ifpe.ratemaster.repository;

import br.com.ifpe.ratemaster.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
}
