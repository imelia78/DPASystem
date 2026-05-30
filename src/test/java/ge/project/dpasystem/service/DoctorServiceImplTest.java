package ge.project.dpasystem.service;

import ge.project.dpasystem.controller.RequestFilter;
import ge.project.dpasystem.dto.DoctorDto;
import ge.project.dpasystem.mapper.DoctorMapper;
import ge.project.dpasystem.model.Doctor;
import ge.project.dpasystem.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllDoctorsByPages() {
        RequestFilter filter = mock(RequestFilter.class);
        when(filter.pageSize()).thenReturn(10);
        when(filter.pageNumber()).thenReturn(0);

        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Doctor doctor = new Doctor();
        Page<Doctor> page = new PageImpl<>(Collections.singletonList(doctor));
        when(doctorRepository.findAllBy(pageable)).thenReturn(page);
        DoctorDto doctorDto = mock(DoctorDto.class);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        List<DoctorDto> result = doctorService.findAllDoctorsByPages(filter);

        assertEquals(1, result.size());
        verify(doctorRepository, times(1)).findAllBy(pageable);
        verify(doctorMapper, times(1)).toDto(doctor);
    }

    @Test
    void testFindDoctorById() {
        UUID id = UUID.randomUUID();
        Doctor doctor = new Doctor();
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        DoctorDto doctorDto = mock(DoctorDto.class);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        DoctorDto result = doctorService.findDoctorById(id);

        assertNotNull(result);
        verify(doctorRepository, times(1)).findById(id);
        verify(doctorMapper, times(1)).toDto(doctor);
    }

    @Test
    void testCreateDoctor() {
        DoctorDto doctorDto = mock(DoctorDto.class);
        Doctor doctor = new Doctor();
        when(doctorMapper.toEntity(doctorDto)).thenReturn(doctor);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        DoctorDto result = doctorService.createDoctor(doctorDto);

        assertNotNull(result);
        verify(doctorMapper, times(1)).toEntity(doctorDto);
        verify(doctorRepository, times(1)).save(doctor);
        verify(doctorMapper, times(1)).toDto(doctor);
    }

    @Test
    void testUpdateDoctor() {
        UUID id = UUID.randomUUID();
        DoctorDto doctorDto = mock(DoctorDto.class);
        when(doctorDto.id()).thenReturn(id);
        Doctor doctor = new Doctor();
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        DoctorDto result = doctorService.updateDoctor(doctorDto);

        assertNotNull(result);
        verify(doctorRepository, times(1)).findById(id);
    }

    @Test
    void testFindDoctorsByFirstNameAndLastName() {
        String firstName = "John";
        String lastName = "Doe";
        Doctor doctor = new Doctor();
        DoctorDto doctorDto = mock(DoctorDto.class);
        when(doctorRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(Collections.singletonList(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        List<DoctorDto> result = doctorService.findDoctorsByFirstNameAndLastName(firstName, lastName);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(doctorRepository, times(1)).findByFirstNameAndLastName(firstName, lastName);
    }

    @Test
    void testFindDoctorByEmail() {
        String email = "test@example.com";
        Doctor doctor = new Doctor();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        DoctorDto doctorDto = mock(DoctorDto.class);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        DoctorDto result = doctorService.findDoctorByEmail(email);

        assertNotNull(result);
        verify(doctorRepository, times(1)).findByEmail(email);
        verify(doctorMapper, times(1)).toDto(doctor);
    }

    @Test
    void testUpdateEmail() {
        UUID id = UUID.randomUUID();
        String newEmail = "new@example.com";
        Doctor doctor = new Doctor();
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByEmail(newEmail)).thenReturn(false);

        DoctorDto doctorDto = mock(DoctorDto.class);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        DoctorDto result = doctorService.updateEmail(id, newEmail);

        assertEquals(doctorDto, result);
        assertEquals(newEmail, doctor.getEmail());
    }

    @Test
    void testUpdatePhoneNumber() {
        UUID id = UUID.randomUUID();
        String newPhoneNumber = "1234567890";
        Doctor doctor = new Doctor();
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByPhoneNumber(newPhoneNumber)).thenReturn(false);

        DoctorDto doctorDto = mock(DoctorDto.class);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        DoctorDto result = doctorService.updatePhoneNumber(id, newPhoneNumber);

        assertEquals(doctorDto, result);
        assertEquals(newPhoneNumber, doctor.getPhoneNumber());
    }

    @Test
    void testDeleteByEmail() {
        String email = "test@example.com";
        UUID id = UUID.randomUUID();
        Doctor doctor = new Doctor();
        doctor.setId(id);
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));

        doctorService.deleteByEmail(email);

        verify(doctorRepository, times(1)).findByEmail(email);
        verify(doctorRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();
        Doctor doctor = new Doctor();
        doctor.setId(id);
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));

        doctorService.deleteById(id);

        verify(doctorRepository, times(1)).findById(id);
        verify(doctorRepository, times(1)).deleteById(id);
    }
}
