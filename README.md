# TodoApp API

This project is a Todo REST API built with Java and Spring Boot.
It provides common endpoints to manage todos and other features such as pagination with filtering, sorting, and marking tasks as done/undone.

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

### GET /todos

Fetches an array of todos, with optional filters and pagination.

#### Query parameters

- `text` (optional): Filter Todos by text.
- `priority` (optional): Filter Todos by priority. Values can be: `LOW`, `MEDIUM`, `HIGH`.
- `done` (optional): Filter Todos by completion status (boolean).
- `page` (default: `1`): Page number for pagination.
- `size` (default: `10`): Number of Todos per page.
- `sort_by` (default: `TEXT`): Sort Todos by a specific field. Values can be: `TEXT`, `PRIORITY`, `DUE_DATE`.
- `order` (default: `ASC`): Sort order. Values can be: `ASC`, `DESC`.

#### Response

Status code `200` (OK).

```json
{
  "content": [
    {
      "id": 1,
      "text": "Buy groceries",
      "priority": "HIGH",
      "isDone": false,
      "dueDate": "2025-02-17T10:00:00"
    },
    ...
  ],
  "currentPage": 1,
  "totalPages": 3,
  "pageSize": 10,
  "totalItems": 25
}
```

### GET /todos/{id}

Fetches a specific todo by ID.

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

### GET /todos/metrics

Fetches metrics on the average completion times of todos measured in seconds.

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

### POST /todos

Creates a new todo item.
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

### PUT /todos/{id}

Updates an existing todo by ID.
All fields are optional. If a field is not provided, it will retain its current value.

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

### PUT /todos/{id}/done

Marks a todo as completed.

#### Response

Status code `204` (No Content) with no body.

### PUT /todos/{id}/undone

Marks a todo as not completed.

#### Response
Status code `204` (No Content) with no body.

### DELETE /todos/{id}

Deletes a todo by ID.

#### Response

Status code `204` (No Content) with no body.

---

## Data Storage

Data is stored in-memory using a Java List.
This means any todos saved while the application is running will be lost once the application is shut down.