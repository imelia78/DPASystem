package ge.project.dpasystem.mapper;

import ge.project.dpasystem.dto.ReviewDto;
import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.Client;
import ge.project.dpasystem.model.Doctor;
import ge.project.dpasystem.model.Review;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewDto reviewDto,
                           Appointment appointment,
                           Doctor doctor,
                           Client client) {

        return Review.builder()
                .rating(reviewDto.rating())
                .comment(reviewDto.comment())
                .appointment(appointment)
                .doctor(doctor)
                .client(client)
                .build();

    }

    public ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .comment(review.getComment())
                .rating(review.getRating())
                .appointmentId(review.getAppointment().getId())
                .doctorId(review.getDoctor().getId())
                .clientId(review.getClient().getId())
                .createdAt(review.getCreatedAt())
                .build();
    }
    public void updateEntity(ReviewDto reviewDto, Review review) {
        review.setComment(reviewDto.comment());
        review.setRating(reviewDto.rating());
    }
}
