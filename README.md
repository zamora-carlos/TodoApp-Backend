# TodoApp API

This project is a Todo REST API built with Java and Spring Boot.
It provides common endpoints to manage to-dos and other features such as pagination with filtering, sorting, and marking tasks as done/undone.

---

## Technologies

- **Java**: Primary language.
- **Maven**: Build tool.
- **Spring Boot**: Framework for building and running the REST API.

---

## Getting started

### Prerequisites

Make sure you have Maven installed and Java JDK 21

### Setup

- **Clone the repository**:
  ```bash
  git clone https://github.com/zamora-carlos/TodoApp-Backend.git
  ```

- **Navigate to project folder**:
  ```bash
  cd TodoApp-Backend
  ```

- **Install dependencies and build the application**:
  ```bash
  mvn clean install
  ```

### Running the project

#### Commands

- **Start the application**:

  The following command will start the application at http://localhost:9090.

  ```bash
  mvn spring-boot:run
  ```

- **Test the application**:

  ```bash
  mvn test
  ```
  
---

## API Endpoints

Once the application is running, you can view the full documentation, along with the request and response schemas, in the Swagger UI at http://localhost:9090/swagger-ui/index.html.
From there, you can also interact with the API and try out the endpoints directly.

### GET `/api/v1/todos`

Fetches an array of to-dos, with optional filters and pagination.

#### Query parameters

- `text` (optional): Filter to-dos by text.
- `priority` (optional): Filter to-dos by priority. Values can be: `LOW`, `MEDIUM`, `HIGH`.
- `done` (optional): Filter to-dos by completion status (boolean).
- `page` (default: `1`): Page number for pagination.
- `size` (default: `10`): Number of to-dos per page.
- `sort_by` (default: `TEXT`): Sort to-dos by a specific field. Values can be: `TEXT`, `PRIORITY`, `DUE_DATE`.
- `order` (default: `ASC`): Sort order. Values can be: `ASC`, `DESC`.

#### Response

Status code `200` (OK).

```json
{
  "content": [
    {
      "id": 21,
      "text": "Buy groceries",
      "priority": "HIGH",
      "isDone": false,
      "dueDate": "2025-02-17T10:00:00"
    },
    {
      "id": 22,
      "text": "Clean the dishes",
      "priority": "LOW",
      "isDone": true,
      "dueDate": "2025-02-15T10:25:00"
    }
  ],
  "currentPage": 3,
  "totalPages": 3,
  "pageSize": 10,
  "totalItems": 22
}
```

### GET `/api/v1/todos/{id}`

Fetches a specific to-do by ID.

#### Response

Status code `200` (OK).

```json
{
  "id": 1,
  "text": "Buy groceries",
  "priority": "HIGH",
  "isDone": false,
  "dueDate": "2025-02-17T10:00:00"
}
```

### GET `/api/v1/todos/metrics`

Fetches metrics for the average completion times of to-dos, measured in seconds.

#### Response

Status code `200` (OK).

```json
{
  "avgTime": 10000,
  "avgTimeLow": 8000,
  "avgTimeMedium": 12000,
  "avgTimeHigh": 15000
}
```

### POST `/api/v1/todos`

Creates a new to-do item.
The `text` and `priority` fields are required, while the `dueDate` field can be `null`.

#### Request body

```json
{
  "text": "Buy groceries",
  "priority": "HIGH",
  "dueDate": "2025-02-17T10:00:00"
}
```

#### Response

Status code `201` (Created).

```json
{
  "id": 1,
  "text": "Buy groceries",
  "priority": "HIGH",
  "isDone": false,
  "dueDate": "2025-02-17T10:00:00"
}
```

### PUT `/api/v1/todos/{id}`

Updates an existing to-do by ID.
All fields are optional. If a field is not provided, it will retain its current value.
If the `dueDate` is set to null, the to-do's due date will be removed.

#### Request body

```json
{
    "text": "Buy groceries and milk",
    "priority": "MEDIUM",
    "dueDate": "2025-02-18T10:00:00"
}
```

#### Response

Status code `200` (OK).

```json
{
    "id": 1,
    "text": "Buy groceries and milk",
    "priority": "MEDIUM",
    "isDone": false,
    "dueDate": "2025-02-18T10:00:00"
}
```

### PUT `/api/v1/todos/{id}/done`

Marks a to-do as completed.

#### Response

Status code `204` (No Content) with no response body.

### PUT `/api/v1/todos/{id}/undone`

Marks a to-do as not completed.

#### Response
Status code `204` (No Content) with no response body.

### DELETE `/api/v1/todos/{id}`

Deletes a to-do by ID.

#### Response

Status code `204` (No Content) with no response body.

---

## Error handling

In case of invalid input or errors, the API returns appropriate HTTP status codes and error messages.

### 400 Bad Request

If the request body is invalid or missing required fields.

<details>

<summary>See examples</summary>

```json
{
  "code": 400,
  "message": "Query parameter 'id' could not be converted to 'Long' type.",
  "timestamp": "2025-02-16T23:29:38.672545"
}
```

```json
{
  "code": 400,
  "message": "Malformed JSON request body or invalid data format.",
  "timestamp": "2025-02-16T23:30:33.501053"
}
```

```json

{
  "code": 400,
  "message": "Validation failed for one or more fields.",
  "timestamp": "2025-02-16T23:31:15.311695",
  "errors": [
    {
      "field": "priority",
      "message": "Priority is required.",
      "rejectedValue": null
    },
    {
      "field": "dueDate",
      "message": "Due date must be either today or in the future.",
      "rejectedValue": "2024-10-17T04:11:39.564"
    },
    {
      "field": "text",
      "message": "Text should be between 3 and 120 characters.",
      "rejectedValue": "hi"
    }
  ]
}
```
</details>

### 404 Not Found

If a to-do with the specified ID is not found.

```json
{
  "code": 404,
  "message": "The todo with id 1 was not found.",
  "timestamp": "2025-02-16T23:29:22.154758"
}
```

### 500 Internal Server Error

For server-side issues and other unhandled exceptions.

```json
{
  "code": 500,
  "message": "An unexpected error occurred.",
  "timestamp": "2025-02-16T23:30:11.506533"
}
```

---

## Data Storage

Data is stored in-memory using a Java List.
This means any to-dos saved while the application is running will be lost once the application is shut down.