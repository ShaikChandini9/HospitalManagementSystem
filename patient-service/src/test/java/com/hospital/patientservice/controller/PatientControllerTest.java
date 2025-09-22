package com.hospital.patientservice.controller;

import com.hospital.patientservice.request.PatientRequest;
import com.hospital.patientservice.response.PatientResponse;
import com.hospital.patientservice.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    @InjectMocks
    PatientController patientController;

    @Mock
    PatientService patientService;

    @Mock
    Pageable pageable;

    @Mock
    Page<PatientResponse> patientResponses;

    PatientRequest patientRequest =new PatientRequest();
    PatientResponse patientResponse = new PatientResponse();



    @BeforeEach
    public void setUp(){

        patientRequest.setFullName("Alis");
        patientRequest.setPatientIdNumber("12334567");
        patientRequest.setAddress("3102 4th St Irving,TX");

        patientResponse.setFullName("Alis");
        patientResponse.setPatientIdNumber("1234567");
        patientResponse.setAddress("3102 4th St Irving,TX");

    }

    @Test
    public void createPatientTest(){

        when(patientService.createPatient(patientRequest)).thenReturn(patientResponse);
        ResponseEntity<PatientResponse> patientresponse=patientController.createPatient(patientRequest);
        assertNotNull(patientresponse);
    }

    @Test
    public void getPatientByIdTest(){

        when(patientService.getPatientById(123L)).thenReturn(patientResponse);
        ResponseEntity<PatientResponse> patientResponse=patientController.getPatientById(123L);
        assertNotNull(patientResponse);
    }

    @Test
    public void getAllPatientsTest(){

        when(patientService.getAllPatients(pageable)).thenReturn(patientResponses);
        ResponseEntity<Page<PatientResponse>> pageResponseEntity=patientController.getAllPatients(pageable);
        assertNotNull(pageResponseEntity);
    }

    @Test
    public void updatePatientTest(){

        when(patientService.updatePatient(123L,patientRequest)).thenReturn(patientResponse);
        ResponseEntity<PatientResponse> responseResponseEntity = patientController.updatePatient(123L, patientRequest);
        assertNotNull(responseResponseEntity);
    }

    @Test
    public void deletePatientTest(){

        patientService.deletePatient(123L);
        ResponseEntity<Void> response = patientController.deletePatient(123L);
        assertNotNull(response);
    }
}
