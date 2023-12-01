# End-to-End Testing

End-to-End testing was manually done via Postman. The following checklist outlines creation of a client, creation of both valid and invalid reservations, and returning various data.

## 1. Client Creation

`POST http://localhost:8080/api/v1/reservation/createClient`

```json
{
  "customValues": {
    "partySize": "INTEGER",
    "birthday": "BOOLEAN"
  },
  "startTime": "09:00:00",
  "endTime": "21:00:00",
  "slotLength": 30,
  "reservationsPerSlot": 2
}
```

Returns: 200 OK and client ID

```
1
```

`GET http://localhost:8080/api/v1/reservation/getClient?clientId=1`

Returns: 200 OK and client information

```json
{
  "clientId": 1,
  "startTime": "09:00:00",
  "endTime": "21:00:00",
  "slotLength": 30,
  "reservationsPerSlot": 2
}
```

## Invalid Single Reservation Creations

Start time before client range

`POST http://localhost:8080/api/v1/reservation`

```json
{
  "clientId": 1,
  "userId": 1,
  "startTime": "2023-11-29T01:00:00",
  "numSlots": 2,
  "customValues": {
    "partySize": 4,
    "birthday": true
  }
}
```

Returns: 500 Internal Server Error

```json
{
  "timestamp": "2023-11-30T22:41:55.755+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Reservation time is not within client's time range",
  "path": "/api/v1/reservation"
}
```

Start time is an invlaid slot multiple

`POST http://localhost:8080/api/v1/reservation`

```json
{
  "clientId": 1,
  "userId": 1,
  "startTime": "2023-11-29T14:45:00",
  "numSlots": 2,
  "customValues": {
    "partySize": 4,
    "birthday": true
  }
}
```

Returns: 500 Internal Server Error

```json
{
  "timestamp": "2023-11-30T22:44:52.750+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Reservation start time is not a multiple of slot length",
  "path": "/api/v1/reservation"
}
```

## Valid Single Reservation Creation

`POST http://localhost:8080/api/v1/reservation`

```json
{
  "clientId": 1,
  "userId": 1,
  "startTime": "2023-11-29T14:00:00",
  "numSlots": 2,
  "customValues": {
    "partySize": 4,
    "birthday": true
  }
}
```

Returns: 200 OK

## Multiple Reservation Creations

Creation of a second reservation for the same time slot is okay since the client specified 2 reservations per slot.

`POST http://localhost:8080/api/v1/reservation`

```json
{
  "clientId": 1,
  "userId": 1,
  "startTime": "2023-11-29T14:00:00",
  "numSlots": 2,
  "customValues": {
    "partySize": 4,
    "birthday": true
  }
}
```

Returns: 200 OK

Creation of a third reservation for the same time slot is returns an error since the client specified 2 reservations per slot.

`POST http://localhost:8080/api/v1/reservation`

```json
{
  "clientId": 1,
  "userId": 1,
  "startTime": "2023-11-29T14:00:00",
  "numSlots": 2,
  "customValues": {
    "partySize": 4,
    "birthday": true
  }
}
```

Returns: 500 Internal Server Error

```json
{
  "timestamp": "2023-12-01T00:28:27.462+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Reservation time slots are fully booked",
  "path": "/api/v1/reservation"
}
```

## Return All Reservations for a Client

`GET http://localhost:8080/api/v1/reservation/getClientReservations?clientId=1`

```json
[
  {
    "birthday": "true",
    "numSlots": "2",
    "clientId": "1",
    "reservationId": "1",
    "startTime": "2023-11-29T14:00",
    "partySize": "4",
    "userId": "1"
  },
  {
    "birthday": "true",
    "numSlots": "2",
    "clientId": "1",
    "reservationId": "2",
    "startTime": "2023-11-29T14:00",
    "partySize": "4",
    "userId": "1"
  }
]
```

## Creation of Additional Clients

Multiple clients are differentiated.

`POST http://localhost:8080/api/v1/reservation/createClient`

```json
{
  "customValues": {
    "doctorId": "INTEGER",
    "patientNotes": "VARCHAR"
  },
  "startTime": "06:00:00",
  "endTime": "18:00:00",
  "slotLength": 60,
  "reservationsPerSlot": 1
}
```

Returns: 200 OK and client ID

```
2
```

New client can be seen.

`GET http://localhost:8080/api/v1/reservation/getClient?clientId=2`

Returns: 200 OK and client information

```json
{
  "clientId": 2,
  "startTime": "06:00:00",
  "endTime": "18:00:00",
  "slotLength": 60,
  "reservationsPerSlot": 1
}
```

`POST http://localhost:8080/api/v1/reservation`

```json
{
  "clientId": 2,
  "userId": 1,
  "startTime": "2023-11-29T08:00:00",
  "numSlots": 1,
  "customValues": {
    "doctorId": 1,
    "patientNotes": "diabetes"
  }
}
```

Returns: 200 OK

Returns only client 2's reservations.

`GET http://localhost:8080/api/v1/reservation/getClientReservations?clientId=1`

```json
[
  {
    "numSlots": "1",
    "patientNotes": "diabetes",
    "clientId": "2",
    "reservationId": "3",
    "doctorId": "1",
    "startTime": "2023-11-29T08:00",
    "userId": "1"
  }
]
```
