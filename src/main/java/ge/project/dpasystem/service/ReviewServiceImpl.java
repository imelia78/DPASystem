package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.ReviewDto;
import ge.project.dpasystem.mapper.ReviewMapper;
import ge.project.dpasystem.model.AppointmentStatus;
import ge.project.dpasystem.model.Review;
import ge.project.dpasystem.repository.AppointmentRepository;
import ge.project.dpasystem.repository.ClientRepository;
import ge.project.dpasystem.repository.DoctorRepository;
import ge.project.dpasystem.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final ClientRepository clientRepository;
    private final ReviewMapper reviewMapper;




    //Логика по доступу к написанию отзыва
    // у appointment должен быть статус COMPLETED

    @Override
    public List<ReviewDto> findAllReviews(RequestFilter filter) {
        int pageSize = filter.pageSize() != null
                ? filter.pageSize() : 10; //todo default size in app.properties,  не хардкодить!
        int pageNumber = filter.pageNumber() != null
                ? filter.pageNumber() : 0; // first page
        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        return reviewRepository.findAllBy(pageable).stream().map(reviewMapper::toDto).toList();
    }

    @Override
    public List<ReviewDto> findReviewsByDoctorId(UUID id, RequestFilter filter) {
        int pageSize = filter.pageSize() != null
                ? filter.pageSize() : 10; //todo default size in app.properties,  не хардкодить!
        int pageNumber = filter.pageNumber() != null
                ? filter.pageNumber() : 0; // first page
        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        return reviewRepository.findAllByDoctorId(id,pageable).stream().map(reviewMapper::toDto).toList();
    }

    @Override
    public List<ReviewDto> findReviewsByClientId(UUID id, RequestFilter filter) {
        int pageSize = filter.pageSize() != null
                ? filter.pageSize() : 10; //todo default size in app.properties,  не хардкодить!
        int pageNumber = filter.pageNumber() != null
                ? filter.pageNumber() : 0; // first page
        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        return reviewRepository.findAllByClientId(id,pageable).stream().map(reviewMapper::toDto).toList();
    }

    @Override
    public  ReviewDto findReviewById(UUID id) {
        var review = reviewRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        log.info("review with id {} has been found", id);
        return reviewMapper.toDto(review);
    }


    @Transactional
    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {

        var appointment = appointmentRepository.findAppointmentById(reviewDto.appointmentId()).orElseThrow(EntityNotFoundException::new);
        log.info("particular appointment has been found");
        if (appointment.getAppointmentStatus() != AppointmentStatus.COMPLETED){
            throw new IllegalStateException("Access to the review will be provided only after completing appointment!");

        }
        log.info("Appointment status successfully checked");

        if(!appointment.getClient().getId().equals(reviewDto.clientId())|| !appointment.getDoctor().getId().equals(reviewDto.doctorId()) ){
            throw new IllegalStateException("Client or Doctor mismatch");
        }
        if(reviewRepository.existsByAppointment(appointment)){
            throw new IllegalStateException("Review already exists");
        }
        log.info("Validation passed for appointment {}", reviewDto.appointmentId());
        var doctor = appointment.getDoctor();

        var client = appointment.getClient();

        Review review = reviewMapper.toEntity(reviewDto,appointment,doctor,client);

       var saved = reviewRepository.save(review);
       log.info("Review successfully created");
        return reviewMapper.toDto(saved);

    }

    @Override
    public ReviewDto updateReview(UUID id,ReviewDto reviewDto) {
        var review = reviewRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        log.info("Updating review with id {}", id);
        reviewMapper.updateEntity(reviewDto,review);
        log.info("Review with id {} successfully updated", id);
        return reviewMapper.toDto(review);
    }

    @Override
    public ReviewDto updateReviewComment(UUID id, String comment) {
        var review = reviewRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        log.info("updating comment for review with id {}", id);
        review.setComment(comment);
        return reviewMapper.toDto(review);

    }

    @Override
    public ReviewDto updateReviewRating(UUID id, Double rating) {
        var review = reviewRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        log.info("updating rating for review with id {}", id);
        review.setRating(rating);
        return reviewMapper.toDto(review);
    }


    @Override
    public void deleteReviewById(UUID id) {
        var review = reviewRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        log.info("deleting review with id {}",id);
        reviewRepository.deleteById(id);
        log.info("Review  with id {} has been deleted successfully", id);

    }


    //Логика по доступу к написанию отзыва
    // у appointment должен быть статус COMPLETED

}
