package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.dto.AuthenticationDTO;
import br.com.ifpe.ratemaster.dto.LoginResponseDTO;
import br.com.ifpe.ratemaster.dto.RegisterDTO;
import br.com.ifpe.ratemaster.entity.UserModel;
import br.com.ifpe.ratemaster.repository.UserRepository;
import br.com.ifpe.ratemaster.infra.security.TokenService;
import br.com.ifpe.ratemaster.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if(this.userRepository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel newUserModel = new UserModel(data.name(),data.email(), encryptedPassword, data.role());

        this.userRepository.save(newUserModel);

        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserModel> findUserById(@PathVariable String id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable String id, @RequestBody UserModel userModel){
        return userService.findUserById(id)
                .map(newUser ->{
                    newUser.setName(userModel.getName());

                    UserModel updateUser= userService.saveUser(newUser);
                    return ResponseEntity.ok(updateUser);
                }).orElse(ResponseEntity.notFound().build());
    }
}
