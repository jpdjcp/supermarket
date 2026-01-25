# 🛒 Supermarket API

A backend REST API for managing a supermarket workflow, built with **Spring Boot** following clean architecture, domain-driven design principles, and real-world business rules.

This project was designed as a **portfolio-grade backend** to demonstrate professional backend practices: aggregates, DTO separation, validation, exception handling, Dockerization, and deployment readiness.

---

## ✨ Features

* Branch management
* Product catalog with price updates
* Sale lifecycle with realistic business states
* Sale items management (add / remove products)
* Validation with Jakarta Bean Validation
* Global exception handling with `@ControllerAdvice`
* DTO separation (Create / Update / Response)
* Dockerized backend + database
* Deploying in VPS / cloud services

---

## 🧠 Domain Model Overview

### Aggregates

* **Branch** (Aggregate Root)
* **Product** (Aggregate Root)
* **Sale** (Aggregate Root)

  * Owns `SaleItem` entities

`SaleItem` is **not** exposed as an independent aggregate.

---

## 🔄 Sale Lifecycle

Sales follow an explicit lifecycle using a `SaleStatus` enum:

* `OPEN` – Sale in progress (default)
* `FINISHED` – Sale completed
* `CANCELLED` – Sale aborted

### Business Rules

* Products can only be added or modified when the sale is `OPEN`
* Finished or cancelled sales are immutable
* Status transitions are explicit (no generic update endpoints)

---

## 📦 API Design

### Sales

* `POST /api/v1/sales` → Create a new sale
* `GET /api/v1/sales/{id}` → Get sale details
* `POST /api/v1/sales/{id}/items` → Add product to sale
* `GET /api/v1/sales/{id}/items` → List sale items
* `DELETE /api/v1/sales/{id}/items/{productId}` → Remove product from sale
* `POST /api/v1/sales/{id}/finish` → Finish sale
* `POST /api/v1/sales/{id}/cancel` → Cancel sale

### Products

* `POST /api/v1/products`
* `GET /api/v1/products`
* `GET /api/v1/products/{id}`
* `PUT /api/v1/products/{id}` (price update)
* `DELETE /api/v1/products/{id}`

### Branches

* `POST /api/v1/branches`
* `GET /api/v1/branches`
* `GET /api/v1/branches/{id}`
* `DELETE /api/v1/branches/{id}`

---

## 📄 DTO Strategy

DTOs are split by **intent**, not reused blindly:

* `CreateRequest`
* `UpdateRequest`
* `Response`

DTOs are organized by domain:

* `dto/branch`
* `dto/product`
* `dto/sale`
* `dto/sale/saleItem`

---

## ❗ Error Handling

Centralized error handling via `@RestControllerAdvice`.

Custom domain exceptions:

* `BranchNotFoundException`
* `ProductNotFoundException`
* `SaleNotFoundException`

Consistent error responses for clients.

---

## 🐳 Docker Support

The project is fully Dockerized:

* Spring Boot backend
* Relational database
* Ready to run with Docker Compose

Supports local development, VM deployment, and VPS hosting.

---

## 🚀 Deployment

* Tested on Ubuntu Server VM
* CORS issues resolved
* Already deployed on VPS (Fly.io)


---

## 🛠 Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* Jakarta Validation
* Lombok
* Docker & Docker Compose
* MySQL / PostgreSQL (configurable)

---

## 🎯 Project Goals

* Demonstrate backend engineering skills
* Model real business workflows (not just CRUD)
* Follow clean, maintainable architecture
* Be understandable and extensible by other developers

---

## 📌 Notes

* Security layer (Spring Security) intentionally postponed
* Testing and CI/CD planned as next steps

---

## 👤 Author

Backend developer focused on clean architecture and professional API design.

This project was built step-by-step with an emphasis on learning and correctness.
