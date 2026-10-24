# In-Memory REST API Cache 🚀

A lightweight, high-performance in-memory Key-Value microservice inspired by Redis architecture, built with Java and Spring Boot.

---

## 🛠 Tech Stack

- **Language & Framework:** Java 17 / Spring Boot
- **Build Tool:** Apache Maven
- **Containerization:** Docker (Multi-stage build)
- **Testing:** JUnit 5, Spring Boot Test (`MockMvc`)
- **CI/CD:** GitHub Actions (Automated Maven Testing & Multi-arch Docker Buildx)

---

## 🏛 Architecture & Roadmap

The microservice provides thread-safe, in-memory data storage accessible via RESTful endpoints.

- **Current State:** Data storage is backed by `ConcurrentHashMap` to ensure basic thread safety during concurrent web requests.
- **Roadmap:** Replacing the standard Java collection with a custom `HybridHashTable` data structure designed to maintain $O(\log n)$ performance under heavy concurrent loads.

---

## 🔌 REST API Reference

Base Endpoint: `/api/kv`

| HTTP Method | Path | Parameters | Success Status | Description |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/kv` | `key` (query), `value` (query) | `200 OK` (`Saved: {key}`) | Store or overwrite a key-value pair |
| `GET` | `/api/kv/{key}` | `key` (path variable) | `200 OK` (value) / `404 Not Found` | Retrieve a value by key |
| `DELETE` | `/api/kv/{key}` | `key` (path variable) | `204 No Content` / `404 Not Found` | Remove a record from cache |

### cURL Examples

```bash
# 1. Store a key-value pair
curl -X POST "http://localhost:8081/api/kv?key=user&value=Emir"

# 2. Retrieve a value
curl -X GET "http://localhost:8081/api/kv/user"

# 3. Delete a key-value pair
curl -X DELETE "http://localhost:8081/api/kv/user"
```

---

## 🚀 Getting Started

### Prerequisites

- JDK 17 or higher
- Apache Maven 3.8+
- Docker Engine (optional, for containerization)

### Local Development (Maven)

```bash
# Run unit & integration tests
mvn clean test

# Launch the Spring Boot application (from the project directory)
mvn spring-boot:run
```

### Running with Docker

The project uses a two-stage Dockerfile based on `eclipse-temurin:17` to minimize image size and improve security.

```bash
# Build Docker image
docker build -t in-memory-cache .

# Run container (Exposed port: 8081)
docker run -p 8081:8081 in-memory-cache
```

---

## 🔄 CI/CD Pipelines (GitHub Actions)

- **Continuous Integration (CI):** Triggers on Pull Requests targeting the `main` branch. Executes `mvn clean test` in the `demo` directory to ensure code quality and protect `main` from broken code.
- **Continuous Delivery (CD):** Triggers upon successful merge into `main`. Uses `docker buildx` to build multi-architecture images (`x86_64` & `ARM64`) and publishes them to Docker Hub.

---

## 👥 Team & Responsibilities

- **Emir:** Integration testing suite (`KeyValuesControllerTests`) & Continuous Integration (CI) pipeline engineering.
- **Atabek:** Multi-stage Dockerfile design & Continuous Delivery (CD) automation with Docker Hub.
