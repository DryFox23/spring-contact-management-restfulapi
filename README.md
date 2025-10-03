# Spring Contact Management RESTful API

A RESTful API built with Spring Boot for managing contacts. This project demonstrates CRUD operations, integration with SQL databases, and follows best practices for building scalable Java backend applications.

## Features

- Create, read, update, and delete contacts
- RESTful endpoints
- SQL database integration
- Error handling
- Maven build system

## Technologies Used

- Java
- Spring Boot
- Maven
- SQL (e.g., MySQL, PostgreSQL)
- IntelliJ IDEA

## Getting Started

### Prerequisites

- Java 17+
- Maven
- SQL database (e.g., MySQL, PostgreSQL)

### Setup

1. Clone the repository:
2. Configure your database settings in `src/main/resources/application.properties`.
3. Build the project:
4. Run the application:

## API Endpoints

- `GET /contacts` - List all contacts
- `GET /contacts/{id}` - Get contact by ID
- `POST /contacts` - Create a new contact
- `PUT /contacts/{id}` - Update a contact
- `DELETE /contacts/{id}` - Delete a contact

## Usage

Use tools like Postman or curl to interact with the API.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License.
