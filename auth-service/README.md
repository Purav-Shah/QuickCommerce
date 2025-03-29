# Auth Service Documentation

## Overview
The Auth Service is responsible for handling user authentication and authorization in the aviation ticketing system. It provides JWT (JSON Web Token) based authentication and integrates with the User Service for user management.

## Core Components

### 1. UserDetailsServiceImpl
- Implements Spring Security's `UserDetailsService`
- Communicates with User Service to load user details
- Converts User data into Spring Security compatible UserDetails

### 2. JwtService
- Handles JWT token generation and management
- Features:
    - Token generation with email and role claims
    - RSA private key-based token signing
    - 30-minute token expiration
    - Secure key handling using environment variables

### 3. UserServiceCommunication (Feign Client)
- Interfaces with the User Service
- Endpoints:
    - GET `/api/user/email/{email}` - Retrieve user by email
    - POST `/api/user` - Create new user

## Dependencies
- Spring Boot Starter Security
- Spring Boot Starter Web
- Spring Cloud OpenFeign
- Spring Cloud Netflix Eureka Client
- JWT (jsonwebtoken) libraries:
    - jjwt-api
    - jjwt-impl
    - jjwt-jackson

## Security Features
- Implements Spring Security for authentication
- Uses JWT tokens for stateless authentication
- RSA-based token signing for enhanced security
- Role-based access control
- Integration with User Service for credential verification

## Configuration
- Requires JWT_PRIVATE_SECRET environment variable for token signing
- Configured as Eureka client for service discovery
- Uses Feign for inter-service communication

## Error Handling
- Handles authentication failures
- Manages user not found scenarios
- Provides secure error responses
# Spring Boot

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.3/maven-plugin/build-image.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.4.3/reference/using/devtools.html)
* [Spring Security](https://docs.spring.io/spring-boot/3.4.3/reference/web/spring-security.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.3/reference/web/servlet.html)
* [OpenFeign](https://docs.spring.io/spring-cloud-openfeign/reference/)

### Guides
The following guides illustrate how to use some features concretely:

* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Additional Links
These additional references should also help you:

* [Declarative REST calls with Spring Cloud OpenFeign sample](https://github.com/spring-cloud-samples/feign-eureka)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

