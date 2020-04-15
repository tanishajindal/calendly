# calendar-app
Build a Calendar Slot Booking service, similar to that of Calendly, which allows people
to define their available slots on a day and other people to book them.

Requirements
This application can be built on any framework which you are comfortable with and feel is best
suited for this.
1. A basic user registration and authentication system.
2. Includes APIs using which a user can define their available slots for a particular day. Assuming that each slot is a fixed hourly slot. For example, 12:00:00-13:00:00 etc.
3. Includes APIs using which a user can book an available slot.
4. Authentication mechanism for Users
5. Added API validations

Postman Collection:
https://www.getpostman.com/collections/181b8818e7ee7ebdce52

Project Structure:

CalendarApplication-
Class contains all the code to setup resources and database mock.

DatabaseMock-
This class is the mocked representation of actual persistence layer. Carries all data in state for now (removes the need of db. Lateron this project can be extended to use a db and persist and read all info from there).

Resources-
LoginResource: Creates token for the passed user 
CalendarResource: Contains endpoints for user creation, slot availabilities submission, getting slot availabilities, reserving slots of attendees by host user
NOTE: Majority of endpoints in CalendarResource needs token to be passed in header generated from login/create user endpoints

Model package- 
Contains all the models/entities involved in system. Uses dropwizard-immutable to generate class definition.

Helper-
LoginHelper: Carries logic for generating tokens and validating user tokens. Responsible for maintaining sessions.
ValidationHelper: Helper methods to validate users/uuids/slots.
CalendarHelper: Carries business logic for all the requirements listed above.
Constants: Carries static final data constants used across classes.

How to run-
build using 'mvn clean install' and run main class.
Run using postman collection attached.
Also refer to the snapshots folder for usage help.

