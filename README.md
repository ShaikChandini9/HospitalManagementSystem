
# 🏥 Hospital Management System

A **backend-only hospital management system** designed to manage core healthcare operations such as patient records, doctor schedules, billing, and laboratory management.  
This project demonstrates expertise in **REST API development**, **database design**, and **secure backend practices**.

> Built with **Spring Boot**, **Java 17**, and **MySQL**, following best practices for scalable backend systems.

---

## 🚀 Key Features

| Module            | Features |
|-------------------|----------|
| **Authentication** | Secure login with JWT, role-based access (Admin, Doctor, Nurse, Lab Tech, Pharmacist) |
| **Patient Management** | Register patients, store demographics, health history, insurance details |
| **Doctor Management** | Maintain doctor profiles, specialties, availability |
| **Appointments** | Schedule, reschedule, cancel appointments |
| **Pharmacy** | Manage medicine inventory and prescriptions |
| **Laboratory** | Handle test orders, results, and reports |
| **Billing** | Generate invoices, track payments |
| **Reports** | Generate analytics and system reports |

---

## 🧰 Tech Stack

| Layer         | Technology |
|---------------|------------|
| **Backend**   | Java 17, Spring Boot, Spring Data JPA |
| **Database**  | MySQL |
| **Authentication** | Spring Security, JWT |
| **Build Tool** | Maven |
| **Testing**   | JUnit, Postman for API testing |
| **Deployment**| Docker, AWS (optional) |

---

## 🏗️ Architecture

The backend follows **layered architecture** for scalability and clean code separation.

```
Controller Layer  ->  Service Layer  ->  Repository Layer  ->  Database
```

### Flow:
1. **Controller Layer:** Handles API requests and responses.  
2. **Service Layer:** Business logic and validation.  
3. **Repository Layer:** Data persistence using JPA.  
4. **Database Layer:** MySQL with normalized tables.

---

## 🗃️ Database Schema

### Core Tables:
- **Users** – login credentials and roles.  
- **Patients** – patient details and health records.  
- **Doctors** – specialization, schedules.  
- **Appointments** – booking information.  
- **Pharmacy** – medicine inventory.  
- **LabTests** – lab orders and results.  
- **Billing** – invoices and payments.

### Example ERD:
```
Patient <--> Appointment <--> Doctor
Patient <--> Billing
Doctor <--> Prescription
Prescription <--> Pharmacy
```

---

## 📡 API Endpoints

| Endpoint                   | Method | Description |
|----------------------------|--------|-------------|
| `/api/v1/auth/login`       | POST   | Authenticate and return JWT |
| `/api/v1/patients`         | GET    | Fetch all patients |
| `/api/v1/patients/{id}`    | GET    | Fetch patient by ID |
| `/api/v1/patients`         | POST   | Create a new patient |
| `/api/v1/appointments`     | POST   | Schedule appointment |
| `/api/v1/appointments`     | GET    | List all appointments |
| `/api/v1/billing/invoice`  | GET    | Generate or fetch invoice |
| `/api/v1/pharmacy/stock`   | PUT    | Update medicine stock |

---

## ⚙️ Setup Instructions

### **1. Clone the Repository**
```bash
git clone https://github.com/YourUsername/HospitalManagementSystem.git
cd HospitalManagementSystem
```

### **2. Configure Database**
Create a MySQL database:
```sql
CREATE DATABASE hospital_management;
```

Update `application.properties`:
```
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_management
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
jwt.secret=your_jwt_secret
```

### **3. Run the Backend**
```bash
./mvnw spring-boot:run
```

Backend will run at: **http://localhost:8080**

---

## 🧪 Testing APIs

Use **Postman** or **cURL** to test endpoints.

Example request to login:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login -H "Content-Type: application/json" -d '{"username":"admin","password":"admin123"}'
```

---

## 🛡 Security Measures
- Passwords encrypted using **BCrypt**  
- **JWT-based authentication** for stateless security  
- Role-based access control (RBAC)  
- Centralized exception handling and input validation  
- Audit logs for sensitive actions  

---

## 🗺 Future Enhancements
- Implement email notifications for appointments.  
- Add insurance claim processing module.  
- Introduce HL7/FHIR API for interoperability.  
- Build a React or Angular front-end interface.  
- Deploy to Kubernetes for scalability.
