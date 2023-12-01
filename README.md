# BugYourSpot 🐛🐞🪲

A reservation booking service, made with <3 by BugBusters.\
Faustina Cheng, Youngseo Lee, Peter Ma, Shreya Somayajula, & Patrick Tong

# Client Application

See [Cricket Care](https://github.com/faustinacheng/CricketCare), a demoable client that uses our BugYourSpot service.

# Components:

API Description and Workflow:

BugYourSpot's structure derives from Spring Boot Flow Architecture examples, which provide several layers of abstraction between the client and the service itself. The client, which is any business that would need to employ some reservation schedule—restaurants, hospitals, the DMV, and more—can make use of several API endpoints to send certain requests. The parameterized information that the client sends is then parsed and gradually propagated throughout the various layers, with various elements of error checking occurring at different levels.

The ReservationController class acts as the first level of interaction with the client, using `@PostMapping`, `@GetMapping`, `@DeleteMapping`, and `@GetMapping` annotations to define several endpoints that the client will connect to. The first endpoint that businesses will utilize is the `createReservationSchema`, which allows the business to define custom fields that are relevant to their unique customers. For instance, restaurants may elect for a field such as "occasion" to indicate something like an anniversary or a birthday celebration. On the other hand, hospitals may designate a field called "doctor" to store which doctor each reservation will be meeting with. The custom schemas are managed on the backend through an Entity-Attribute Value Model; this structure makes use of several individual tables, linking them together through primary keys and allowing for easy field customizability across different clients. Here is a preliminary diagram of our database.

The ReservationService class acts as the next layer of logic, providing the method definitions for functions such as `addNewReservation()`, `deleteReservation()`, and `updateReservation()`. The class initializes an instance of a `ReservationRepository` object, which represents the lowest layer connected to the actual database. Through this object, the ReservationService class implements certain exception handling cases such as when the client / business attempts to delete a non-existent reservation.

# API Documentation

`POST /reservations/createClient`\
Description: The client provides certain fields that they want to represent a reservation with. This is in the form of a JSON object. There are a set of required reservation parameters they have to specify (startTime, endTime, slotLength, reservationsPerSlot) as well as a customValues JSON they can pass in for custom fields they want to include for their reservations. The service validates these fields to ensure that it matches the expected format. Once validated, our service will dynamically generate the database schema based on the custom fields and their data types. Thus, this API should be called just once and before all other API calls.\
Request Body: JSON object that represents the fields of a reservation\
Example Usage: `/createClient`

1. Restaurant

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

Response: 200 OK and client ID

```json
1
```

2. Hospital

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

Response: 200 OK and client ID

```json
2
```

Other possible statuses: “success”, “client already initialized”, “wrong format”

`GET /reservation/getClient`
Description: Get all clients that are registered with our reservation service. Mainly used for testing purposes, will require root permissions to access.\
Example Usage: `/reservations/getClient?clientId=1`

Response: 200 OK and client information

```json
[
  {
    "clientId": 1,
    "startTime": "09:00:00",
    "endTime": "21:00:00",
    "slotLength": 30,
    "reservationsPerSlot": 2
  },
  {
    "clientId": 2,
    "startTime": "06:00:00",
    "endTime": "18:00:00",
    "slotLength": 60,
    "reservationsPerSlot": 1
  }
]
```

`POST /reservation`\
Description: Create a new reservation.\
Request Body: JSON object representing the reservation details.\
Example Usage: `/reservations/createReservation`

1. Restaurant

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

Response: 200 OK

2. Hospital

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

Response: 200 OK

Other possible statuses: “success”, “client not initialized”, “invalid slot”, “wrong format”

Error if client not initialized
Parse JSON into `ReservationDTO` object
Call `AddNewReservation`, passing in the `ReservationDTO` object
Validation (status code: WRONG FORMAT if incorrect):
Get labels and datatype from Attributes table where clientId = our client
Check if all labels are passed in through JSON
Go through each attribute and add into correct entity based on datatype
Add to Reservations and update Occupancy
Validation (status code: INVALID SLOT if time slot is not valid)
Get all available time slots for the client
Check that requested time slots exist and are free
Return `reservationId` for successful reservation

`PUT /reservations/updateReservation`\
Description: Update an existing reservation or time slot.\
Usage: Applications can use this endpoint to modify reservation details, such as changing the booking time or updating user information.\

Request Body: JSON with reservationId, clientId, and the fields to be updated

Example request: `/reservations/updateReservation`

```json
{
  "reservationId": 1,
  "clientId": 2,
  "notes": "Concern for high blood pressure and diarrhea",
  "doctor": "Dr. Phil"
}
```

Response: 200 OK

Other possible statuses: “success”, “client not initialized”, “wrong format”

Error if client not initialized
Parse JSON using the Jackson library
Status code: WRONG FORMAT if reservationId or clientId not included, or fields to update are not in attributes list for that client
Update given attributes

`DELETE /reservations/deleteReservation`\
Description: Delete a reservation or time slot given a path variable.
Usage: Applications can call this endpoint to remove a reservation when it's no longer needed or has been canceled.
Request Body: `reservationId`
Example request: `/reservations/deleteReservation/1`

```json
1
```

Response: 200 OK

Possible statuses: “success”, “client not initialized”, “no reservation found”

Error if client not initialized, or reservationId not in Reservations
Delete row from Reservations table, update Occupancy, delete relevant rows with the same reservationId from the relevant datatype tables

`GET /reservations/getClientReservations`\
Description: Retrieve a list of all reservations made by a client.\
Query Parameters: `clientId`

Our service will return all reservations made by the client – represented as a simple JSON object with a list of reservation objects.

Example Usage: `/reservations/getClientReservations?clientId=1`
Response: 200 OK

1. Restaurant

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

Example Usage: `/reservations/getClientReservations?clientId=2`
Response: 200 OK

2. Hospital

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

Other possible statuses: “success”, “client not initialized”

# Tests

Using JUnit + Mockito framework

- Unit Tests:

  - `ReservationRepositoryTest.java`
  - `ReservationServiceTest.java`
  - `ReservationTest.java`
  - `ReservationDTOTest.java`
  - `ClientDTOTest.java`

- System Tests:

  - `ReservationControllerTest.java` - Mock server-client communication over network (with localhost) using Intellij's HTTP Client Tool

- Internal Integration Tests:

  - `ServiceIntegrationTest.java`

- External Integration Tests:
  - `ServiceIntegrationTest.java`
  - `ConcurrentClientTest.java`
  - `ReservationRepositoryTest.java`

# Test Documentation

Additional test documentations are located in the `documentation/` folder in the root directory.

- Continuous Integration: CI is set up for the repo such that all build, analysis, and testing tools are automatically run. See report.
- Internal integration: Integration tests are automatically run during CI. See report.
- Branch Coverage: Greater than 85% of branch coverage was achieved. See report.
- Bug Finder: A static analysis bug finder tool is run automatically during CI. See report.
- End-to-end Testing: End-to-end testing of a client was carried out manually and documented.

# Setting up Style Checker

To set up pre commit hooks for linting: <br>
`pip install pre-commit`\
`pre-commit install`

Example Style Checker Reports:\
![](prettier_passed.png)
![](prettier_failed.png)

# How to Build, Run, & Test

- Currently: the PostgreSQL database is being hosted locally. Thus, end-to-end testing of this service can only be done on our local machines. We start up our database, use Maven to build the service, and execute all unit/system tests by manually running them in an IDE.
- Next steps: use Docker to create container for application. From there, our client will be able to run an instance of our program on any machine, since the PostgreSQL database will be hosted on GCP. Further, we will set up a git pre-push hook to automatically run all unit and system tests upon pushing to the repo.

# Code Coverage

To see code coverage, we used Jacoco \
Here are the commands used: \
`mvn clean test jacoco:report`\
Then, open **../BugYourSpot/target/site/jacoco/com.example.bugyourspot.reservation/index.html**

Here is a screenshot of the coverage, with 91% branch coverage\
![](documentation/allCoverage.png)

# Workflow (CI)

We setup the Workflow using Github Actions, with the pipeline triggering the build and test after every push.\
Here is a screenshot of the Github Actions page with our workflows:
![](documentation/gitWorkflow.png)
Here are the specific jobs run within the workflow:
![](documentation/workflowJobs.png)

# Sources

[Spring Boot Tutorial](https://www.youtube.com/watch?v=9SGDpanrc8U&t=1333s)\
[EAV Explained](https://www.youtube.com/watch?v=9SGDpanrc8U&t=1333s)
