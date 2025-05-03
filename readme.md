# Remilty API – Spring Boot + PostgreSQL + Docker

A REST API project for managing financial institution data (e.g., SWIFT codes). The application is built with Spring Boot 3.5, Java 21, PostgreSQL, and fully containerized using Docker.

## 🧱 Technologies Used

* Java 21
* Spring Boot 3.5
* Spring Data JPA
* Hibernate
* PostgreSQL 15
* Maven
* Docker & Docker Compose
* Springdoc OpenAPI (Swagger UI)

## 🚀 How to Run This Project

### Prerequisites

Make sure the following are installed on your system:

* Docker
* Docker Compose
* Git

### Steps to Run

1. **Clone the repository:**

   ```bash
   git clone https://github.com/WoyTuloO/swift-code-rest-api.git
   cd swift-code-rest-api
   ```

2. **Start the application with Docker Compose:**
   This command will build the application JAR inside a Docker container, start the PostgreSQL service, and run the Spring Boot application.

   ```bash
   docker compose up --build
   ```
   The building might take approx. 2 minutes.
3. **Access the application:**

    * API: [http://localhost:8080](http://localhost:8080)
    * Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 🛠 Project Structure

```
remilty/
├── Dockerfile
├── docker-compose.yml
├── wait-for-it.sh
├── pom.xml
└── src/
```

## 📦 API Endpoints

### GET `/v1/swift-codes/{swift-code}`

Retrieve details of a specific SWIFT code, either for a headquarter or a branch.

**Response (Headquarter):**

```json
{
  "address": "string",
  "bankName": "string",
  "countryISO2": "string",
  "countryName": "string",
  "isHeadquarter": true,
  "swiftCode": "string",
  "branches": [
    {
      "address": "string",
      "bankName": "string",
      "countryISO2": "string",
      "isHeadquarter": false,
      "swiftCode": "string"
    }
  ]
}
```

**Response (Branch):**

```json
{
  "address": "string",
  "bankName": "string",
  "countryISO2": "string",
  "countryName": "string",
  "isHeadquarter": false,
  "swiftCode": "string"
}
```

### GET `/v1/swift-codes/country/{countryISO2code}`

Return all SWIFT codes for a specific country (both headquarters and branches).

**Response:**

```json
{
  "countryISO2": "string",
  "countryName": "string",
  "swiftCodes": [
    {
      "address": "string",
      "bankName": "string",
      "countryISO2": "string",
      "isHeadquarter": true,
      "swiftCode": "string"
    },
    {
      "address": "string",
      "bankName": "string",
      "countryISO2": "string",
      "isHeadquarter": false,
      "swiftCode": "string"
    }
  ]
}
```

### POST `/v1/swift-codes`

Adds a new SWIFT code record.

**Request:**

```json
{
  "address": "string",
  "bankName": "string",
  "countryISO2": "string",
  "countryName": "string",
  "isHeadquarter": true,
  "swiftCode": "string"
}
```

**Response:**

```json
{
  "message": "string"
}
```

### DELETE `/v1/swift-codes/{swift-code}`

Deletes a SWIFT code record that matches the given SWIFT code.

**Response:**

```json
{
  "message": "string"
}
```

## 🧪 Testing

Swagger UI is available for testing all endpoints:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 🗃️ Database Configuration

The application uses a PostgreSQL container with the following credentials:

* Host: `postgres`
* Port: `5432`
* Username: `user`
* Password: `password`
* Database: `postgres`

All data is persisted in a Docker volume named `postgres_data`.
