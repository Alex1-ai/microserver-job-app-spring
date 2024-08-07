package com.chidi.reviewms.review.impl;


import com.chidi.reviewms.review.Review;
import com.chidi.reviewms.review.ReviewRepository;
import com.chidi.reviewms.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
   @Autowired
    private ReviewRepository reviewRepository;


    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public Review addReview(Long companyId, Review review) {
        if (companyId != null){
            review.setCompanyId(companyId);
            return reviewRepository.save(review);

        }
        return null;
    }

    @Override
    public Review getReview( Long reviewId) {
       return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public Boolean updateReview( Long reviewId, Review updatedReview) {
//        System.out.println(companyService.getCompanyById(companyId));
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(review != null){
            review.setTitle(updatedReview.getTitle());
            review.setDescription(updatedReview.getDescription());
            review.setRating(updatedReview.getRating());
            review.setCompanyId(updatedReview.getCompanyId());
            System.out.println(updatedReview);
            reviewRepository.save(review);
            return true;

        }
        return null;
    }

    @Override
    public Boolean deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(review != null
        ){
           reviewRepository.delete(review);

//            reviewRepository.deleteById(reviewId);

            return true;




        }else{
            return false;

        }

    }
}
