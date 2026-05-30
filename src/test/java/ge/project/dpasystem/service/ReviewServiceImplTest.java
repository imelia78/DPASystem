package ge.project.dpasystem.service;

import ge.project.dpasystem.dto.ReviewDto;
import ge.project.dpasystem.mapper.ReviewMapper;
import ge.project.dpasystem.model.Appointment;
import ge.project.dpasystem.model.AppointmentStatus;
import ge.project.dpasystem.model.Client;
import ge.project.dpasystem.model.Doctor;
import ge.project.dpasystem.model.Review;
import ge.project.dpasystem.repository.AppointmentRepository;
import ge.project.dpasystem.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private AppointmentRepository appointmentRepository;

  @Mock
  private ReviewMapper reviewMapper;

  @InjectMocks
  private ReviewServiceImpl reviewService;

  private UUID reviewId;
  private UUID appointmentId;

  private Review review;
  private ReviewDto reviewDto;
  private Appointment appointment;
  private Doctor doctor;
  private Client client;

  @BeforeEach
  void setUp() {
    reviewId = UUID.randomUUID();
    appointmentId = UUID.randomUUID();
    UUID clientId = UUID.randomUUID();

    doctor = new Doctor();
    UUID doctorId = UUID.randomUUID();
    doctor.setId(doctorId);

    client = new Client();
    client.setId(clientId);

    appointment = new Appointment();
    appointment.setId(appointmentId);
    appointment.setDoctor(doctor);
    appointment.setClient(client);
    appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);

    review = new Review();
    review.setId(reviewId);
    review.setComment("Good doctor");
    review.setRating(5.0);

    reviewDto = mock(ReviewDto.class);

    lenient().when(reviewDto.appointmentId()).thenReturn(appointmentId);
    lenient().when(reviewDto.doctorId()).thenReturn(doctorId);
    lenient().when(reviewDto.clientId()).thenReturn(clientId);
  }

  @Test
  void findReviewById_ShouldReturnReviewDto() {
    when(reviewRepository.findById(reviewId))
        .thenReturn(Optional.of(review));

    when(reviewMapper.toDto(review))
        .thenReturn(reviewDto);

    ReviewDto result = reviewService.findReviewById(reviewId);

    assertNotNull(result);
    verify(reviewRepository).findById(reviewId);
    verify(reviewMapper).toDto(review);
  }

  @Test
  void findReviewById_ShouldThrowException_WhenReviewNotFound() {
    when(reviewRepository.findById(reviewId))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> reviewService.findReviewById(reviewId)
    );
  }

  @Test
  void createReview_ShouldCreateReviewSuccessfully() {

    when(appointmentRepository.findAppointmentById(appointmentId))
        .thenReturn(Optional.of(appointment));

    when(reviewRepository.existsByAppointment(appointment))
        .thenReturn(false);

    when(reviewMapper.toEntity(
        reviewDto,
        appointment,
        doctor,
        client))
        .thenReturn(review);

    when(reviewRepository.save(review))
        .thenReturn(review);

    when(reviewMapper.toDto(review))
        .thenReturn(reviewDto);

    ReviewDto result = reviewService.createReview(reviewDto);

    assertNotNull(result);

    verify(reviewRepository).save(review);
    verify(reviewMapper).toDto(review);
  }

  @Test
  void createReview_ShouldThrowException_WhenAppointmentNotCompleted() {

    appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);

    when(appointmentRepository.findAppointmentById(appointmentId))
        .thenReturn(Optional.of(appointment));

    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> reviewService.createReview(reviewDto)
    );

    assertEquals(
        "Access to the review will be provided only after completing appointment!",
        exception.getMessage()
    );
  }

  @Test
  void createReview_ShouldThrowException_WhenDoctorOrClientMismatch() {

    UUID anotherDoctorId = UUID.randomUUID();

    when(reviewDto.doctorId())
        .thenReturn(anotherDoctorId);

    when(appointmentRepository.findAppointmentById(appointmentId))
        .thenReturn(Optional.of(appointment));

    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> reviewService.createReview(reviewDto)
    );

    assertEquals(
        "Client or Doctor mismatch",
        exception.getMessage()
    );
  }

  @Test
  void createReview_ShouldThrowException_WhenReviewAlreadyExists() {

    when(appointmentRepository.findAppointmentById(appointmentId))
        .thenReturn(Optional.of(appointment));

    when(reviewRepository.existsByAppointment(appointment))
        .thenReturn(true);

    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> reviewService.createReview(reviewDto)
    );

    assertEquals(
        "Review already exists",
        exception.getMessage()
    );
  }

  @Test
  void updateReview_ShouldUpdateReview() {

    when(reviewRepository.findById(reviewId))
        .thenReturn(Optional.of(review));

    when(reviewMapper.toDto(review))
        .thenReturn(reviewDto);

    ReviewDto result = reviewService.updateReview(reviewId, reviewDto);

    assertNotNull(result);

    verify(reviewMapper).updateEntity(reviewDto, review);
    verify(reviewMapper).toDto(review);
  }

  @Test
  void updateReviewComment_ShouldUpdateComment() {

    when(reviewRepository.findById(reviewId))
        .thenReturn(Optional.of(review));

    when(reviewMapper.toDto(review))
        .thenReturn(reviewDto);

    ReviewDto result =
        reviewService.updateReviewComment(reviewId, "Updated comment");

    assertNotNull(result);
    assertEquals("Updated comment", review.getComment());

    verify(reviewMapper).toDto(review);
  }

  @Test
  void updateReviewRating_ShouldUpdateRating() {

    when(reviewRepository.findById(reviewId))
        .thenReturn(Optional.of(review));

    when(reviewMapper.toDto(review))
        .thenReturn(reviewDto);

    ReviewDto result =
        reviewService.updateReviewRating(reviewId, 4.5);

    assertNotNull(result);
    assertEquals(4.5, review.getRating());

    verify(reviewMapper).toDto(review);
  }

  @Test
  void deleteReviewById_ShouldDeleteReview() {

    when(reviewRepository.findById(reviewId))
        .thenReturn(Optional.of(review));

    reviewService.deleteReviewById(reviewId);

    verify(reviewRepository).deleteById(reviewId);
  }

  @Test
  void deleteReviewById_ShouldThrowException_WhenReviewNotFound() {

    when(reviewRepository.findById(reviewId))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> reviewService.deleteReviewById(reviewId)
    );

    verify(reviewRepository, never()).deleteById(any());
  }
}