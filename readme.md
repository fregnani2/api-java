# Transaction API
## Overview
This API is used to manage transactions, it is possible to create clients, accounts and transactions. The API is RESTful and all responses are in JSON format.


## Getting Started
### Prerequisites
- Java 17

### Step 1: Configure IntelliJ IDEA
1. Open IntelliJ IDEA.
2. Click on `File` -> `New` -> `Project from Version Control`.
3. Enter the URL of this repository and click `Clone`.

### Step 2: Install Dependencies
1. Open the terminal and navigate to the project directory.
2. Run the following command to install the dependencies:
```shell
./mvnw clean install
```
## Base URL
The base URL for all requests is `http://localhost:8080/clients`

## Endpoints
### Create a client
#### Request
```http
POST /clients
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
GET /clients
```

### Get a client by account number
#### Request
```http
GET /clients/{accountNumber}
```

### Make a transaction
#### Request
```http
POST /clients/transfer
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
GET /clients/transactions/{accountNumber}
```

## Insonmia Collection
You can import the Insomnia collection [here](https://www.google.com/) to test the API.

