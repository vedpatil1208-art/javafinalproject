# College Event Management System

A desktop application in Java (Swing) for managing college events: creating events, registering participants, scheduling activities, tracking attendance, and generating reports. Data is held in memory and the whole application runs from one window.

Case Study 14, B.Tech CSE 2025-29, Semester III, Java Programming.

---

## Features

- **Organizer management:** add and view organizers
- **Event management:** add, view, update, delete events
- **Participant management:** add, view, update, delete participants
- **Registration:** register a participant for an event (unique registration ID starting at 1001), cancel a registration
- **Attendance:** mark a registration as present
- **Scheduling:** add timed activities to an event
- **Search:** find an event or a participant by ID
- **Sorting:** sort events by date or by category
- **Reports:** event report (capacity, registered, available seats) and participant report (registrations and attendance)

---

## Technology

| Item | Detail |
|------|--------|
| Language | Java 8 or higher |
| GUI | Swing (`javax.swing`) and AWT (`java.awt`) |
| Dates | `java.time.LocalDate` |
| Storage | In memory only |

---

## Project Structure

```
JavaFinalProject/
├── EventManagementSystem.java
├── Event.java
├── Participant.java
└── Registration.java
```

| File | Contains | Role |
|------|----------|------|
| `EventManagementSystem.java` | `EventManagementSystem` (extends `JFrame`) | Window, buttons, all operations, search, sort, reports, `main()` |
| `Event.java` | `Event` and `Organizer` | Event data, participant array, schedule list, organizer details |
| `Participant.java` | `Participant` | Participant data with validation |
| `Registration.java` | `Registration` | Link between one participant and one event, with attendance flag |

`Organizer` is declared inside `Event.java`, so there is no separate `Organizer.java`.

---

## Class Design

```
EventManagementSystem
   |-- has many --> Organizer
   |-- has many --> Event ---------- has one --> Organizer
   |                  |-- holds up to capacity --> Participant
   |                  |-- holds --> schedule (LinkedList<String>)
   |-- has many --> Participant
   |-- has many --> Registration ---- links --> Participant + Event
```

### Event

| Field | Type | Description |
|-------|------|-------------|
| `id` | `int` | Unique positive identifier |
| `name`, `category` | `String` | Must not be empty |
| `date` | `LocalDate` | Event date |
| `capacity` | `int` | Maximum participants, greater than 0 |
| `organizer` | `Organizer` | Person responsible |
| `participants` | `Participant[]` | Fixed-size array, length equals capacity |
| `count` | `int` | Number of occupied slots |
| `schedule` | `LinkedList<String>` | Entries stored as `"time - activity"` |

| Method | Behavior |
|--------|----------|
| `Event(...)` | Validates ID, capacity, organizer; creates the array and calls `update()` |
| `update(name, category, date)` | Validates and sets the three editable fields |
| `addParticipant(p)` | Puts the participant in the next free slot; throws `IllegalStateException` if full |
| `removeParticipant(id)` | Finds the participant, shifts later entries left, clears the last slot, decrements the count |
| `addSchedule(time, activity)` | Rejects empty input, otherwise adds `time - activity` |
| `toString()` | Event details with organizer and schedule |

### Organizer

Fields: `id`, `name`, `department`. The constructor throws `IllegalArgumentException` for an ID of 0 or less or an empty name or department.

### Participant

Fields: `id`, `name`, `email`, `course`. `update(name, email, course)` validates that the name and course are not empty and that the email contains `@`. The constructor calls `update()`.

### Registration

Fields: `id`, `participant`, `event`, `date` (today), `attended` (starts as `false`).

| Method | Behavior |
|--------|----------|
| `markAttendance()` | Sets `attended` to `true` |
| `cancelRegistration()` | Calls `event.removeParticipant(...)` to free the seat |

### EventManagementSystem

| Member | Purpose |
|--------|---------|
| `events` | `ArrayList<Event>`, display order, re-sorted by the sort buttons |
| `participants` | `ArrayList<Participant>` |
| `registrations` | `ArrayList<Registration>` |
| `organizers` | `HashMap<Integer, Organizer>` |
| `eventMap` | `TreeMap<Integer, Event>`, lookup of an event by ID, keys sorted |
| `registrationMap` | `HashMap<Integer, Participant>`, registration ID to participant |
| `ask`, `askInt` | Show an input popup and return text or a number |
| `check` | Throws an error when something was not found |
| `getEvent`, `getParticipant`, `getRegistration` | Ask for an ID and return the object or throw "not found" |
| `run` | Wraps every button action in `try/catch` and shows errors in a dialog |

