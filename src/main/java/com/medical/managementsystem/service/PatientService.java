package com.medical.managementsystem.service;

import com.medical.managementsystem.request.PatientRequest;
import com.medical.managementsystem.response.PatientResponse;

import java.util.List;

public interface PatientService {

    PatientResponse registerPatient(PatientRequest request);
    List<PatientResponse> getAllPatients();
    PatientResponse getPatientById(Long id);
    PatientResponse updatePatient(Long id, PatientRequest request);
    void deletePatient(Long id);
}
