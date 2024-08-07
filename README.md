# Job Microservice Project

## Overview

This project is a microservice architecture that includes a Job microservice, Company microservice, and Review microservice. These services communicate with each other using a variety of technologies, including Docker, OpenFeign, Eureka Client, Config Server, PostgreSQL, RabbitMQ, and Zipkin. The microservices are accessible through an API Gateway.

## Technologies Used

- **Docker**: For containerization of services.
- **Docker Compose**: To orchestrate the multi-container Docker applications.
- **Spring Boot**: Framework for creating standalone Java applications.
- **OpenFeign**: For inter-service communication.
- **Eureka Client**: For service discovery.
- **Config Server**: To manage external properties for applications across all environments.
- **PostgreSQL**: Relational database for data storage.
- **RabbitMQ**: Message broker for asynchronous communication.
- **Zipkin**: Distributed tracing system to gather timing data.

## Architecture

- **Job Service**: Manages job-related operations.
- **Company Service**: Manages company-related operations and communicates with the Job service.
- **Review Service**: Manages reviews and communicates with both Job and Company services.
- **API Gateway**: Central entry point for accessing the microservices.
- **Config Server**: Centralized configuration management.
- **Eureka Server**: Service registry for service discovery.
- **RabbitMQ**: For message brokering between services.
- **Zipkin**: For tracing and monitoring microservices.

## Setup

### Prerequisites

- Docker
- Docker Compose
- Java 11+
- Maven

### Running the Services

1. **Clone the Repository**

   ```sh
   git clone [<repository-url>](https://github.com/Alex1-ai/microserver-job-app-spring)
   cd microserver-job-app-spring
   ```

2. **Build the Services**

   ```sh
   mvn clean install
   ```

3. **Start the Services using Docker Compose**

   ```sh
   docker-compose up --build
   ```

### Accessing the Services

- **API Gateway**: `http://localhost:8084`
  - **Job Service**: `http://localhost:8084/jobs`
  - **Company Service**: `http://localhost:8084/companies`
  - **Review Service**: `http://localhost:8084/reviews`

### Configuration

Configuration properties are managed by the Config Server. Update the configuration in the `application.yml` files located in the `config-repo` directory.

### Example `docker-compose.yml`

```yaml
version: '3.8'

services:
  postgres:
    image: postgres
    environment:
      POSTGRES_USER: alexis
      POSTGRES_PASSWORD: alexis
      PGDATA: /data/postgres
    volumes:
      - postgres:/data/postgres
    ports:
      - "5432:5432"
    networks:
      - microservice-network

  pgadmin:
    image: dpage/pgadmin4
    environment:
      PGADMIN_DEFAULT_EMAIL: pgadmin4@pgadmin.org
      PGADMIN_DEFAULT_PASSWORD: admin
    volumes:
      - pgadmin:/var/lib/pgadmin
    ports:
      - "5050:80"
    networks:
      - microservice-network

  config-server:
    image: chidi123/config-server-ms:latest
    ports:
      - 8888:8888
    networks:
      - microservice-network

  eureka-server:
    image: chidi123/servicereg:latest
    ports:
      - 8761:8761
    networks:
      - microservice-network
    depends_on:
      - config-server

  job-service:
    image: chidi123/jobms:latest
    ports:
      - 8082:8082
    networks:
      - microservice-network
    depends_on:
      - eureka-server
      - config-server
      - postgres
    environment:
      SPRING_PROFILES_ACTIVE: docker

  company-service:
    image: chidi123/companyms:latest
    ports:
      - 8081:8081
    networks:
      - microservice-network
    depends_on:
      - eureka-server
      - config-server
      - postgres
    environment:
      SPRING_PROFILES_ACTIVE: docker

  review-service:
    image: chidi123/reviewms:latest
    ports:
      - 8083:8083
    networks:
      - microservice-network
    depends_on:
      - eureka-server
      - config-server
      - postgres
    environment:
      SPRING_PROFILES_ACTIVE: docker

  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - 5672:5672
      - 15672:15672
    networks:
      - microservice-network

  zipkin:
    image: openzipkin/zipkin
    ports:
      - 9412:9411
    networks:
      - microservice-network

  gateway:
    image: chidi123/gateway-ms:latest
    ports:
      - 8084:8084
    networks:
      - microservice-network
    depends_on:
      - eureka-server
      - config-server

networks:
  microservice-network:

volumes:
  postgres:
  pgadmin:
```

## Notes

- Ensure that the Config Server is running before starting other services.
- Check the logs for any errors and ensure that all services are registered with Eureka.
- Use RabbitMQ for message-driven communication between services.
- Zipkin provides tracing capabilities to monitor the interaction between microservices.

## Troubleshooting

- If you encounter any issues, check the logs of the individual services using Docker logs.
- Ensure that the database services are up and running, and accessible from the microservices.
- Verify the configuration in the `application.yml` files for correct database connection properties.

## Contributing

Feel free to contribute to this project by opening issues and submitting pull requests.

## License

This project is licensed under the MIT License.
