package com.hotelmanagement.staffservice.controller;

import com.hotelmanagement.staffservice.request.StaffRequest;
import com.hotelmanagement.staffservice.response.StaffResponse;
import com.hotelmanagement.staffservice.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping("/add")
    public ResponseEntity<StaffResponse> addStaff(@RequestBody @Validated StaffRequest request) {
        StaffResponse response = staffService.createStaff(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<StaffResponse> getStaffById(@PathVariable Long id) {
        StaffResponse response = staffService.getStaffById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-staffId/{staffId}")
    public ResponseEntity<StaffResponse> getStaffByStaffIdNumber(@PathVariable String staffId) {
        StaffResponse response = staffService.getStaffByStaffIdNumber(staffId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<StaffResponse>> getAllStaff() {
        List<StaffResponse> list = staffService.getAllStaff();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/update/{staffId}")
    public ResponseEntity<StaffResponse> updateStaff(@PathVariable String staffId,
                                                     @RequestBody @Validated StaffRequest request) {
        StaffResponse response = staffService.updateStaff(staffId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
