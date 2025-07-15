
# 📚 E-Learning Microservices System

This project is a distributed **E-Learning Platform** built with a **Microservices Architecture** using **Spring Boot**, **Spring Cloud**, and supporting tools. It manages users (Admins, Trainers, Learners), course creation, subscriptions, secure payments, exams, and evaluation – all via scalable and maintainable services.

---

## ⚙️ Architecture Overview

The system follows the **Microservices pattern**, where each service operates independently and communicates via REST APIs. It supports dynamic discovery, load balancing, and failure resilience.

### 🧩 Microservices

| Service              | Responsibility                                                                           |
| -------------------- | ---------------------------------------------------------------------------------------- |
| `discovery-server`   | Service registry (Eureka) for automatic discovery                                        |
| `api-gateway`        | Central API Gateway that routes and filters requests                                     |
| `user-service`       | Handles user registration, authentication, and role management (ADMIN, TRAINER, LEARNER) |
| `course-service`     | Manages training courses: creation, approval, publishing                                 |
| `enrollment-service` | Manages course enrollments and secure payment validation                                 |
| `exam-service`       | Handles quizzes, evaluations, and course completion status                               |

---

## 🔌 Communication & Integration

* All services expose **RESTful APIs** for CRUD operations.
* **API Gateway** (`Spring Cloud Gateway`) handles routing, logging, and global filters.
* Services interact using **`RestTemplate`** and **`WebClient`**, with `@LoadBalanced` to support dynamic service discovery via **Eureka**.
* Example: `course-service` communicates with `exam-service` to auto-generate exams when a new course is published.

---

## ♻️ Load Balancing & Resilience

* **Load Balancing** is implemented using **Spring Cloud LoadBalancer** with Eureka – tested specifically on **exam creation requests** when courses are created.
* **Resilience4j** is used to enhance system reliability:

  * **Circuit Breaker** to isolate failing services.
  * **Retry** for transient errors.
  * **Rate Limiter** to prevent service overloads.

---

## 🔐 Security

* **Spring Security** secures all endpoints, ensuring role-based access control and token-based authentication.
* Sensitive services are protected behind the **API Gateway**.

---

## 🧪 Tested Scenario

**Load Distribution Test**: Verified while creating exams through the `exam-service` upon new course submission from `course-service`. Multiple instances responded via the gateway, confirming correct load balancing behavior.

---

## 🛠️ Tech Stack

| Tool / Framework              | Purpose                                                |
| ----------------------------- | ------------------------------------------------------ |
| **Java + Spring Boot**        | Core microservice development                          |
| **Spring Cloud Gateway**      | API Gateway implementation                             |
| **Eureka Discovery**          | Dynamic service discovery                              |
| **Spring Cloud LoadBalancer** | Client-side load balancing                             |
| **Resilience4j**              | Fault tolerance (Circuit Breaker, Retry, Rate Limiter) |
| **MongoDB**                   | NoSQL Database per service                             |
| **Maven**                     | Dependency and build management                        |
| **Lombok**                    | Boilerplate code reduction                             |
| **RestTemplate / WebClient**  | Inter-service communication                            |

---

## 🚀 Getting Started

> Make sure to start services in the following order:

1. `discovery-server`
2. `api-gateway`
3. All other microservices (user, course, enrollment, exam)

Then, access the services via the API Gateway (e.g., `http://localhost:8080/user/...`).

---

## 📂 Folder Structure (Example)

```
e-learning-system/
├── discovery-server/
├── api-gateway/
├── user-service/
├── course-service/
├── enrollment-service/
├── exam-service/
└── common-config/ (optional shared config)
```

---

## 👨‍💻 Authors

* Salma Abu Shaqir

**Supervisor:** Eng. Aya Al-Moati

---

