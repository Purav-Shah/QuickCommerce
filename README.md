# Quick Commerce - Microservices E-commerce Platform

Quick Commerce is a modern e-commerce platform built using microservices architecture. It provides a scalable and maintainable solution for online shopping with features like user management, product catalog, order processing, and payment handling.

## Architecture

The application is built using the following microservices:

1. **API Gateway** (Port: 8080)
   - Routes requests to appropriate microservices
   - Handles authentication and authorization
   - Provides Swagger API documentation
   - Swagger UI: http://localhost:8080/swagger-ui.html

2. **Eureka Server** (Port: 8761)
   - Service discovery and registration
   - Load balancing
   - Dashboard: http://localhost:8761

3. **Login Service** (Port: 8081)
   - User authentication and authorization
   - Role-based access control (USER/ADMIN)
   - Swagger UI: http://localhost:8081/swagger-ui.html

4. **Product Service** (Port: 8081)
   - Product catalog management
   - Product search and filtering
   - Swagger UI: http://localhost:8081/swagger-ui.html

5. **Inventory Service** (Port: 8083)
   - Stock management
   - Inventory tracking
   - Swagger UI: http://localhost:8083/swagger-ui.html

6. **Order Service** (Port: 8084)
   - Order processing
   - Order tracking
   - Swagger UI: http://localhost:8084/swagger-ui.html

7. **Payment Service** (Port: 8085)
   - Payment processing
   - Transaction management
   - Swagger UI: http://localhost:8085/swagger-ui.html

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- Spring Boot 3.x
- Spring Cloud 2023.x

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/QuickCommerce.git
   cd QuickCommerce
   ```

2. **Configure Database**
   - Create MySQL databases for each service:
     ```sql
     CREATE DATABASE login;
     CREATE DATABASE product_service;
     CREATE DATABASE quick_commerce;
     ```
   - Update application.properties files with your database credentials

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Start the services**
   ```bash
   # Start Eureka Server first
   cd Eureka-Server
   mvn spring:boot run

   # Start other services in separate terminals
   cd API-Gateway
   mvn spring:boot run

   cd LogIn
   mvn spring:boot run

   cd Product-Service
   mvn spring:boot run

   cd Inventory-Service
   mvn spring:boot run

   cd Order-Service
   mvn spring:boot run

   cd Payment-Service
   mvn spring:boot run
   ```

## API Documentation

### Swagger UI Access
Each service has its own Swagger UI for detailed API documentation:

1. **API Gateway**: http://localhost:8080/swagger-ui.html
2. **Login Service**: http://localhost:8081/swagger-ui.html
3. **Product Service**: http://localhost:8081/swagger-ui.html
4. **Inventory Service**: http://localhost:8083/swagger-ui.html
5. **Order Service**: http://localhost:8084/swagger-ui.html
6. **Payment Service**: http://localhost:8085/swagger-ui.html

### API Endpoints Documentation

#### Authentication Service
```
POST /api/auth/register
- Register a new user
- Body: {
    "username": "string",
    "password": "string",
    "role": "USER/ADMIN"
  }

POST /api/auth/login
- Login user
- Body: {
    "username": "string",
    "password": "string"
  }
```

#### Product Service
```
GET /api/products
- Get all products
- Query Parameters:
  - page (optional)
  - size (optional)
  - sort (optional)

POST /api/products
- Create new product (Admin only)
- Body: {
    "name": "string",
    "description": "string",
    "price": number,
    "category": "string",
    "stockQuantity": number
  }

GET /api/products/{id}
- Get product by ID

PUT /api/products/{id}
- Update product (Admin only)
- Body: Same as POST

DELETE /api/products/{id}
- Delete product (Admin only)
```

#### Order Service
```
POST /api/orders
- Create new order
- Body: {
    "userId": number,
    "items": [
      {
        "productId": number,
        "quantity": number
      }
    ]
  }

GET /api/orders/{id}
- Get order by ID

GET /api/orders/user/{userId}
- Get orders by user ID
```

#### Payment Service
```
POST /api/payments/process
- Process payment
- Body: {
    "orderId": number,
    "userId": number,
    "amount": number,
    "paymentMethod": "string"
  }

GET /api/payments/{paymentId}
- Get payment by ID
```

#### Inventory Service
```
GET /api/inventory/check
- Check product availability
- Query Parameters: productIds (comma-separated)

PUT /api/inventory/{productId}
- Update inventory
- Query Parameters: quantityChange
```

## Authentication

The application uses JWT-based authentication with role-based access control:

### User Roles
1. **ADMIN**
   - Full access to all endpoints
   - Can manage products, inventory, and users
   - Can view all orders and payments

2. **USER**
   - Access to user-specific endpoints
   - Can place orders and make payments
   - Can view their own orders and profile

### Authentication Flow
1. Register a new user
2. Login to get JWT token
3. Include token in Authorization header:
   ```
   Authorization: Bearer <your-token>
   ```

## Security

- JWT-based authentication
- Role-based access control
- Password encryption using BCrypt
- CORS enabled
- CSRF protection
- Rate limiting
- Input validation

## Development

### Adding New Features
1. Create a new branch
2. Implement the feature
3. Add unit tests
4. Update API documentation
5. Create pull request

### Testing
```bash
# Run all tests
mvn test

# Run specific service tests
cd <service-name>
mvn test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## Support

For support, email support@quickcommerce.com or create an issue in the repository. 