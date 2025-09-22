package com.hotelmanagement.staffservice.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class StaffRequest {

    @NotBlank
    @Size(min = 6, max = 12)
    private String staffIdNumber;

    @NotBlank
    @Size(min = 2, max = 100)
    private String fullName;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    private String address;

    private Set<@NotBlank String> roles;  // Roles assigned
}