---

## Data Structures Used

| Structure | Where | Why |
|-----------|-------|-----|
| Array `Participant[]` | Inside `Event` | Capacity is fixed, so the array size is the seat limit |
| `LinkedList<String>` | Inside `Event` | Schedule entries kept in the order added |
| `ArrayList` | Events, participants, registrations | Ordered, resizable lists |
| `TreeMap<Integer, Event>` | System | Events by ID, always sorted |
| `HashMap<Integer, Participant>` | System | Registration ID to participant |
| `HashMap<Integer, Organizer>` | System | Organizer lookup by ID |

---

## OOP Concepts Demonstrated

- **Encapsulation:** all fields private, accessed through methods
- **Abstraction:** each real-world entity is its own class
- **Association and composition:** `Event` has an `Organizer`; `Registration` has a `Participant` and an `Event`
- **Inheritance:** `EventManagementSystem` extends `JFrame`
- **Constructors with validation:** invalid objects cannot be created
- **Exception handling:** `IllegalArgumentException`, `IllegalStateException`, `NumberFormatException`, `DateTimeParseException`
- **Method overriding:** `toString()` in the model classes
- **Collections and generics:** typed lists and maps
- **Lambda expressions:** button actions and the sort comparators

---

## Compile and Run

Open a terminal on your own computer, move into the project folder, and paste this to compile and run:

```
javac Event.java Participant.java Registration.java EventManagementSystem.java && java EventManagementSystem
```

`EventManagementSystem` is the only class with `main`, so it is the one to run.

---

## User Guide

Recommended order: add an organizer, add an event, add participants, register, add schedule, mark attendance, view reports.

| Button | Inputs | Result |
|--------|--------|--------|
| Add Organizer | ID, name, department | Organizer stored |
| View Organizers | None | Lists organizers |
| Add Event | ID, name, category, date (`YYYY-MM-DD`), capacity, organizer ID | Event created |
| View Events | None | Lists events with schedules |
| Update Event | Event ID, new name, new category, new date | Event updated |
| Delete Event | Event ID | Event and its registrations removed |
| Add Participant | ID, name, email, course | Participant stored |
| View Participants | None | Lists participants |
| Update Participant | Participant ID, new name, email, course | Participant updated |
| Delete Participant | Participant ID | Participant and their registrations removed |
| Register | Event ID, participant ID | Registration ID shown |
| Cancel Registration | Registration ID | Registration removed, seat freed |
| Mark Attendance | Registration ID | Marked as present |
| Add Schedule | Event ID, time, activity | Entry added to the event |
| Search Event | Event ID | Shows the event |
| Search Participant | Participant ID | Shows the participant |
| Sort by Date | None | Events listed earliest first |
| Sort by Category | None | Events listed A to Z by category |
| Event Report | None | Capacity, registered, and available seats per event |
| Participant Report | None | Totals and attendance per registration |
| Clear | None | Empties the output area |

Pressing Cancel in any popup stops the operation quietly.

---

## Validation and Error Handling

| Situation | Message |
|-----------|---------|
| Non-numeric ID or capacity | Please enter a valid number |
| Wrong date format | Date must be in YYYY-MM-DD format |
| Duplicate event, participant, or organizer ID | `... ID already exists!` |
| Unknown event, participant, organizer, or registration | `... not found!` |
| Event full | Event is full! |
| Same participant registered twice for an event | Participant already registered! |
| Attendance marked twice | Attendance already marked! |
| Email without `@` | Invalid email |
| Empty name, category, course, time, or activity | Validation message from the class |

---

## Known Limitations

- No persistence: data is lost when the window closes
- Capacity and organizer of an event cannot be changed after creation
- Organizers cannot be updated or deleted
- Category is free text
- Search is by ID only
- No check for past dates

---

## Possible Improvements

- Save and load data with files or a database through JDBC
- Show records in a `JTable` instead of text
- Search by name, category, or date range
- Dropdown for category and a date picker
- Export reports to CSV or PDF
- Separate GUI code from business logic
