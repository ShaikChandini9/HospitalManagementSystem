package com.hotelmanagement.staffservice.serviceimpl;

import com.hotelmanagement.staffservice.entity.Staff;
import com.hotelmanagement.staffservice.repository.StaffRepository;
import com.hotelmanagement.staffservice.request.StaffRequest;
import com.hotelmanagement.staffservice.response.StaffResponse;
import com.hotelmanagement.staffservice.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Override
    public StaffResponse createStaff(StaffRequest request) {
        if (staffRepository.existsByStaffIdNumber(request.getStaffIdNumber())) {
            throw new IllegalArgumentException("Staff ID number already exists");
        }
        if (staffRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Staff staff = mapRequestToEntity(request);
        staff = staffRepository.save(staff);
        return mapEntityToResponse(staff);
    }

    @Override
    public StaffResponse getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        return mapEntityToResponse(staff);
    }

    @Override
    public StaffResponse getStaffByStaffIdNumber(String staffIdNumber) {
        Staff staff = staffRepository.findByStaffIdNumber(staffIdNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        return mapEntityToResponse(staff);
    }

    @Override
    public List<StaffResponse> getAllStaff() {
        return staffRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StaffResponse updateStaff(String staffIdNumber, StaffRequest request) {
        Staff staff = staffRepository.findByStaffIdNumber(staffIdNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        // Update fields
        staff.setFullName(request.getFullName());
        staff.setEmail(request.getEmail());
        staff.setPhoneNumber(request.getPhoneNumber());
        staff.setAddress(request.getAddress());
        staff.setRoles(request.getRoles());

        staff = staffRepository.save(staff);
        return mapEntityToResponse(staff);
    }

    @Override
    public void deleteStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        staffRepository.delete(staff);
    }

    // Helper methods

    private Staff mapRequestToEntity(StaffRequest request) {
        Staff staff = new Staff();
        staff.setStaffIdNumber(request.getStaffIdNumber());
        staff.setFullName(request.getFullName());
        staff.setEmail(request.getEmail());
        staff.setPhoneNumber(request.getPhoneNumber());
        staff.setAddress(request.getAddress());
        staff.setRoles(request.getRoles());
        return staff;
    }

    private StaffResponse mapEntityToResponse(Staff staff) {
        StaffResponse response = new StaffResponse();
        response.setId(staff.getId());
        response.setStaffIdNumber(staff.getStaffIdNumber());
        response.setFullName(staff.getFullName());
        response.setEmail(staff.getEmail());
        response.setPhoneNumber(staff.getPhoneNumber());
        response.setAddress(staff.getAddress());
        response.setRoles(staff.getRoles());
        return response;
    }
}
