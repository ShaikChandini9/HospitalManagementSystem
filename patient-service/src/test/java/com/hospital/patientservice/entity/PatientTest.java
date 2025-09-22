package com.hospital.patientservice.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PatientTest {

    @Test
    public void testBuilderAndGetters() {
        LocalDate now = LocalDate.now();
        Patient patient = Patient.builder()
                .id(1L)
                .fullName("John Doe")
                .patientIdNumber("PID123")
                .email("john@example.com")
                .dateOfBirth(now)
                .phoneNumber("123456789")
                .address("123 Main St")
                .active(true)
                .build();

        assertEquals(1L, patient.getId());
        assertEquals("John Doe", patient.getFullName());
        assertEquals("PID123", patient.getPatientIdNumber());
        assertEquals("john@example.com", patient.getEmail());
        assertEquals(now, patient.getDateOfBirth());
        assertEquals("123456789", patient.getPhoneNumber());
        assertEquals("123 Main St", patient.getAddress());
        assertTrue(patient.isActive());
    }

    @Test
    public void testPrePersistSetsTimestamps() {
        Patient patient = new Patient();
        patient.onCreate();

        assertNotNull(patient.getCreatedAt());
        assertNotNull(patient.getUpdatedAt());
        assertEquals(patient.getCreatedAt(), patient.getUpdatedAt());
    }

    @Test
    public void testPreUpdateSetsUpdatedAt() throws InterruptedException {
        Patient patient = new Patient();
        patient.onCreate();
        LocalDateTime createdAt = patient.getCreatedAt();

        // simulate some delay
        Thread.sleep(10);
        patient.onUpdate();

        assertNotNull(patient.getUpdatedAt());
        assertTrue(patient.getUpdatedAt().isAfter(createdAt));
    }

    @Test
    public void testDefaultActiveValue() {
        Patient patient = new Patient();
        assertTrue(patient.isActive());
    }

    @Test
    public void testSetters() {
        Patient patient = new Patient();
        patient.setFullName("Alice");
        patient.setPatientIdNumber("PID456");
        patient.setEmail("alice@example.com");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setPhoneNumber("987654321");
        patient.setAddress("456 Main St");
        patient.setActive(false);

        assertEquals("Alice", patient.getFullName());
        assertEquals("PID456", patient.getPatientIdNumber());
        assertEquals("alice@example.com", patient.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), patient.getDateOfBirth());
        assertEquals("987654321", patient.getPhoneNumber());
        assertEquals("456 Main St", patient.getAddress());
        assertFalse(patient.isActive());
    }
}
