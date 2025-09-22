package com.hotelmanagement.appointmentservice.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Staff ID is required")
    private String staffId;

    @NotNull(message = "Appointment date and time is required")
    private LocalDateTime appointmentTime;

    private String reason;

}
