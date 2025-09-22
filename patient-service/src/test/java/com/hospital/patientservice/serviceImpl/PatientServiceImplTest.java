package com.hospital.patientservice.serviceImpl;

import com.hospital.patientservice.entity.Patient;
import com.hospital.patientservice.repository.PatientRepository;
import com.hospital.patientservice.request.PatientRequest;
import com.hospital.patientservice.response.PatientResponse;
import com.hospital.patientservice.serviceimpl.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplTest {

    @InjectMocks
    PatientServiceImpl patientService;

    @Mock
    PatientRepository patientRepository;

    Patient patient = new Patient();
    PatientRequest patientRequest = new PatientRequest();
    PatientResponse patientResponse = new PatientResponse();

    @BeforeEach
    public void setUp() {

        this.patient = Patient.builder().fullName("Alis A").email("alis@gmail.com")
                .id(Long.valueOf(1)).address("3102 4th St Irving, Tx").patientIdNumber("1234567")
                .dateOfBirth(LocalDate.now()).phoneNumber("123456789")
                .createdAt(LocalDateTime.now()).active(true)
                .updatedAt(LocalDateTime.now()).build();

        this.patientRequest = PatientRequest.builder().patientIdNumber("1234567")
                .address("3102 4th St Irving, Tx").phoneNumber("123456789")
                .patientIdNumber("1234567")
                .email("alis@gmail.com").dateOfBirth(LocalDate.now())
                .fullName("Alis A").build();

        this.patientResponse = PatientResponse.builder()
                .address("3102 4th St Irving, Tx").id(1L).updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now()).dateOfBirth(LocalDate.now())
                .email("alis@gmail.com").patientIdNumber("1234567").id(1L)
                .fullName("Alis").phoneNumber("123456789").build();
    }

    @Test
    public void createPatientTest() {

        when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(patient);
        PatientResponse response = patientService.createPatient(patientRequest);
        assertNotNull(response);
    }

    @Test
    public void getPatientById_foundTest(){

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        PatientResponse response = patientService.getPatientById(1L);
        assertNotNull(response);
    }

    @Test
    public void testGetPatientById_NotFound() {
        when(patientRepository.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            patientService.getPatientById(2L);
        });
        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    public void testGetAllPatients_Success() {
        // Prepare pageable and mock page
        Pageable pageable = PageRequest.of(0, 10);
        Page<Patient> patientPage = new PageImpl<>(List.of(patient));

        when(patientRepository.findByActiveTrue(pageable)).thenReturn(patientPage);

        Page<PatientResponse> responsePage = patientService.getAllPatients(pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getContent().size());
        assertEquals(patient.getId(), responsePage.getContent().get(0).getId());
    }

    @Test
    public void testUpdatePatient_Success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(patient);
        PatientResponse response = patientService.updatePatient(1L, patientRequest);
        assertNotNull(response);
        assertEquals(patient.getId(), response.getId());
        assertEquals("Alis A", response.getFullName());
    }

    @Test
    public void testUpdatePatient_NotFound() {
        when(patientRepository.findById(2L)).thenReturn(Optional.empty());
        PatientRequest updateRequest = PatientRequest.builder().build();
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            patientService.updatePatient(2L, updateRequest);
        });
        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    public void testDeletePatient_Success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(patient);
        patientService.deletePatient(1L);
        assertFalse(patient.isActive()); // patient should be inactive now
    }

    @Test
    public void testDeletePatient_NotFound() {
        when(patientRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            patientService.deletePatient(2L);
        });
        assertEquals("Patient not found", exception.getMessage());
    }

}
