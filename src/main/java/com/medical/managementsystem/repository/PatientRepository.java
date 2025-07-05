package com.medical.managementsystem.repository;

import com.medical.managementsystem.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository  extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);
}
