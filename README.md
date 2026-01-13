# Monday Clone Server (MCS)

> **Backend for Monday Clone Application**
>
> A robust, clean-architecture based backend service built with Java and Spring Boot, designed to power the [Monday Clone React Frontend](https://github.com/TuanHung1995/monday-clone-react).

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green.svg)](https://spring.io/projects/spring-boot)
[![Build](https://img.shields.io/badge/Build-Maven-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Table of Contents
- [About the Project](#-about-the-project)
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Configuration](#configuration)
- [Usage](#-usage)
- [Testing](#-testing)
- [Project Structure](#-project-structure)
- [Contributing](#-contributing)
- [Contact](#-contact)

## 📖 About the Project

**Monday Clone Server (MCS)** provides the core business logic, data persistence, and API endpoints for a task management platform inspired by monday.com. It is designed with modularity and scalability in mind, leveraging Domain-Driven Design (DDD) principles and Clean Architecture.

**Key Features:**
*   **Authentication & Authorization**: Secure JWT-based auth (Access/Refesh tokens), OAuth2 integration.
*   **Kanban/Board Management**: Create boards, groups, and tasks.
*   **Real-time Updates**: (Planned/In-progress) WebSocket integration for live collaboration.
*   **Resilience**: Rate limiting and circuit breaking with Resilience4j.

## 🏗 Architecture

The project follows a **Multi-module Maven** structure based on **Clean Architecture**:

*   **`mc-domain`**: The heart of the software. Contains entities, value objects, and domain services. No dependencies on frameworks.
*   **`mc-application`**: Orchestrates use cases using domain objects. Defines DTOs and interfaces for infrastructure.
*   **`mc-infrastructure`**: Implements interfaces defined in the application layer. Handles persistence (JPA/MySQL), security (Spring Security), and external APIs.
*   **`mc-controller`**: The entry point for REST API requests. Adapts HTTP requests to application use cases.
*   **`mc-start`**: The bootloader module containing the main application class.

## 🛠 Tech Stack

*   **Language**: Java 21
*   **Framework**: Spring Boot 3.5.7
*   **Database**: MySQL 8.0+
*   **Security**: Spring Security 6, JWT (jjwt), OAuth2 Client
*   **Resilience**: Resilience4j
*   **Utilities**: Lombok, Caffeine Cache, FastJSON
*   **Build Tool**: Maven

## 🚀 Getting Started

Follow these instructions to get a local copy of the project up and running.

### Prerequisites

*   [JDK 21](https://www.oracle.com/java/technologies/downloads/#java21) installed.
*   [Maven](https://maven.apache.org/download.cgi) installed (or use the included `mvnw` wrapper).
*   [MySQL](https://dev.mysql.com/downloads/installer/) running locally or via Docker.

### Installation

1.  **Clone the repository**
    ```bash
    git clone https://github.com/TuanHung1995/mcs.git
    cd mcs
    ```

2.  **Database Setup**
    *   Create a MySQL database named `monday_clone` (or update `application.yml` to match your DB name).
    *   Import the initial schema/data using `monday_clone.sql` located in the root directory.

3.  **Build the project**
    ```bash
    ./mvnw clean install
    ```

### Configuration

The application authenticates against the database and other services using environment variables or `application.yml`.
Ensure you configure your database credentials in `mc-start/src/main/resources/application.yml` or pass them as environment variables:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/monday_clone?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: YOUR_USERNAME
    password: YOUR_PASSWORD
```

## 🏃 Usage

**Run the application:**

```bash
./mvnw spring-boot:run -pl mc-start
```

The server will start at `http://localhost:8080`.

**API Documentation:**
Once the server is running, access the Swagger UI (if configured) or check the controller endpoints in `mc-controller`.

## 🧪 Testing

This project uses **JUnit 5** and **Mockito** for unit testing.

**Run all tests:**
```bash
./mvnw test
```

> **Note for JDK 21 Users:**
> The `pom.xml` is configured with `-XX:+EnableDynamicAgentLoading` and `-Xshare:off` to ensure compatibility with Mockito on newer JDK versions.

## 📂 Project Structure

```text
mcs
├── mc-domain           # Core business logic & entities
├── mc-application      # Service interfaces, DTOs, Use cases
├── mc-infrastructure   # Implementation (JPA, Security, Utils)
├── mc-controller       # REST Controllers
└── mc-start            # Main application entry point
```

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## 📧 Contact

**Frontend Repository**: [monday-clone-react](https://github.com/TuanHung1995/monday-clone-react)
