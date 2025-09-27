
# 🏥 Hospital Management System

A **backend-only hospital management system** built using **Microservice Architecture** to manage critical healthcare operations such as patient records, doctor schedules, billing, and laboratory services.

This project demonstrates enterprise-level skills in **microservices design**, **REST APIs**, **secure authentication**, and **distributed systems**.  
Each module is **loosely coupled**, **independently deployable**, and communicates via **REST APIs**, enabling scalability and flexibility.

> Built with **Spring Boot**, **Java 17**, and **MySQL**, following domain-driven design principles.

---

## 🚀 Key Features

| Microservice       | Responsibilities |
|--------------------|------------------|
| **Auth Service**    | User login, JWT-based authentication, and role management |
| **Patient Service** | Manage patient registration, health history, insurance |
| **Doctor Service**  | Doctor profiles, specialties, availability schedules |
| **Appointment Service** | Scheduling, rescheduling, and cancellations |
| **Pharmacy Service** | Medicine inventory and prescription management |
| **Lab Service**     | Test orders, results, and reporting |
| **Billing Service** | Generate invoices and track payments |
| **Report Service**  | Analytics and administrative reports |

---

## 🧰 Tech Stack

| Layer         | Technology |
|---------------|------------|
| **Backend**   | Java 17, Spring Boot, Spring Cloud |
| **Database**  | MySQL (separate DB for each microservice) |
| **API Gateway** | Spring Cloud Gateway |
| **Service Discovery** | Netflix Eureka |
| **Authentication** | Spring Security, JWT |
| **Build Tool** | Maven |
| **Testing**   | JUnit, Postman |
| **Deployment**| Docker, Kubernetes, AWS |

---

## 🏗️ Microservice Architecture

Each module is a **standalone microservice** with its own database and deployment pipeline.  
Services communicate through **REST APIs**, and future versions can include **message queues** (Kafka or RabbitMQ).

### Architecture Flow
```
[ Client / Postman ]
       |
       v
[ API Gateway ]
       |
---------------------------------------------
|       |         |         |               |
v       v         v         v               v
Auth   Patient   Doctor   Appointment     Billing                              
```

### Benefits of This Architecture
- **Scalability**: Scale services independently based on demand.  
- **Fault Isolation**: Failure in one service doesn’t crash the entire system.  
- **Independent Deployments**: Deploy services without affecting others.  
- **Domain-Driven**: Services aligned with real healthcare modules.  

---

## 🗃️ Database Schema (Per Service)

Each microservice manages its own database. This promotes **data isolation** and **service autonomy**.

| Service           | Key Tables |
|-------------------|------------|
| **Auth Service**   | Users, Roles |
| **Patient Service**| Patients, Medical History |
| **Doctor Service** | Doctors, Specialties, Schedules |
| **Appointment Service** | Appointments, Visit Notes |
| **Pharmacy Service** | Medicines, Prescriptions |
| **Lab Service**    | Lab Orders, Test Results |
| **Billing Service**| Invoices, Payments |

Example Relationship (Patient & Doctor):
```
Patient <--> Appointment <--> Doctor
Patient <--> Billing
Doctor <--> Prescription
Prescription <--> Pharmacy
```

---

## 📡 API Endpoints

| Endpoint                        | Method | Description |
|--------------------------------|--------|-------------|
| `/api/v1/auth/login`           | POST   | Authenticate and get JWT token |
| `/api/v1/patients`             | GET    | Fetch all patients |
| `/api/v1/patients/{id}`        | GET    | Fetch specific patient by ID |
| `/api/v1/appointments`         | POST   | Create new appointment |
| `/api/v1/appointments`         | GET    | List all appointments |
| `/api/v1/billing/invoice`      | GET    | Retrieve or generate invoices |
| `/api/v1/pharmacy/stock`       | PUT    | Update medicine stock |

> All APIs are versioned and protected with **JWT authentication**.

---

## ⚙️ Setup Instructions

### **1. Clone the Repository**
```bash
git clone https://github.com/YourUsername/HospitalManagementSystem.git
cd HospitalManagementSystem
```

### **2. Configure Databases**
Create separate MySQL databases for each service:
```sql
CREATE DATABASE auth_service;
CREATE DATABASE patient_service;
CREATE DATABASE doctor_service;
CREATE DATABASE appointment_service;
CREATE DATABASE pharmacy_service;
CREATE DATABASE lab_service;
CREATE DATABASE billing_service;
```

Update each service's `application.properties` with the correct DB URL and credentials.

Example (`patient-service`):
```
spring.datasource.url=jdbc:mysql://localhost:3306/patient_service
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
jwt.secret=your_jwt_secret
```

### **3. Run Each Microservice**
Navigate to each service folder and run:
```bash
./mvnw spring-boot:run
```

Example:
```bash
cd auth-service
./mvnw spring-boot:run

cd ../patient-service
./mvnw spring-boot:run
```

### **4. Run with Docker (Optional)**
```bash
docker compose up --build
```

---

## 🧪 Testing APIs

Use **Postman** or **cURL** to test endpoints.

Example login request:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login -H "Content-Type: application/json" -d '{"username":"admin","password":"admin123"}'
```

---

## 🛡 Security Measures

- **JWT-based authentication** for stateless security.  
- **Role-based access control (RBAC)**.  
- **Password hashing** using BCrypt.  
- Centralized **input validation** and **error handling**.  
- Audit trails for sensitive operations.  
- Potential integration with **OAuth2** and **2FA** in future.

---

## 🗺 Future Enhancements

- Event-driven communication using Kafka or RabbitMQ.  
- API documentation with Swagger/OpenAPI.  
- Healthcare interoperability via HL7/FHIR.  
- AI-driven analytics for patient care.  
- Build a React or Angular front-end.  
- Kubernetes deployment for production-grade scaling.
