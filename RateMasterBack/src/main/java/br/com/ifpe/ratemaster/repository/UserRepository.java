package br.com.ifpe.ratemaster.repository;

import br.com.ifpe.ratemaster.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

    UserDetails findByEmail(String email);
}
