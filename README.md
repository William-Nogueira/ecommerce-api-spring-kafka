# Ecommerce API

This is a Java-based ecommerce API project built with Spring Boot and containerized using Docker. The project provides a RESTful API for managing ecommerce-related data, including products, customers, orders, and payments. It leverages Postgres for data persistence and Kafka for event-driven communication.

## Getting Started

To run the API along with its dependencies (Postgres and Kafka), use the provided `docker-compose.yml` file. In the project’s root directory, run:

```bash
docker compose up
```

This will start the API on the port :8081, as well as the required Postgres and Kafka containers.

## API Documentation

The API documentation can be found at [localhost:8081/swagger-ui.html](http://localhost:8080/swagger-ui.html). This page provides a interactive interface for exploring the API endpoints and testing requests.

## Best Practices

This project follows best practices for software development, including:

* **RESTful API Design**: The API follows standard RESTful principles, including the use of HTTP verbs (GET, POST, PUT, DELETE), resource-based URLs, standard HTTP status codes and proper error handling.
* **Spring Actuator**: The project uses Spring Actuator to provide production-ready features such as monitoring, metrics, and auditing.
* **Separation of Concerns**: The API is divided into separate modules for each domain entity, making it easy to maintain and extend.
* **Test-Driven Development**: The project includes a comprehensive set of unit tests and integration tests to ensure the API is reliable and stable.
* **Code Quality**: The code is written in a clean, readable, and maintainable style, following standard Java coding conventions.
* **Scalability**: The API is designed to scale horizontally, making it easy to add more instances as traffic increases.

## Dependencies

The project uses the following dependencies:

* Java 17
* Spring Boot 3.4.1
* Postgres 14
* Spring Kafka 3.3.1
* Mapstruct 1.5.3.Final
* Flyway 10.20.1
* Lombok 1.18.36
* Springdoc 2.8.5
* Docker 3.8

## Contributing

Contributions are welcome! If you'd like to contribute to the project, please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License. See the LICENSE file for details.
