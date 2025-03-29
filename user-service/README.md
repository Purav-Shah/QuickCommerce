# User Service

## Overview
The User Service manages user-related operations in the aviation ticketing system. It provides user management functionality including creation, retrieval, updates, and deletion of user accounts.

## Core Components

### 1. UserService Interface
- Defines core user management operations
- Methods:
    - `createUser(UserRequest)` - Create new user
    - `getUserByEmail(String)` - Retrieve user by email
    - `getUserById(int)` - Get user by ID
    - `getAllUsers()` - Get all users
    - `updateUser(UserRequest)` - Update user details
    - `deleteUserById(int)` - Delete specific user
    - `deleteAllUsers()` - Delete all users

### 2. UserServiceImpl
- Implements UserService interface
- Features:
    - User CRUD operations
    - Email-based user lookup
    - Data validation
    - User mapping between DTOs and entities

## Data Model
User entity includes:
- User ID
- Email
- Password (securely stored)
- Role
- Other user details

## Dependencies
- Spring Boot Starter Web
- Spring Data JPA
- Lombok
- Spring Cloud Netflix Eureka Client

## Features
- Complete user management system
- Email-based user lookup
- Role-based user management
- Data validation and error handling
- Service discovery integration
- DTO pattern for data transfer

## Error Handling
- UserNotFoundException
- UserAlreadyExistsException
- NoUsersFoundException
- Validation errors

## Security Features
- Password security
- Role-based access control
- Email validation
- Unique email enforcement

## Integration Points
- Auth Service for authentication
- Service Registry for service discovery
- Database for user data persistence

## Configuration
- Database configuration
- Eureka client configuration
- Exception handling setup
- Mapper configuration 