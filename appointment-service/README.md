# Appointment Service

## Overview

**Appointment Service** is a microservice that manages appointments between patients and staff in a hotel or healthcare management system.

It allows clients to:

- Create new appointments
- Retrieve existing appointments
- Update or delete appointments

**Key Features:**

- Prevents staff double-booking at the same time
- Integrates with **Patient Service** and **Staff Service** via REST calls
- Provides detailed responses and error handling

**Tech Stack:**

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Data JPA & Hibernate**
- **MySQL**
- **Spring Security JWT** (optional)

---

## Table of Contents

1. [Entities](#entities)
2. [DTOs](#dtos)
3. [Repository Layer](#repository-layer)
4. [Service Layer](#service-layer)
5. [Controller Layer](#controller-layer)
6. [REST Calls & Workflow](#rest-calls--workflow)
7. [Exception Handling](#exception-handling)
8. [Security](#security)
9. [Testing](#testing)
10. [Running the Service](#running-the-service)

---

## Entities

### Appointment Entity

```java
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId; // ID from Patient Service
    private String staffId; // ID from Staff Service

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    private String reason;
}
```

**Explanation:**

- `patientId` and `staffId` reference other microservices rather than local entities.
- `appointmentTime` stores the scheduled date/time.
- `reason` is optional.

---

## DTOs (Data Transfer Objects)

### AppointmentRequest

```java
public class AppointmentRequest {
    private Long patientId;
    private String staffId;
    private LocalDateTime appointmentTime;
    private String reason;
}
```

### AppointmentResponse

```java
public class AppointmentResponse {
    private Long id;
    private Long patientId;
    private String staffId;
    private LocalDateTime appointmentTime;
    private String reason;
}
```

### PatientResponse & StaffResponse

These DTOs are received from **Patient Service** and **Staff Service** via REST calls.

---

## Repository Layer

### AppointmentRepository

```java
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByStaffIdAndAppointmentTime(String staffId, LocalDateTime appointmentTime);
}
```

**Explanation:**

- Checks staff availability before creating an appointment.
- Provides CRUD operations via `JpaRepository`.

---

## Service Layer

### AppointmentService Interface

```java
public interface AppointmentService {
    AppointmentResponse createAppointment(AppointmentRequest request);
    AppointmentResponse getAppointmentById(Long id);
    List<AppointmentResponse> getAllAppointments();
    void deleteAppointment(Long id);
}
```

### AppointmentServiceImpl

```java
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
        // 1. Validate Patient
        PatientResponse patient = getPatient(request.getPatientId());
        if (patient == null) throw new RuntimeException("Patient not found");

        // 2. Validate Staff
        StaffResponse staff = getStaff(request.getStaffId());
        if (staff == null) throw new RuntimeException("Staff not found");

        // 3. Check Staff Appointment Conflicts
        List<Appointment> conflicts = appointmentRepository.findByStaffIdAndAppointmentTime(
                request.getStaffId(), request.getAppointmentTime()
        );
        if (!conflicts.isEmpty()) throw new RuntimeException("Staff already has an appointment at this time");

        // 4. Save Appointment
        Appointment appointment = new Appointment();
        appointment.setPatientId(request.getPatientId());
        appointment.setStaffId(request.getStaffId());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());

        Appointment saved = appointmentRepository.save(appointment);

        // 5. Map to Response
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
```

**Explanation:**

- Calls Patient and Staff services via REST using `RestTemplate`.
- Validates existence of patient and staff.
- Checks for conflicts before saving an appointment.
- Maps entity to DTO for API response.

---

## Controller Layer

```java
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/add")
    public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody AppointmentRequest request) {
        return new ResponseEntity<>(appointmentService.createAppointment(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## REST Calls & Workflow

**REST calls to external services:**

- **Patient Service:** `GET /api/v1/patients/get-by-id/{patientId}`
- **Staff Service:** `GET /api/v1/staff/get-by-id/{staffId}`

**Workflow for creating an appointment:**

1. Client sends POST request to `/api/v1/appointments/add`.
2. Controller forwards request to `AppointmentServiceImpl`.
3. Service calls Patient and Staff services using `RestTemplate`.
4. Checks staff appointment conflicts.
5. Saves appointment in the database and returns response.

**Example Request:**

```json
{
  "patientId": 1,
  "staffId": "2",
  "appointmentTime": "2025-08-20T14:30:00",
  "reason": "Routine Checkup"
}
```

**Example Response:**

```json
{
  "id": 1,
  "patientId": 1,
  "staffId": "2",
  "appointmentTime": "2025-08-20T14:30:00",
  "reason": "Routine Checkup"
}
```

---

## Exception Handling

- `RuntimeException` for:

  - Patient not found
  - Staff not found
  - Staff conflict at requested time
  - Appointment not found

- A **Global Exception Handler** can be added for proper HTTP responses.

---

## Security

- Spring Security JWT for authentication.
- Role-based access: `ADMIN`, `DOCTOR`, `PATIENT`.
- Protect endpoints with `@PreAuthorize`.

---

## Testing

- Unit test `AppointmentServiceImpl` using **JUnit 5** and **Mockito**.
- Test scenarios:
  - Valid appointment creation
  - Conflict scenario
  - Get appointment by ID
  - Delete appointment

---

## Running the Service

1. Configure `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

2. Run service:

```bash
mvn spring-boot:run
```

3. Access endpoints:

```
http://localhost:8084/api/v1/appointments
```

---

This README fully explains **Appointment Service**, including **REST calls, conflict handling, DTOs, controller, service, repository, and workflow**.

