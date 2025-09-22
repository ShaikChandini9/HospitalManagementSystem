package com.hotelmanagement.staffservice.repository;

import com.hotelmanagement.staffservice.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByStaffIdNumber(String staffIdNumber);
    boolean existsByStaffIdNumber(String staffIdNumber);
    boolean existsByEmail(String email);
}
