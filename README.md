# E-Commerce Microservices Project

A Spring Boot-based e-commerce application built using a **parent-multi module** architecture with microservices, featuring service discovery, API gateway routing, and event-driven communication.

## 📋 Project Overview

This project demonstrates a production-ready microservices architecture where independent services work together to form an e-commerce platform. The system uses Eureka for service discovery, Spring Cloud Gateway for API routing, and Apache Kafka for asynchronous event streaming.

## 🏗️ Architecture

### Parent-Multi Module Structure

```
Ecommerce (Parent)
├── discovery-server (Eureka Server)
├── api-gateway (Spring Cloud Gateway)
├── product (Microservice)
├── order (Microservice)
├── inventory (Microservice)
└── common (Shared utilities & events)
```

### Services Overview

| Service | Purpose | Port | Database |
|---------|---------|------|----------|
| **Discovery Server** | Eureka service registry & discovery | 8761 | N/A |
| **API Gateway** | Spring Cloud Gateway for routing | 8080 | N/A |
| **Product Service** | Manages product catalog | 8081 | product_db |
| **Order Service** | Handles customer orders & events | 8082 | order_db |
| **Inventory Service** | Manages product stock & updates | 8083 | inventory_db |

## 📡 Microservices Details

### 1. Product Service (Port 8081)
- **Purpose:** Manages product catalog
- **Features:** Product CRUD operations, Kafka event publishing
- **Database:** PostgreSQL
- **API Prefix:** `/api/products/**`

### 2. Order Service (Port 8082)
- **Purpose:** Handles customer orders
- **Features:** Order processing, order lifecycle events, Kafka integration
- **Database:** PostgreSQL
- **Messaging:** Apache Kafka (producer & consumer)
- **API Prefix:** `/api/orders/**`

### 3. Inventory Service (Port 8083)
- **Purpose:** Manages product inventory/stock
- **Features:** Stock tracking, real-time inventory updates via Kafka
- **Database:** PostgreSQL
- **Messaging:** Apache Kafka (producer & consumer)
- **API Prefix:** `/api/inventory/**`

## 🔧 Infrastructure

### Discovery Server (Port 8761)
- **Role:** Eureka service registry
- All microservices register and discover each other through this server
- Dashboard available at: `http://localhost:8761`

### API Gateway (Port 8080)
- **Role:** Single entry point for all client requests
- Routes requests to appropriate microservices using load balancer (`lb://`)
- Centralized request handling and service discovery integration

### Message Broker
- **Technology:** Apache Kafka with Zookeeper
- **Kafka Brokers Port:** 29092 (internal), 9092 (external)
- **Zookeeper Port:** 2181
- **Purpose:** Asynchronous event communication between services

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Spring Boot** | 3.3.2 | Application framework |
| **Spring Cloud** | 2023.0.3 | Cloud-native features (Eureka, Gateway) |
| **Java** | 21 | Programming language |
| **Spring Data JPA** | - | Database access & ORM |
| **PostgreSQL** | 16 | Production database |
| **Apache Kafka** | 7.6.1 | Event streaming & async messaging |
| **Eureka** | Spring Cloud | Service discovery & registration |
| **Spring Cloud Gateway** | - | API Gateway & routing |
| **Lombok** | 1.18.34 | Reduce boilerplate code |
| **SpringDoc OpenAPI** | 2.6.0 | API documentation (Swagger) |
| **Maven** | 3.6+ | Build tool |

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- Docker & Docker Compose (for running with containerization)

### Option 1: Run with Docker Compose (Recommended)

```bash
# Build all Docker images
docker-compose build

# Start all services (database, Kafka, microservices, gateway)
docker-compose up

# Services will be available at:
# - API Gateway: http://localhost:8080
# - Discovery Server: http://localhost:8761
# - Product Service: http://localhost:8081
# - Order Service: http://localhost:8082
# - Inventory Service: http://localhost:8083
```

### Option 2: Build & Run Locally

**Build all modules:**
```bash
mvn clean install
```

**Build specific module:**
```bash
mvn clean install -pl product
mvn clean install -pl order
mvn clean install -pl inventory
```

**Run Discovery Server first:**
```bash
cd discovery-server
mvn spring-boot:run
```

**Run other services (in separate terminals):**
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

# API Gateway
cd api-gateway
mvn spring-boot:run
```

### Run Tests
```bash
# All modules
mvn test

# Specific module
mvn test -pl product
```

## 🌐 API Gateway Routes

All requests go through the API Gateway (Port 8080):

| Endpoint | Routes To | Service |
|----------|-----------|---------|
| `/api/products/**` | `lb://product` | Product Service (8081) |
| `/api/orders/**` | `lb://order` | Order Service (8082) |
| `/api/inventory/**` | `lb://inventory` | Inventory Service (8083) |

