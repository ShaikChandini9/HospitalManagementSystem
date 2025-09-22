package com.hotelmanagement.staffservice.service;

import com.hotelmanagement.staffservice.request.StaffRequest;
import com.hotelmanagement.staffservice.response.StaffResponse;

import java.util.List;

public interface StaffService {

    StaffResponse createStaff(StaffRequest staffRequest);
    StaffResponse getStaffById(Long id);
    StaffResponse getStaffByStaffIdNumber(String staffIdNumber);
    List<StaffResponse> getAllStaff();
    StaffResponse updateStaff(String staffIdNumber, StaffRequest staffRequest);
    void deleteStaff(Long id);
}
