#ShopSphere Backend
Overview

ShopSphere Backend is a microservices-based backend application for an e-commerce platform. It handles core functionalities such as user management, product catalog, orders, payments, and more.

This project is containerized using Docker, and a docker-compose.yml file is provided to run all services together.

High-Level Architecture

For a visual representation and high-level overview of the system, refer to the diagram below:
https://github.com/user-attachments/assets/81b8abf7-7466-4acb-ab99-211472006693


Backend Demonstration

A demonstration of the backend APIs and workflows is available here:
https://github.com/user-attachments/assets/2aa03360-6077-4601-9079-f9f9fe4739c2

Environment Variables
Variable	Description
StripeKey	sk_test_51S97PXQ9kTJpAJ41FQ3nqFrupHfpsmLr5r0Gr3ORxXkDSJOH0VVrBtrNquAPffmDOi7WlDB0WzYiHcZTTsfJANiJ009f7MDH0g

Database Setup
 MySQL is not dockerized in this setup. You have two options:
      1. Use a MySQL container:
            Update docker-compose.yml to include a MySQL service.
            Configure other services to connect to this container.
      2. Use MySQL on host machine:
            Create necessary databases and users on your host MySQL server based on each microservice's entity class
            Update service configurations to connect to the host database.

Running the Application

  1. Clone the repository.
  2. Run the services using Docker Compose:
        docker-compose up --build
  3. Verify services are running and accessible via their exposed ports.


ShopSphere-Backend/
│
├─ docker-compose.yml 
├─ API Gateway
├─ EurekaServer
├─ ProductService
├─ UserService
├─ OrderService
├─ PaymentService
├─ NotificationService
└─ README.md


Notes
  Ensure ports required by each service are free on your host machine.

Contribution
  Contributions are welcome! Please create issues for bugs or feature requests and submit pull requests for code improvements.
