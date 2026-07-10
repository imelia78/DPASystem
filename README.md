# 🏥 HealthBridge Application (Distributed Doctor-Patient Appointment System)

A production-oriented medical appointment platform built on a microservices architecture.  
HealthBridge enables clients to discover verified doctors, book appointments, and leave reviews — while providing doctors with a structured onboarding and scheduling workflow backed by a secure identity management layer.

---

## Table of Contents

- [Killer Features](#killer-features)
- [Architecture Overview](#architecture-overview)
- [Services](#services)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Doctor Verification Workflow](#doctor-verification-workflow)

---

## Killer Features

### 🔐 JWT Authentication & Authorization via Keycloak
Every request to HealthBridge is secured with signed JSON Web Tokens issued by a dedicated Keycloak Identity Provider. Token validation is performed **locally within each service** using asymmetric RSA signature verification — no round-trip to Keycloak per request. Role-based access control (`dpasystem.DOCTOR`, `dpasystem.DOCTOR_PENDING`) is enforced at the method level via Spring Security's `@PreAuthorize`. Keycloak also enables **Single Sign-On (SSO)**: once authenticated, users gain access across all connected platform services without re-entering credentials.

### 📬 Event-Driven Notifications via Kafka + Gmail
When a client registers or an appointment is booked, the appointment-service publishes a domain event to an Apache Kafka topic. The notification-service consumes these events asynchronously and dispatches transactional confirmation emails via Gmail SMTP. This design ensures that a temporary mail delivery issue **never affects the core booking flow** — the services are fully decoupled at the infrastructure level.

### 📊 Review & Rating Statistics
Clients can rate doctors and leave detailed comments after completed appointments. The review subsystem exposes aggregated statistics per doctor — enabling data-driven discovery and transparent quality assessment across the platform.

### 💳 Transaction Management
All write operations on appointments, client profiles, and doctor records are wrapped in ACID-compliant PostgreSQL transactions managed via Spring's declarative `@Transactional`. This guarantees that partial failures — such as a crash mid-appointment-creation — never leave the database in an inconsistent state. Each microservice owns its own isolated database schema, eliminating cross-service transaction coupling entirely.

### ⚡ Kafka Event-Driven Architecture
Inter-service communication is fully asynchronous and event-driven. The appointment-service produces domain events; the notification-service consumes them independently. This gives the system **temporal decoupling**, **natural resilience** to downstream failures, and a clear foundation for adding new event consumers (analytics, audit log, push notifications) without modifying existing services.

---

## Architecture Overview

HealthBridge is composed of two independently deployable Spring Boot microservices that communicate asynchronously via Apache Kafka. Authentication and authorisation are delegated entirely to a Keycloak Identity Provider, which issues signed JWT tokens consumed by each service as an OAuth 2.0 Resource Server.

```
Client / Frontend
      │
      ▼
appointment-service  ──(Kafka: ClientRegisteredEvent / AppointmentBookedEvent)──▶  notification-service
      │                                                                                      │
      ▼                                                                                      ▼
 PostgreSQL (appointments_db)                                                  PostgreSQL (notifications_db)
      │
      ▼
  Keycloak (Identity Provider · SSO · JWT)
```

---

## Services

### appointment-service
The core domain service. Handles all primary business logic:

- **Client management** — registration, profile management, contact updates
- **Doctor management** — profile management, specialisation details, administrative verification
- **Appointment scheduling** — full lifecycle: create, view, modify, cancel
- **Review system** — clients can rate and comment on doctors after appointments

Exposes a versioned REST API under `/api/v1/` secured with JWT role-based access control.

### notification-service
A lightweight, event-driven consumer service responsible for:

- Listening to domain events published by the appointment-service via Kafka
- Sending transactional email notifications via Gmail SMTP (registration confirmation, appointment booking confirmation)

The service operates independently and does not expose a public API.

---

## Technology Stack

**⚙️ Core**  
`Java 21 LTS` · `Spring Boot 4` · `Spring MVC` · `Embedded Apache Tomcat`

**🗄️ Persistence**  
`PostgreSQL` · `Hibernate ORM` · `Spring Data JPA`

**🔐 Security**  
`Keycloak` · `OAuth 2.0 / OIDC` · `Spring Security` · `JWT`

**📨 Messaging**  
`Apache Kafka` · `Spring Kafka`

**📦 Infrastructure**  
`Docker` · `Docker Compose`

**📖 Observability & Docs**  
`Spring Boot Actuator` · `Springdoc OpenAPI` · `Swagger UI`

---

## Prerequisites

Before running HealthBridge, ensure the following are installed on your machine:

- **Docker** (v24+) — https://docs.docker.com/get-docker/
- **Docker Compose** (v2.x, included with Docker Desktop)

No local JDK or database installation is required — all services run inside containers.

---

## Configuration

### 1. Keycloak Realm

The repository includes a pre-configured Keycloak realm export file:

```
keycloak/dpasystem-realm.json
```

This file contains all realm settings, client definitions, roles (`dpasystem.DOCTOR`, `dpasystem.DOCTOR_PENDING`), and scope mappings required for the platform to function. It is imported automatically on first Keycloak startup via the Docker Compose configuration — no manual steps needed.

---

### 2. appointment-service — Client Secret

Open the production configuration file:

```
appointment-service/src/main/resources/application-prod.yml
```

Locate the Keycloak section and set your client secret:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://keycloak:8080/realms/dpasystem

keycloak:
  client-secret: YOUR_CLIENT_SECRET_HERE   # <-- set this value
```

The client secret can be found in the Keycloak Admin Console after startup:  
**Keycloak Admin → Realm: dpasystem → Clients → appointment-service → Credentials → Client Secret**

---

### 3. notification-service — Mail Application Password

The SMTP application password for the notification-service mailer is **not stored in the repository** for security reasons.

```
notification-service/src/main/resources/application.yml
```

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: YOUR_APP_PASSWORD_HERE   # <-- see paper documentation
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

> The application password is provided in the **paper project report** submitted to the supervisor.

---

## Running the Application

Once configuration is complete, start the full stack with a single command from the project root:

```bash
# Development environment (with debug ports exposed)
docker compose -f docker-compose.dev.yml up --build

# Production-equivalent environment
docker compose -f docker-compose.prod.yml up --build

# Start only the Keycloak instance (e.g. for initial setup inspection)
docker compose -f docker-compose.auth.yml up
```

On first launch, Docker will build the service images and pull dependencies. Subsequent starts are significantly faster.

**Startup order** (handled automatically by `depends_on` in Compose):

```
PostgreSQL  ──▶  Keycloak  ──▶  Kafka  ──▶  appointment-service
                                        └──▶  notification-service
```

To stop and remove all containers:

```bash
docker compose -f docker-compose.dev.yml down
```

To stop and also remove volumes (clears all database data):

```bash
docker compose -f docker-compose.dev.yml down -v
```

---

## API Documentation

Once the appointment-service is running, the interactive Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

The raw OpenAPI 3.0 specification (JSON) is available at:

```
http://localhost:8080/v3/api-docs
```

All endpoints require a valid Bearer token in the `Authorization` header, obtainable via the Keycloak token endpoint or through a connected frontend client.

---

## Doctor Verification Workflow

HealthBridge enforces a supervised onboarding process for medical professionals before they gain full platform access:

```
1. Doctor registers
        │
        ▼
2. Role assigned: dpasystem.DOCTOR_PENDING
   → Profile hidden from clients
   → Appointment endpoints return 403
        │
        ▼
3. Administrator reviews credentials
   via Keycloak Admin Console
        │
        ▼
4. Role updated to: dpasystem.DOCTOR
   → Profile visible to clients
   → Appointment booking available
```

Role management is performed in the Keycloak Admin Console:  
**Users → [Doctor account] → Role Mappings → Remove DOCTOR_PENDING → Assign DOCTOR**

---

## Health Monitoring

Service health endpoints (secured, requires admin role):

```
http://localhost:8080/actuator/health    — appointment-service
http://localhost:8081/actuator/health    — notification-service
```
