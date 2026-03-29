# E-Commerce Microservices Project

A Spring Boot-based e-commerce application built using a **parent-multi module** architecture with microservices.

## 📋 Project Overview

This project demonstrates a microservices architecture where multiple independent services work together to form an e-commerce platform. Each service is responsible for a specific business domain.

##  Architecture

### Parent-Multi Module Structure

```
Ecommerce (Parent)
├── product (Microservice)
├── order (Microservice)
└── inventory (Microservice)
```

##  Microservices

### 1. Product Service
- **Purpose:** Manages product catalog
- **Features:** Product CRUD operations
- **Port:** TBD
- **Database:** PostgreSQL (production), H2 (testing)

### 2. Order Service
- **Purpose:** Handles customer orders
- **Features:** Order processing, Kafka integration for event streaming
- **Port:** TBD
- **Database:** PostgreSQL (production), H2 (testing)
- **Messaging:** Apache Kafka

### 3. Inventory Service
- **Purpose:** Manages product inventory/stock
- **Features:** Stock tracking, Kafka integration for real-time updates
- **Port:** TBD
- **Database:** PostgreSQL (production), H2 (testing)
- **Messaging:** Apache Kafka

##  Technology Stack

| Technology | Purpose |
|------------|---------|
| **Spring Boot 4.0.3** | Application framework |
| **Java 21** | Programming language |
| **Spring Data JPA** | Database access |
| **PostgreSQL** | Production database |
| **H2 Database** | In-memory database for testing |
| **Apache Kafka** | Event streaming (Order & Inventory) |
| **Lombok** | Reduce boilerplate code |
| **SpringDoc OpenAPI** | API documentation (Swagger) |
| **Maven** | Build tool |



### Prerequisites
- Java 21 or higher
- Maven 3.6+
- PostgreSQL (for production)

### Build All Modules
```bash
mvn clean install
```

### Build Specific Module
```bash
mvn clean install -pl product
mvn clean install -pl order
mvn clean install -pl inventory
```

### Run a Service
```bash
# Product Service
cd product
mvn spring-boot:run

# Order Service
cd order
mvn spring-boot:run

# Inventory Service
cd inventory
mvn spring-boot:run
```

### Run Tests
```bash
# All modules
mvn test

# Specific module
mvn test -pl product
```

##  API Documentation

Once a service is running, access the Swagger UI at:
```
http://localhost:<port>/swagger-ui.html
```

API docs JSON:
```
http://localhost:<port>/v3/api-docs
```
