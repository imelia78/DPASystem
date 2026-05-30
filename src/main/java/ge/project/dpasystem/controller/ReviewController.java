package ge.project.dpasystem.controller;


import ge.project.dpasystem.dto.ReviewDto;
import ge.project.dpasystem.model.Review;
import ge.project.dpasystem.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "review_methods")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
    ) {
        var filter = new RequestFilter(pageSize, pageNumber);
        return ResponseEntity.ok(reviewService.findAllReviews(filter));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable UUID id){
        return ResponseEntity.ok(reviewService.findReviewById(id));
    }


    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<ReviewDto>> getReviewsByDoctorId(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
            , @PathVariable UUID id) {
        var filter = new RequestFilter(pageSize, pageNumber);
        return ResponseEntity.ok(reviewService.findReviewsByDoctorId(id, filter));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<ReviewDto>> getReviewsByClientId(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
            , @PathVariable UUID id
    ) {
        var filter = new RequestFilter(pageSize, pageNumber);
        return ResponseEntity.ok(reviewService.findReviewsByClientId(id, filter));
    }

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.createReview(reviewDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable UUID id, ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.updateReview(id, reviewDto));
    }

    @PatchMapping("/{id}/comment")
    public ResponseEntity<ReviewDto> updateReviewComment(@PathVariable UUID id, @RequestParam String newComment) {
        return ResponseEntity.ok(reviewService.updateReviewComment(id, newComment));
    }

    @PatchMapping("/{id}/rating")
    public ResponseEntity<ReviewDto> updateReviewRating(@PathVariable UUID id, @RequestParam Double newRating) {
        return ResponseEntity.ok(reviewService.updateReviewRating(id, newRating));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReviewById(@PathVariable UUID id){
        reviewService.deleteReviewById(id);
        return ResponseEntity.ok("Review has been deleted successfully!");
    }

}
