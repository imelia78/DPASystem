package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.ReviewDto;
import ge.project.dpasystem.model.Review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewService {


    List<ReviewDto> findAllReviews(RequestFilter filter);

    List<ReviewDto> findReviewsByDoctorId(UUID id, RequestFilter filter);

    List<ReviewDto> findReviewsByClientId(UUID id, RequestFilter filter);

    ReviewDto findReviewById(UUID id);

    ReviewDto createReview(ReviewDto reviewDto);

    ReviewDto updateReview(UUID id, ReviewDto reviewDto);

    ReviewDto updateReviewComment(UUID id, String comment);

    ReviewDto updateReviewRating(UUID id, Double rating);

    void deleteReviewById(UUID id);


}
