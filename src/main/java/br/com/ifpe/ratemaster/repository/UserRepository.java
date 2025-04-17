package br.com.ifpe.ratemaster.repository;

import br.com.ifpe.ratemaster.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, Long> {
}
