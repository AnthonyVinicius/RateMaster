package br.com.ifpe.ratemaster.service;

import br.com.ifpe.ratemaster.entity.UserModel;
import br.com.ifpe.ratemaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public List<UserModel> listAll(){
        return repository.findAll();
    }

    public Optional<UserModel> findById(Long id){
        return repository.findById(id);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }
}
