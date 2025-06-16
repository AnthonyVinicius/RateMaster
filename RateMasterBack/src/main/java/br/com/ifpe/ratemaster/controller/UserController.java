package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.dto.*;
import br.com.ifpe.ratemaster.entity.UserModel;
import br.com.ifpe.ratemaster.repository.UserRepository;
import br.com.ifpe.ratemaster.infra.security.TokenService;
import br.com.ifpe.ratemaster.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        UserModel user = (UserModel) userRepository.findByEmail(userEmail);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponseDTO userResponse = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getBackGroundColor(),
                user.getImage(),
                user.getRole()
        );

        return ResponseEntity.ok(userResponse);
    }

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
    public ResponseEntity<UserModel> updateUser(@PathVariable String id, @RequestBody UserUpdateDTO dto) {
        return userService.findUserById(id)
                .map(existingUser -> {
                    if (dto.name() != null) existingUser.setName(dto.name());
                    if (dto.backGroundColor() != null) existingUser.setBackGroundColor(dto.backGroundColor());
                    if (dto.image() != null) existingUser.setImage(dto.image());

                    UserModel updatedUser = userService.saveUser(existingUser);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<UserModel> listAllUsers(){
        return userService.listAllUsers();
    }
}
