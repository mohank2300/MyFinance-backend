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
