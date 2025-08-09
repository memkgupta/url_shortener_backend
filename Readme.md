## URL SHORTENER

### An implementation of a microservice-architecture-based URL shortener application.

In this project (experiment), I’ve built a scalable URL shortener service using **microservice architecture**.  
I integrated tools like **Kafka** and **Redis** to improve performance and handle real-time communication.  
Some tools may seem redundant or like an overkill for such a small project — but that’s intentional. The main goal was
to **learn how they work** and how they can be applied in real-world scalable systems.

---

## Tech Stack

**Backend:** Spring Boot (Microservices)  
**Messaging:** Apache Kafka  
**Cache:** Redis  
**Database:** PostgreSQL and MongoDB (used as TimeSeries DB for analytics)  
**Service Discovery:** Eureka Server  
**API Gateway:** Spring Cloud Gateway  
**Containerization:** Docker, Docker Compose  
**Others:** Node.js and Server-Sent Events (for live dashboard updates)

---

## Features

- Shorten long URLs with unique short codes
- Redirect shortened URLs to their original destination
- Real-time click analytics using Kafka Streams
- Caching with Redis for fast URL retrieval and analytics data
- Microservice architecture for scalability and modularity
- API Gateway for routing and service management
- SSE-based live analytics dashboard

---

## Architecture Overview

The application is split into multiple microservices:

1. **URL Shortener Service** – Handles short code generation and storage. For now, URL redirection is also handled here.
2. **Analytics Service** – Consumes click events from Kafka, stores them, and exposes REST API endpoints for retrieving
   analytics.
3. **User Service** – Handles user management and authentication. Users can register and log in via email/password or
   OAuth. The code is modular and adaptable to other OAuth providers.
4. **Analytics Dashboard** – A Node.js service that consumes live aggregated click event windows and displays real-time
   analytics using Server-Sent Events.
5. **API Gateway** – Routes incoming requests to respective services and authenticates protected API endpoints via the
   User Service.
6. **Eureka Server** – Manages service discovery.

---

## Setup & Installation

### **1️⃣ Clone the Repository**

```bash
git clone https://github.com/yourusername/url-shortener.git
cd url-shortener
```

```bash
docker-compose up --build -d
```

**NOTE :** There are some environment variables defined in the docker compose file make sure to change them and update
them

## Access the API Documentation from this endpoint

``http://localhost:8001/swagger-ui/index.html``

