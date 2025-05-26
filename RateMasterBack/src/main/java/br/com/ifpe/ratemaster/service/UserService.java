package br.com.ifpe.ratemaster.service;

import br.com.ifpe.ratemaster.entity.BrandModel;
import br.com.ifpe.ratemaster.entity.ProductModel;
import br.com.ifpe.ratemaster.entity.UserModel;
import br.com.ifpe.ratemaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username);
    }
    public List<UserModel> listAllUsers() {return repository.findAll();}

    public UserModel saveUser(UserModel userModel) {return repository.save(userModel);}

    public Optional<UserModel> findUserById(String id) {return repository.findById(id);}
}
