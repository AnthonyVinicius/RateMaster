package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.dto.ResponseDTO;
import br.com.ifpe.ratemaster.entity.ResponseModel;
import br.com.ifpe.ratemaster.entity.ReviewModel;
import br.com.ifpe.ratemaster.entity.UserModel;
import br.com.ifpe.ratemaster.service.ResponseService;
import br.com.ifpe.ratemaster.service.ReviewService;
import br.com.ifpe.ratemaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/response")
public class ResponseController {

    @Autowired
    private ResponseService responseService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<ResponseDTO> getAllResponses() {
        return responseService.getAllResponses()
                .stream()
                .map(ResponseDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> insertResponse(@RequestBody ResponseDTO dto) {
        ReviewModel review = reviewService.findReviewById(dto.getReviewId())
                .orElseThrow(() -> new RuntimeException("Review não encontrada"));
        UserModel user = userService.findUserById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        ResponseModel response = new ResponseModel();
        response.setComment(dto.getComment());
        response.setCreatedAt(dto.getCreatedAt());
        response.setUserModel(user);
        response.setName(dto.getName());
        response.setReview(review);

        ResponseModel saved = responseService.saveResponse(response);
        return ResponseEntity.ok(new ResponseDTO(saved));
    }
}
