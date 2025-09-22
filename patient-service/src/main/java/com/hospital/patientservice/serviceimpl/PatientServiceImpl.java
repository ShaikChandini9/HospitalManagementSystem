package com.hospital.patientservice.serviceimpl;

import com.hospital.patientservice.entity.Patient;
import com.hospital.patientservice.repository.PatientRepository;
import com.hospital.patientservice.request.PatientRequest;
import com.hospital.patientservice.response.PatientResponse;
import com.hospital.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        Patient patient = mapRequestToEntity(request);
        return mapEntityToResponse(patientRepository.save(patient));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "patients", key = "#id")
    public PatientResponse getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(this::mapEntityToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientResponse> getAllPatients(Pageable pageable) {
        return patientRepository.findByActiveTrue(pageable)
                .map(this::mapEntityToResponse);
    }

    @Override
    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        existing.setFullName(request.getFullName());
        existing.setEmail(request.getEmail());
        existing.setDateOfBirth(request.getDateOfBirth());
        existing.setPhoneNumber(request.getPhoneNumber());
        existing.setAddress(request.getAddress());

        return mapEntityToResponse(patientRepository.save(existing));
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        patient.setActive(false); // soft delete
        patientRepository.save(patient);
    }

    private Patient mapRequestToEntity(PatientRequest request) {
        return Patient.builder()
                .fullName(request.getFullName())
                .patientIdNumber(request.getPatientIdNumber())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .active(true)
                .build();
    }

    private PatientResponse mapEntityToResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .patientIdNumber(patient.getPatientIdNumber())
                .email(patient.getEmail())
                .dateOfBirth(patient.getDateOfBirth())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
}
