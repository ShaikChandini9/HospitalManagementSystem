package com.hotelmanagement.staffservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String staffIdNumber;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    private String address;

    // Roles assigned to this staff
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "staff_roles", joinColumns = @JoinColumn(name = "staff_id"))
    @Column(name = "role")
    private Set<String> roles;  // e.g., "ADMIN", "MANAGER", "EMPLOYEE"
}
