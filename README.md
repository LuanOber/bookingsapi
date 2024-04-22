# Hotel Booking API


## Description

This repository contains a REST API built with Java Spring Boot, running within a docker container and using an in-memory H2 database.


## System Requirements
 
### For Java Spring Boot:

1. **Java Development Kit (JDK)**: Ensure you have JDK 17 or later installed on your system.

## Running

To run the API, navigate to the root directory in your terminal and run the following commands:

```bash
mvn clean
```
```bash
mvn install
```
to install the jar files
```bash
docker build -t bookingsapi .  
```
```bash
docker run -p 8080:8080 bookingsapi
```

## Calling the API

### GET all bookings

```bash
http://localhost:8080/api/v1/bookings
```

### GET specific booking
Path Variable:
1. id (number)

```bash
http://localhost:8080/api/v1/bookings/{id}
```

### GET all bookings for an agency
To account for an agency that can have several hundred bookings, pagination is implemented in the API. 

Path Variable:
1. id (number)

Parameters:
1. page
2. size

```bash
http://localhost:8080/api/v1/bookings/agency/{id}?page={page}&size={size}
```

### POST
Request body:
1. agencyId (number) [required]
2. hotelId (number) [required]
3. checkIn (string) [required]
4. checkOut (string) [required]
5. customerId (number) [required]
6. price (number) [required]

```bash
http://localhost:8080/api/v1/bookings
```

Example JSON Request Body\
{
"agencyId": 4,
"hotelId": 5,
"checkIn": "2024-04-25",
"checkOut": "2024-04-30",
"customerId": 223,
"price": 420
}

### DELETE
Path Variable:
1. id (number)

```bash
http://localhost:8080/api/v1/bookings/{id}
```

### PUT
Update an existing booking \
\
Path Variable:
1. id (number)

Request body:
1. agencyId (number) [required]
2. hotelId (number) [required]
3. checkIn (string) [required]
4. checkOut (string) [required]
5. customerId (number) [required]
6. price (number) [required]

```bash
http://localhost:8080/api/v1/bookings/{id}
```

Example JSON Request Body\
{
"agencyId" : 4,
"hotelId": 5,
"checkIn": "2024-04-25",
"checkOut": "2024-04-30",
"customerId": 225,
"price": 420
}

### PATCH
Update an existing booking with specific values \
\
Path Variable:
1. id (number)

Request body:
1. agencyId (number) [not required]
2. hotelId (number) [not required]
3. checkIn (string) [not required]
4. checkOut (string) [not required]
5. customerId (number) [not required]
6. price (number) [not required]

```bash
http://localhost:8080/api/v1/bookings/{id}
```

Example JSON Request Body\
{
"agencyId" : 4,
"hotelId": 5,
"checkIn": "2024-04-25",
"checkOut": "2024-04-30",
"customerId": 225,
"price": 420
}

OR

{
"checkOut": "2024-05-02",
"price": 600
}
