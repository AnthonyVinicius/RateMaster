package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.dto.ReviewDTO;
import br.com.ifpe.ratemaster.entity.ProductModel;
import br.com.ifpe.ratemaster.entity.ReviewModel;
import br.com.ifpe.ratemaster.service.ProductService;
import br.com.ifpe.ratemaster.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

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

    @PostMapping("/register")
    private ReviewModel registerReview(@RequestBody ReviewDTO dto){
        ProductModel product = productService.findProductById(dto.productId).orElseThrow();
        ReviewModel review = new ReviewModel();
        review.setName(dto.name);
        review.setComment(dto.comment);
        review.setRating(dto.rating);
        review.setProductModel(product);
        review.setCreatedAt(dto.createdAt);
        return reviewService.saveReview(review);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Void> deleteReviewById(@PathVariable Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}