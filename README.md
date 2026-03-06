# MyFinance Backend

A secure finance tracking backend built with **Spring Boot**, **Spring Security**, **JWT**, **PostgreSQL**, and **Docker**.

This project allows users to register, log in, and manage transactions securely with role-based access.

---

## Features

- User registration and login
- JWT authentication
- Role-based authorization (`USER`, `ADMIN`)
- Transaction CRUD APIs
- PostgreSQL database integration
- Docker support for database
- Swagger UI for API testing
- Global exception handling
- Input validation

---

## Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT
- PostgreSQL
- Docker
- Maven
- Swagger / OpenAPI

---

## Project Structure

```bash
src/main/java/finance/com/MyFinance/com
│
├── auth
├── common
├── config
├── transaction
├── user
└── Application.java





API Endpoints
Auth

POST /api/auth/register

POST /api/auth/login

Transactions

GET /api/transactions

POST /api/transactions

PUT /api/transactions/{id}

DELETE /api/transactions/{id}

Security

/api/auth/** → public

/api/transactions/** → secured with JWT

DELETE /api/transactions/** → ADMIN only

Swagger

After running the app, open:

http://localhost:8080/swagger-ui/index.html

OpenAPI docs:

http://localhost:8080/v3/api-docs
How to Run
1. Clone the repo
git clone https://github.com/mohank2300/MyFinance-backend.git
cd MyFinance-backend
2. Start PostgreSQL with Docker
docker compose up -d
3. Run the Spring Boot app

Using IntelliJ or:

./mvnw spring-boot:run

On Windows:

mvnw.cmd spring-boot:run
Database Configuration

The app uses PostgreSQL running in Docker.

Example database config:

spring.datasource.url=jdbc:postgresql://localhost:5432/finance_tracker
spring.datasource.username=postgres
spring.datasource.password=postgres

Author

Mohan K / Bannu

GitHub: mohank2300
