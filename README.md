# 💰 FinanceHelper

## 📖 Introduction
**FinanceHelper** is a full-stack personal finance management web application designed to help users gain complete control over their spending and financial goals. With a modern and intuitive **Glassmorphism**-based interface, the platform allows users to record transactions, manage categories and companies, and set monthly spending limits (goals), providing a clear overview of their financial health through interactive charts.

Avaible at: https://finance-helper-one.vercel.app/

---

## ✨ Key Features

* 🔒 **Authentication & Security:** User registration and login with JWT token-based authentication and BCrypt password encryption.
* 📊 **Interactive Dashboard:** A dashboard featuring expense summaries, transaction counts, and interactive data visualization (pie and bar charts powered by Recharts).
* 💸 **Transaction Management:** Detailed expense tracking with category and company associations. Includes advanced filtering (by date, category, and company) and sorting options.
* 🎯 **Financial Goals:** Create spending limits linked to categories or companies, with automatic progress calculation and dynamic status updates (*Active, Completed, Exceeded*).
* 🏷️ **Category & Company Customization:** Create categories and companies with custom colors and upload icons/images for easier visual identification.
* 🎨 **Modern & Responsive UI/UX:** Elegant glassmorphism design, native dark mode support, and dynamic user feedback through global alerts and modal dialogs.
* 🌍 **Internationalization (i18n):** Backend validation messages and error responses are fully prepared for multiple languages (English and Brazilian Portuguese).
* 🐳 **Docker Ready:** Configured for easy PostgreSQL database orchestration using `docker-compose.yml`.

---

## 🏗️ Project Architecture & Components

The project is divided into two main parts: **Frontend** and **Backend**, following a clear **Separation of Concerns (SoC)** architecture.

### 💻 Frontend (`React` + `Vite`)
The frontend is organized in a modular structure to ensure scalability and code reusability:

* **`pages/`**: Contains the application's main routes (`Login`, `Dashboard`, `Transaction`, `Category`, `Company`, `Goal`).
* **`components/`**: Independent and reusable UI components:
  * **Modals:** Creation/edit forms (`NewTransaction`, `NewGoal`), confirmation dialogs (`ConfirmModal`), and statistics (`AnalyticsModal`).
  * **Charts:** Standalone data visualization components.
  * **Layout & Navigation:** Responsive `NavMenu` for primary application navigation.
* **`contexts/`**: Global state management. The `AlertContext` centralizes success and error notifications across the application.
* **`services/`**: `Axios` configuration (`api.js`) with **Interceptors**. Automatically attaches the JWT token to request headers and redirects users to the login page when the session expires (HTTP 401/403).

### ⚙️ Backend (`Java 21` + `Spring Boot 3`)
The REST API follows a **Layered Architecture**:

* **Controllers:** RESTful controllers documented with Swagger, responsible for handling HTTP requests and delegating business logic.
* **Services:** Implements the core business logic, including goal progress calculations, expense aggregations, and duplication validation rules.
* **Repositories:** `Spring Data JPA` repositories with support for dynamic `Specifications` to enable advanced transaction and goal filtering, as well as optimized JPQL queries.
* **Models & DTOs:** Database entities (e.g., `UserProfile`, `Transaction`, `Goal`) are strictly separated from **Data Transfer Objects (DTOs)**. Object mapping is handled using **ModelMapper**.
* **Security:** Robust authentication and authorization managed by `SecurityFilter` and `TokenService` (using Auth0 JWT) to secure private endpoints.
* **Exception Handling:** `MyGlobalExceptionHandler` globally handles exceptions (such as `DataIntegrityViolationException`, validation failures, and *Not Found* errors) and returns standardized JSON responses (`APIResponse`).

---

## 🛠️ Technologies Used

### Frontend
* React 18
* React Router DOM
* Axios
* Recharts (Data Visualization)
* Pure CSS with CSS Variables and Backdrop Filters

### Backend
* Java 21
* Spring Boot 3 (Web, Data JPA, Security, Validation)
* PostgreSQL (Relational Database)
* Maven
* JWT (Auth0)
* ModelMapper
* Swagger / OpenAPI (springdoc-openapi)

---

## 🚀 Getting Started

### Prerequisites
* Node.js (v18+)
* Java 21 (JDK)
* Maven
* Docker & Docker Compose (for the database)

---

## 📸 Database Entity Structure

- **User:** The primary owner of all application resources.
- **Category:** Expense categories (e.g., Food, Transportation). Supports custom icons and colors.
- **Company:** Businesses or merchants (e.g., Walmart, Shell). Each company is associated with a category.
- **Transaction:** An expense record containing an amount, date, and a mandatory category (with an optional company association).
- **Goal:** A time-based spending limit associated with either a category or a company.

---
