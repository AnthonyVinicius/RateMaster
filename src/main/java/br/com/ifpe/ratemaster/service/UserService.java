package br.com.ifpe.ratemaster.service;

import br.com.ifpe.ratemaster.entity.UserModel;
import br.com.ifpe.ratemaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserModel> listAll() {return userRepository.findAll();}

    public UserModel saveUser(UserModel usermodel) {return userRepository.save(usermodel);}

    public Optional<UserModel> findById(Long id) {return userRepository.findById(id);}

    public void delete(Long id) {userRepository.deleteById(id);}
}
