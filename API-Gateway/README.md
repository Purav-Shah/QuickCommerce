# API Gateway Documentation

## Overview
The API Gateway serves as the single entry point for all client requests in the aviation ticketing system. It handles routing, security, authentication, and CORS configuration while providing a unified interface to all microservices.

## Core Components

### 1. Gateway Configuration
- Routes traffic to appropriate microservices
- Services routed:
    - Airport Service (`/airport-service/**`)
    - Auth Service (`/auth-service/**`)
    - Flight Service (`/flight-service/**`)
    - User Service (`/user-service/**`)
    - Schedule Service (`/schedule-service/**`)

### 2. Security Configuration
- Implements Spring Security for request filtering
- JWT-based authentication
- Role-based access control (RBAC)
- CORS configuration for cross-origin requests
- Endpoints security:
    - Public endpoints (no authentication required)
    - User endpoints (authentication required)
    - Admin endpoints (admin role required)

### 3. JWT Authentication
Components:
- `JwtAuthenticationFilter`: Validates tokens and sets security context
- `JwtService`: Handles token validation and user details extraction
- `JwtAuthenticationEntryPoint`: Handles unauthorized access

## Security Features

### Authentication
- Bearer token validation
- RSA public key verification
- Token expiration checking
- User details extraction from tokens

### Authorization
Public Endpoints:
- POST `/auth-service/api/login`
- POST `/auth-service/api/register`
- GET `/flight-service/api/flights`
- GET `/airport-service/api/airports`
- GET `/schedule-service/api/schedules`

Admin-Only Endpoints:
- User management operations
- Flight management operations
- Airport management operations
- Schedule management operations

### CORS Configuration
- Allows specified origins
- Supports credentials
- Configurable allowed methods and headers
- Pre-flight request handling
- Exposed headers configuration

## Dependencies
- Spring Cloud Gateway
- Spring Security
- Spring Cloud Netflix Eureka Client
- Spring Cloud OpenFeign
- JWT Libraries:
    - jjwt-api
    - jjwt-impl
    - jjwt-jackson

## Configuration

### Application Properties
- Server port: 8080
- Spring application name: "api-gateway"
- Eureka client configuration
- JWT public key configuration
- CORS settings

### Route Configuration
- Load balanced routing
- Path-based routing
- Strip prefix configuration
- Service discovery integration

## Error Handling
- Authentication failures
- Token validation errors
- Route not found
- Service communication errors

## Integration Points
- Eureka Server for service discovery
- All microservices through defined routes
- Authentication service for token validation

## Best Practices
1. All client requests must go through the gateway
2. Use HTTPS in production
3. Implement rate limiting
4. Monitor gateway performance
5. Implement circuit breakers for service calls
# Spring Boot

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.3/maven-plugin/build-image.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.4.3/reference/using/devtools.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.3/reference/web/servlet.html)
* [OpenFeign](https://docs.spring.io/spring-cloud-openfeign/reference/)
* [Eureka Discovery Client](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html#_service_discovery_eureka_clients)
* [Reactive Gateway](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Service Registration and Discovery with Eureka and Spring Cloud](https://spring.io/guides/gs/service-registration-and-discovery/)
* [Using Spring Cloud Gateway](https://github.com/spring-cloud-samples/spring-cloud-gateway-sample)

### Additional Links
These additional references should also help you:

* [Declarative REST calls with Spring Cloud OpenFeign sample](https://github.com/spring-cloud-samples/feign-eureka)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

