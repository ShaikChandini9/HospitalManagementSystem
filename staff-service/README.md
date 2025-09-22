# Staff Service - Detailed Documentation

## Overview

Staff Service is a microservice that manages staff data in a healthcare system, including doctors, nurses, and administrative personnel. It allows creating, updating, retrieving, and deleting staff information. This service is built using **Java 21** and **Spring Boot 3.4.5**, with RESTful APIs, **Spring Data JPA**, **Hibernate**, and **MySQL**.

---

## Table of Contents

1. [Entities](#entities)
2. [DTOs](#dtos)
3. [Repository Layer](#repository-layer)
4. [Service Layer](#service-layer)
5. [Controller Layer](#controller-layer)
6. [Exception Handling](#exception-handling)
7. [Security](#security)
8. [Testing](#testing)
9. [Running the Service](#running-the-service)

---

## Entities

### Staff Entity

Represents staff data in the database.

```java
@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role; // e.g., DOCTOR, NURSE, ADMIN

    private String phoneNumber;
    private String department;
}
```

**Explanation:**

- `@Entity` marks the class as a JPA entity.
- `@Table(name = "staff")` specifies the database table.
- `@Id` and `@GeneratedValue` handle the primary key.
- `@Column` maps fields to table columns with constraints.

---

## DTOs (Data Transfer Objects)

### StaffRequestDTO

Used for creating/updating staff.

```java
public class StaffRequestDTO {
    private String name;
    private String email;
    private String role;
    private String phoneNumber;
    private String department;
}
```

### StaffResponseDTO

Used for returning staff data in API responses.

```java
public class StaffResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String phoneNumber;
    private String department;
}
```

**Explanation:** DTOs decouple API layer from internal entity structure, ensuring security and flexibility.

---

## Repository Layer

### StaffRepository

```java
@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);
}
```

**Explanation:**

- Extends `JpaRepository` for CRUD operations.
- Custom method `findByEmail` checks for existing staff.

---

## Service Layer

### StaffService Interface

```java
public interface StaffService {
    StaffResponseDTO createStaff(StaffRequestDTO staffRequestDTO);
    StaffResponseDTO getStaffById(Long id);
    List<StaffResponseDTO> getAllStaff();
    StaffResponseDTO updateStaff(Long id, StaffRequestDTO staffRequestDTO);
    void deleteStaff(Long id);
}
```

### StaffServiceImpl

```java
@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public StaffResponseDTO createStaff(StaffRequestDTO dto) {
        Staff staff = new Staff();
        staff.setName(dto.getName());
        staff.setEmail(dto.getEmail());
        staff.setRole(dto.getRole());
        staff.setPhoneNumber(dto.getPhoneNumber());
        staff.setDepartment(dto.getDepartment());

        Staff savedStaff = staffRepository.save(staff);
        return mapToResponse(savedStaff);
    }

    private StaffResponseDTO mapToResponse(Staff staff) {
        StaffResponseDTO response = new StaffResponseDTO();
        response.setId(staff.getId());
        response.setName(staff.getName());
        response.setEmail(staff.getEmail());
        response.setRole(staff.getRole());
        response.setPhoneNumber(staff.getPhoneNumber());
        response.setDepartment(staff.getDepartment());
        return response;
    }

    // Other CRUD methods implemented similarly...
}
```

**Explanation:**

- Contains business logic.
- Maps DTOs to entities and vice versa.
- Handles repository interactions.

---

## Controller Layer

### StaffController

```java
@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    public ResponseEntity<StaffResponseDTO> createStaff(@RequestBody StaffRequestDTO dto) {
        StaffResponseDTO response = staffService.createStaff(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> getStaff(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @GetMapping
    public ResponseEntity<List<StaffResponseDTO>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> updateStaff(@PathVariable Long id, @RequestBody StaffRequestDTO dto) {
        return ResponseEntity.ok(staffService.updateStaff(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
```

**Explanation:**

- Exposes REST endpoints.
- Maps HTTP methods to service methods.
- Returns appropriate HTTP status codes.

---

## Exception Handling

### Custom Exception

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

### Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("status", "404");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

**Explanation:**

- Handles exceptions globally.
- Returns meaningful error messages to clients.

---

## Security

- Spring Security with JWT authentication.
- Role-based access (ADMIN, DOCTOR, NURSE).
- Protect endpoints using `@PreAuthorize`.

Example:

```java
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
    staffService.deleteStaff(id);
    return ResponseEntity.noContent().build();
}
```

---

## Testing

- Use **JUnit 5** and **Mockito**.
- Unit test service and controller layers.

Example:

```java
@Test
void createStaff_shouldReturnStaffResponse() {
    StaffRequestDTO dto = new StaffRequestDTO();
    dto.setName("Dr. Smith");
    dto.setEmail("drsmith@example.com");

    when(staffRepository.save(any(Staff.class))).thenReturn(new Staff());

    StaffResponseDTO response = staffService.createStaff(dto);
    assertNotNull(response);
}
```

---

## Running the Service

1. Configure **application.properties**:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

2. Run using:

```bash
mvn spring-boot:run
```

3. Access endpoints at `http://localhost:8082/api/v1/staff`

---

This completes the detailed Staff Service documentation.

