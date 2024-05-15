# Transaction API
## Overview
This API is used to manage transactions, it is possible to create clients and make transactions.
It uses Spring Boot, Spring Data JPA, H2 database and Lombok.


## Getting Started
### Prerequisites
- Java JDK 17

### Step-by-step guide
1. Clone the repository.
2. Import the project into your favorite IDE.
3. Install the maven dependencies.
4. Run the project.

## Base URL
The base URL for all requests is `http://localhost:8080/clients`

## V1 Endpoints
### Create a client
#### Request
```http
POST /clients/v1
```
```json
{
    "name": "John Doe",
    "accountNumber": "123",
    "balance": 1000.0
}
```
### Get all clients
#### Request
```http
GET /clients/v1
```

### Get a client by account number
#### Request
```http
GET /clients/v1/{accountNumber}
```

### Make a transaction
#### Request
```http
POST /clients/v1/transfer
```
```json
{
  "fromAccountNumber": 123,
  "toAccountNumber": 220,
  "value": 50
}
```
### Get Transaction by account number
#### Request
```http
GET /clients/v1/transactions/{accountNumber}
```

## V2 Endpoints
### Update a client
#### Request
```http
PUT /clients/v2/{id}
```
```json
{
    "name": "John Doe",
    "accountNumber": "123",
    "balance": 1000.0
}
```

### Get All Transactions
#### Request
```http
GET /clients/v2/transactions?fromAccountNumber=210&page=0&size=10
```
## Insomnia Collection
You can download the Insomnia collection [here](https://github.com/fregnani2/api-java-test/blob/main/collection.json) to test the API.

