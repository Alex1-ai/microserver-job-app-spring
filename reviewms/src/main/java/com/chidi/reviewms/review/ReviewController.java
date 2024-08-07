package com.chidi.reviewms.review;


import com.chidi.reviewms.messaging.ReviewMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private ReviewService reviewService;
    private ReviewMessageProducer reviewMessageProducer;

    @Autowired
    public ReviewController(
            ReviewService reviewService,
            ReviewMessageProducer reviewMessageProducer
            ) {

        this.reviewService = reviewService;
        this.reviewMessageProducer = reviewMessageProducer;
    }


    @GetMapping("/averageRating")
    public Double getAverageReview(@RequestParam Long companyId){
        List<Review> reviewList = reviewService.getAllReviews(companyId);

        return reviewList.stream()
                .mapToDouble(Review::getRating)
                .average().orElse(0.0);

    }


    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId){
        return new ResponseEntity<>(reviewService.getAllReviews(companyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestParam Long companyId, @RequestBody Review review){
        Review savedReview = reviewService.addReview(companyId, review);

        if(savedReview != null){
            reviewMessageProducer.sendMessage(review);
            return new ResponseEntity<>(savedReview, HttpStatus.CREATED);



        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(
                                            @PathVariable Long reviewId){
        Review review = reviewService.getReview( reviewId);

        if(review != null) {
            return new ResponseEntity<>(review, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }



    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(
                                               @PathVariable Long reviewId,
                                               @RequestBody Review review){

        Boolean isUpdated  = reviewService.updateReview(reviewId, review);
        if(isUpdated){
            return new ResponseEntity<>("Review updated successfully", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Review not updated", HttpStatus.NOT_FOUND);
        }

    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId
    ){
        Boolean isDeleted  = reviewService.deleteReview(reviewId);
        if(isDeleted){
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Review not deleted", HttpStatus.NOT_FOUND);
        }

    }



}
