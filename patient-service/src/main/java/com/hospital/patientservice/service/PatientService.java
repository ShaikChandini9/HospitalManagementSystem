package com.hospital.patientservice.service;

import com.hospital.patientservice.request.PatientRequest;
import com.hospital.patientservice.response.PatientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {
    PatientResponse createPatient(PatientRequest request);
    PatientResponse getPatientById(Long id);
    Page<PatientResponse> getAllPatients(Pageable pageable);
    PatientResponse updatePatient(Long id, PatientRequest request);
    void deletePatient(Long id);
}
