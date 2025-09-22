package com.hotelmanagement.appointmentservice.service;

import com.hotelmanagement.appointmentservice.request.AppointmentRequest;
import com.hotelmanagement.appointmentservice.response.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest request);
    AppointmentResponse getAppointmentById(Long id);
    List<AppointmentResponse> getAllAppointments();
    void deleteAppointment(Long id);
}
