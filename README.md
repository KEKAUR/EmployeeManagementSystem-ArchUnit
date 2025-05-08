# EmployeeManagementSystem-ArchUnit

This project is an Employee Management System built using Spring Boot, providing CRUD operations for managing employee data. It includes features like adding, updating, retrieving, and deleting employee records via RESTful APIs. The application integrates with a relational database using Spring Data JPA.

## Architectural Testing with ArchUnit:

Purpose: Ensures compliance with clean architecture principles.
Enforced Rules:
Controllers depend only on services.
Services interact only with repositories.
No cyclic dependencies between packages.
Utility classes have no public constructors.
