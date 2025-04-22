package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.entity.ReviewModel;
import br.com.ifpe.ratemaster.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping
    private List<ReviewModel> listAllReviews(){
        return reviewService.listAllReviews();
    }

    @GetMapping("/{id}")
    private ResponseEntity findReviewById(@PathVariable Long id){
        return reviewService.findReviewById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    private ReviewModel registerReview(@RequestBody ReviewModel reviewModel){
        return reviewService.saveReview(reviewModel);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteReviewById(@PathVariable Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
