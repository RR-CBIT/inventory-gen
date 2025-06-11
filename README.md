# 🛒 Inventory Management System (CRUD App)

A Spring Boot-based RESTful Inventory Management System for managing **Products**, **Categories**, and **Suppliers** with PostgreSQL as the database.

## 📦 Features

- Create, Read, Update, Delete (CRUD) operations for:
  - 🧴 Products
  - 🗂️ Categories
  - 🚚 Suppliers
- Uses DTOs for data abstraction
- ModelMapper for entity-DTO mapping
- PostgreSQL integration
- Modular service layer design
- Ready for expansion into a Microservices architecture

## 🧰 Tech Stack

| Layer         | Tech Stack                       |
|---------------|----------------------------------|
| Backend       | Java 17, Spring Boot             |
| ORM           | Spring Data JPA                  |
| Database      | PostgreSQL                       |
| Mapping       | ModelMapper                      |
| Build Tool    | Maven                            |
| API Docs      | Swagger / SpringDoc OpenAPI      |       |
| Dev Tools     | Spring Boot DevTools             |

## ⚙️ Run Locally

1. **Clone the repository:**

   ```bash
   git clone https://github.com/RR-CBIT/inventory-gen.git
   cd inventory-gen
2.	Update application.properties:
     ```bash
     spring.datasource.url=jdbc:postgresql://localhost:5432/inventorydb
     spring.datasource.username=yourusername
     spring.datasource.password=yourpassword
3.	Run using Maven:
       ```bash
        mvn spring-boot:run
4. Access the app:
  •	API Base URL: http://localhost:8080/api/
	•	Swagger UI : http://localhost:8080/swagger-ui.html
