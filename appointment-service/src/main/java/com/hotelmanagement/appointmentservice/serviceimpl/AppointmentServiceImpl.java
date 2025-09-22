package com.hotelmanagement.appointmentservice.serviceimpl;

import com.hotelmanagement.appointmentservice.entity.Appointment;
import com.hotelmanagement.appointmentservice.repository.AppointmentRepository;
import com.hotelmanagement.appointmentservice.request.AppointmentRequest;
import com.hotelmanagement.appointmentservice.response.AppointmentResponse;
import com.hotelmanagement.appointmentservice.response.PatientResponse;
import com.hotelmanagement.appointmentservice.response.StaffResponse;
import com.hotelmanagement.appointmentservice.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, RestTemplate restTemplate) {
        this.appointmentRepository = appointmentRepository;
        this.restTemplate = restTemplate;
    }

    private PatientResponse getPatient(Long patientId) {
        String url = "http://localhost:8083/api/v1/patients/get-by-id/" + patientId;
        return restTemplate.getForObject(url, PatientResponse.class);
    }

    private StaffResponse getStaff(String staffId) {
        String url = "http://localhost:8085/api/v1/staff/get-by-id/" + staffId;
        return restTemplate.getForObject(url, StaffResponse.class);
    }

    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        PatientResponse patient = getPatient(request.getPatientId());
        if (patient == null) throw new RuntimeException("Patient not found");

        StaffResponse staff = getStaff(request.getStaffId());
        if (staff == null) throw new RuntimeException("Staff not found");

        List<Appointment> conflicts = appointmentRepository.findByStaffIdAndAppointmentTime(
                request.getStaffId(), request.getAppointmentTime()
        );
        if (!conflicts.isEmpty()) throw new RuntimeException("Staff already has an appointment at this time");

        Appointment appointment = new Appointment();
        appointment.setPatientId(request.getPatientId());
        appointment.setStaffId(request.getStaffId());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());

        Appointment saved = appointmentRepository.save(appointment);

        AppointmentResponse response = new AppointmentResponse();
        response.setId(saved.getId());
        response.setPatientId(saved.getPatientId());
        response.setStaffId(saved.getStaffId());
        response.setAppointmentTime(saved.getAppointmentTime());
        response.setReason(saved.getReason());

        return response;
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(appt -> {
            AppointmentResponse resp = new AppointmentResponse();
            resp.setId(appt.getId());
            resp.setPatientId(appt.getPatientId());
            resp.setStaffId(appt.getStaffId());
            resp.setAppointmentTime(appt.getAppointmentTime());
            resp.setReason(appt.getReason());
            return resp;
        }).collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        Optional<Appointment> optional = appointmentRepository.findById(id);
        if (optional.isEmpty()) throw new RuntimeException("Appointment not found");
        Appointment appt = optional.get();
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appt.getId());
        response.setPatientId(appt.getPatientId());
        response.setStaffId(appt.getStaffId());
        response.setAppointmentTime(appt.getAppointmentTime());
        response.setReason(appt.getReason());
        return response;
    }

    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) throw new RuntimeException("Appointment not found");
        appointmentRepository.deleteById(id);
    }
}
