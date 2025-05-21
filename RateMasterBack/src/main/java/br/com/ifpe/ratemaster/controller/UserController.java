package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.dto.AuthenticationDTO;
import br.com.ifpe.ratemaster.dto.LoginResponseDTO;
import br.com.ifpe.ratemaster.dto.RegisterDTO;
import br.com.ifpe.ratemaster.entity.UserModel;
import br.com.ifpe.ratemaster.repository.UserRepository;
import br.com.ifpe.ratemaster.infra.security.TokenService;
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
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if(this.repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel newUserModel = new UserModel(data.name(),data.email(), encryptedPassword, data.role());

        this.repository.save(newUserModel);

        return ResponseEntity.ok().build();
    }
}
