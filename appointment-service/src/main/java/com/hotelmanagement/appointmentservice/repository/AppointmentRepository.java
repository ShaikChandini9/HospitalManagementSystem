package com.hotelmanagement.appointmentservice.repository;

import com.hotelmanagement.appointmentservice.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByStaffIdAndAppointmentTime(String staffId, LocalDateTime appointmentTime);
}
