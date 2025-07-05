package com.medical.managementsystem.controller;

import com.medical.managementsystem.request.PatientRequest;
import com.medical.managementsystem.response.PatientResponse;
import com.medical.managementsystem.service.PatientService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/register")
    public ResponseEntity<PatientResponse> registerPatient(@RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.registerPatient(request));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<PatientResponse> getPatientsById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PutMapping("/update-details/{id}")
    public ResponseEntity<PatientResponse> updatePatientDetails(@PathVariable Long id, @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePatientDetails(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
