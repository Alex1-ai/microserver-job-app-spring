package com.chidi.reviewms.review;

import java.util.List;

public interface ReviewService {


    List<Review> getAllReviews(Long companyId);
    Review addReview(Long companyId, Review review);
    Review getReview( Long reviewId);
    Boolean updateReview( Long reviewId, Review review);

    Boolean deleteReview(Long reviewId);
}
