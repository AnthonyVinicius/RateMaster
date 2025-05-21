/*
package br.com.ifpe.ratemaster.service;

import br.com.ifpe.ratemaster.entity.ReviewModel;
import br.com.ifpe.ratemaster.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<ReviewModel> listAllReviews() {return reviewRepository.findAll();}

    public ReviewModel saveReview(ReviewModel reviewModel) {return reviewRepository.save(reviewModel);}

    public Optional<ReviewModel> findReviewById(Long id) {return reviewRepository.findById(id);}

    public void deleteReview(Long id) {reviewRepository.deleteById(id);}
}
 */