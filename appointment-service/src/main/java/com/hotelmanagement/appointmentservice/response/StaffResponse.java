package com.hotelmanagement.appointmentservice.response;

import lombok.Data;

import java.util.Set;

@Data
public class StaffResponse {

    private Long id;
    private String staffIdNumber;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private Set<String> roles;
}