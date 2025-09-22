# Patient Service - Detailed Documentation

## Overview

**Patient Service** is a microservice that manages patient data in a healthcare or hotel management system.

It allows clients to:

- Create new patient records
- Retrieve existing patients
- Update or delete patients

**Key Features:**

- Centralized patient management
- Provides detailed responses and error handling
- Integrates with other services (like Appointment Service) via REST calls

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
6. [REST Workflow](#rest-workflow)
7. [Exception Handling](#exception-handling)
8. [Security](#security)
9. [Testing](#testing)
10. [Running the Service](#running-the-service)

---

## Entities

### Patient Entity

```java
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
}
```

**Explanation:**

- Stores basic patient information.
- `id` is the primary key.
- Other fields capture personal and contact info.

---

## DTOs (Data Transfer Objects)

### PatientRequest

```java
public class PatientRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
}
```

### PatientResponse

```java
public class PatientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
}
```

---

## Repository Layer

### PatientRepository

```java
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
```

**Explanation:**

- Provides CRUD operations via `JpaRepository`.

---

## Service Layer

### PatientService Interface

```java
public interface PatientService {
    PatientResponse createPatient(PatientRequest request);
    PatientResponse getPatientById(Long id);
    List<PatientResponse> getAllPatients();
    PatientResponse updatePatient(Long id, PatientRequest request);
    void deletePatient(Long id);
}
```

### PatientServiceImpl

```java
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientResponse createPatient(PatientRequest request) {
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setAddress(request.getAddress());
        patient.setDateOfBirth(request.getDateOfBirth());

        Patient saved = patientRepository.save(patient);

        PatientResponse response = new PatientResponse();
        response.setId(saved.getId());
        response.setFirstName(saved.getFirstName());
        response.setLastName(saved.getLastName());
        response.setEmail(saved.getEmail());
        response.setPhone(saved.getPhone());
        response.setAddress(saved.getAddress());
        response.setDateOfBirth(saved.getDateOfBirth());

        return response;
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream().map(patient -> {
            PatientResponse response = new PatientResponse();
            response.setId(patient.getId());
            response.setFirstName(patient.getFirstName());
            response.setLastName(patient.getLastName());
            response.setEmail(patient.getEmail());
            response.setPhone(patient.getPhone());
            response.setAddress(patient.getAddress());
            response.setDateOfBirth(patient.getDateOfBirth());
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setFirstName(patient.getFirstName());
        response.setLastName(patient.getLastName());
        response.setEmail(patient.getEmail());
        response.setPhone(patient.getPhone());
        response.setAddress(patient.getAddress());
        response.setDateOfBirth(patient.getDateOfBirth());
        return response;
    }

    @Override
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setAddress(request.getAddress());
        patient.setDateOfBirth(request.getDateOfBirth());

        Patient updated = patientRepository.save(patient);

        PatientResponse response = new PatientResponse();
        response.setId(updated.getId());
        response.setFirstName(updated.getFirstName());
        response.setLastName(updated.getLastName());
        response.setEmail(updated.getEmail());
        response.setPhone(updated.getPhone());
        response.setAddress(updated.getAddress());
        response.setDateOfBirth(updated.getDateOfBirth());
        return response;
    }

    @Override
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found");
        }
        patientRepository.deleteById(id);
    }
}
```

**Explanation:**

- Provides CRUD operations.
- Converts entity to DTO for API response.
- Throws exceptions when patient not found.

---

## Controller Layer

```java
@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/add")
    public ResponseEntity<PatientResponse> createPatient(@RequestBody PatientRequest request) {
        return new ResponseEntity<>(patientService.createPatient(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## REST Workflow

- **Endpoints:**
  - `POST /api/v1/patients/add`
  - `GET /api/v1/patients/{id}`
  - `GET /api/v1/patients`
  - `PUT /api/v1/patients/{id}`
  - `DELETE /api/v1/patients/{id}`

**Example Request (Create Patient):**

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "address": "123 Main St",
  "dateOfBirth": "1990-01-01"
}
```

**Example Response:**

```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "address": "123 Main St",
  "dateOfBirth": "1990-01-01"
}
```

---

## Exception Handling

- Throws `RuntimeException` for:
  - Patient not found
- Can implement a **Global Exception Handler** for proper HTTP responses.

---

## Security

- Spring Security JWT for authentication
- Role-based access if needed
- Protect endpoints with `@PreAuthorize`

---

## Testing

- Unit test `PatientServiceImpl` using **JUnit 5** and **Mockito**.
- Test scenarios:
  - Create patient
  - Get patient by ID
  - Update patient
  - Delete patient
  - Get all patients

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
http://localhost:8083/api/v1/patients
```
instructions**.

