# JDBC CRUD Demo Hibernate

[🇮🇷 فارسی](./README.fa.md)


This project is a simple Java CRUD application built with JPA and Hibernate ORM to demonstrate how database operations can be performed using object-relational mapping instead of plain JDBC.
---
It includes a basic Person entity and provides examples of how to:

- Configure Hibernate via persistence.xml

- Perform insert, update, delete, and select operations with EntityManager

- Use the @Version annotation for optimistic locking, preventing concurrent update conflicts

- Handle common Hibernate configuration issues such as JAXBException in modern Java versions

This project is intended as a learning example to compare plain JDBC implementation with Hibernate-based ORM.

## 🛠️ Technologies Used
- Java 8

- Hibernate ORM (JPA)

- H2 Database (in-memory)

