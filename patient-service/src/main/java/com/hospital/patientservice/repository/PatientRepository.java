package com.hospital.patientservice.repository;

import com.hospital.patientservice.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPatientIdNumber(String patientIdNumber);
    Page<Patient> findByActiveTrue(Pageable pageable);
}
