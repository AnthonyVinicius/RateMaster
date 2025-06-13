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
    public List<ResponseModel> getAllResponses() {
        return responseService.getAllResponses();
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseModel> insertResponse(@RequestBody ResponseDTO dto) {
        ReviewModel review = reviewService.findReviewById(dto.reviewId).orElseThrow();
        UserModel user = userService.findUserById(dto.userId).orElseThrow();

        ResponseModel response = new ResponseModel();
        response.setComment(dto.comment);
        response.setCreatedAt(dto.createdAt);
        response.setUserModel(user);
        response.setName(dto.name);
        response.setReview(review);

        ResponseModel saved = responseService.saveResponse(response);
        return ResponseEntity.ok(saved);
    }
}